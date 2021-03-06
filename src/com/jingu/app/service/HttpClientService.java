package com.jingu.app.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.jingu.app.bean.AddFormBean;
import com.jingu.app.bean.JobBean;
import com.jingu.app.bean.ParamBean;
import com.jingu.app.bean.UserBean;
import com.jingu.app.dao.DBJobInfoDao;
import com.jingu.app.util.BaseConst;
import com.jingu.app.util.CustomerHttpClient;
import com.jingu.app.util.FillUtil;

/**
 * @author Administrator 关于发送http请求的服务类 1.用户登录 2.请求最新工单 3.发送工单被阅读的消息 4.提交工单处理结果
 */
public class HttpClientService
{
    public static String TAG = "HttpTag";

    /**
     * 用户登录 线程方式登录
     * 
     * @param user
     * @return
     */
    public static String getRpostOfLogin(Context context, UserBean user)
    {
	NameValuePair param1 = new BasicNameValuePair("username", user.getUsername());
	NameValuePair param2 = new BasicNameValuePair("password", user.getPassword());
	NameValuePair param3 = new BasicNameValuePair("version", BaseConst.getAppVersion(context));
	NameValuePair param4 = new BasicNameValuePair("mphone_brand", BaseConst.getPhoneName());
	NameValuePair param5 = new BasicNameValuePair("act", "login");
	// 获取访问服务器登录的url
	try
	{
	    String returnStr = CustomerHttpClient.post(param1, param2, param3, param4, param5);
	    return returnStr;
	}
	catch (Exception e)
	{
	    return e.getMessage();
	}
    }

    /**
     * 根据时间段查询对应时间段的工单列表
     * 
     * @param context
     * @param bDate
     * @param eDate
     * @return
     */
    public static List<JobBean> getCheckAllJob(Context context, String bDate, String eDate, String typeStr)
    {
	List<JobBean> liBeans = new ArrayList<JobBean>();
	UserBean uBean = BaseConst.getUserFromApplication(context);
	NameValuePair param1 = new BasicNameValuePair("username", uBean.getUsername());
	NameValuePair param2 = new BasicNameValuePair("password", uBean.getPassword());
	NameValuePair param3 = new BasicNameValuePair("start_time", bDate);
	NameValuePair param4 = new BasicNameValuePair("end_time", eDate);
	NameValuePair param5 = new BasicNameValuePair("handle_status", typeStr);
	NameValuePair param6 = new BasicNameValuePair("act", "all_job");

	try
	{
	    String return_str = CustomerHttpClient.post(param1, param2, param3, param4, param5,param6);
	    Log.i(TAG, "Send get_All_job Message!return_message is:" + return_str);
	    if (return_str == null || "".equals(return_str)) { return null; }
	    JSONArray arr = new JSONArray(return_str);
	    int repostNum = arr.length();
	    for (int i = 0; i < repostNum; i++)
	    {
		JSONObject temp = (JSONObject) arr.get(i);
		JobBean job = new JobBean(temp.getString("roll_number"), temp.getString("payroll_title"),
			temp.getString("roll_content"), uBean.getUsername(), temp.getString("company_tel"),
			temp.getString("re_service_time_start"), temp.getString("handle_status"));
		liBeans.add(job);
	    }
	    return liBeans;
	}
	catch (Exception e)
	{
	    StringBuffer sql = new StringBuffer();
	    sql.append("get All job error ,the error msg is:");
	    sql.append(e.getMessage());
	    FillUtil.writeLogToFile(sql.toString());
	    return null;
	}
    }

    /**
     * 请求新的工单信息 将新工单插入数据库 返回获取的工单条数
     * 
     * @return
     */
    public static int getRepostOfNewJob(Context context)
    {
	DBJobInfoDao dInfoDao = new DBJobInfoDao(context);
	UserBean uBean = BaseConst.getUserFromApplication(context);
	int repostNum = 0;
	try
	{
	    NameValuePair param1 = new BasicNameValuePair("username", uBean.getUsername());
	    NameValuePair param2 = new BasicNameValuePair("password", uBean.getPassword());
	    NameValuePair param3 = new BasicNameValuePair("act", "check_job");
	    // 获取json格式的工单信息
	    String return_str = CustomerHttpClient.post(param1, param2, param3);
	    Log.i(TAG, "Send Check_Job Message!return_message is:" + return_str);
	    if (return_str == null || "".equals(return_str))
	    {
		dInfoDao.closeDB();
		return repostNum;
	    }
	    JSONArray arr = new JSONArray(return_str);
	    repostNum = arr.length();
	    for (int i = 0; i < repostNum; i++)
	    {
		// act_flag 状态标识 1.新增 2.催办 3.取消
		JSONObject temp = (JSONObject) arr.get(i);
		String act_flag = temp.getString("act_flag");
		if ("2".equals(act_flag) || "3".equals(act_flag))
		{
		    // 如果是催办(2)或者取消(3)，更新对应工单的状态
		    String src_roll_number = temp.getString("src_roll_number");
		    if ("2".equals(act_flag)) act_flag = "C";
		    if ("3".equals(act_flag)) act_flag = "Q";
		    dInfoDao.updateType(src_roll_number, act_flag);
		}
		else
		{
		    // 插入新工单
		    final JobBean job = new JobBean(temp.getString("roll_number"), temp.getString("payroll_title"),
			    temp.getString("roll_content"), uBean.getUsername(), temp.getString("company_tel"),
			    temp.getString("re_service_time_start"));
		    // 将新工单插入到数据库中
		    dInfoDao.add(job);
		}
	    }
	    dInfoDao.closeDB();
	}
	catch (Exception e)
	{
	    Log.i(TAG, e.getMessage());
	    dInfoDao.closeDB();
	}
	finally
	{
	    dInfoDao.closeDB();
	}
	return repostNum;
    }

    /**
     * 发送阅读了工单的方法
     * 
     * @param context
     * @param jobid
     */
    public static boolean sendMsgOfReadJob(Context context, String jobid)
    {
	// 更新状态
	DBJobInfoDao dInfoDao = new DBJobInfoDao(context);
	dInfoDao.updateState(jobid, "R");
	dInfoDao.closeDB();
	UserBean uBean = BaseConst.getUserFromApplication(context);
	// 发送已阅读消息
	NameValuePair param1 = new BasicNameValuePair("username", uBean.getUsername());
	NameValuePair param2 = new BasicNameValuePair("password", uBean.getPassword());
	NameValuePair param3 = new BasicNameValuePair("act", "read_job");
	NameValuePair param4 = new BasicNameValuePair("roll_number", jobid);
	try
	{

	    String return_str = CustomerHttpClient.post(param1, param2, param3, param4);
	    Log.i(TAG, return_str);
	    if ("success".equals(getState(return_str))) { return true; }
	    return false;
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    Log.i(TAG, e.getMessage());
	    return false;
	}

    }

    /**
     * 处理工单，发送工单处理情况到服务器 成功：返回success 失败：返回faile或者失败信息
     */
    public static boolean sendConfirmJob(Context context, JobBean jobBean, String result)
    {
	DBJobInfoDao dInfoDao = new DBJobInfoDao(context);
	UserBean uBean = BaseConst.getUserFromApplication(context);
	NameValuePair param1 = new BasicNameValuePair("username", uBean.getUsername());
	NameValuePair param2 = new BasicNameValuePair("password", uBean.getPassword());
	NameValuePair param3 = new BasicNameValuePair("act", "handle_job_save");
	NameValuePair param4 = new BasicNameValuePair("roll_number", jobBean.getJobId());
	NameValuePair param5 = new BasicNameValuePair("handle_situation", jobBean.getJobReply());
	NameValuePair param6 = new BasicNameValuePair("code", result);
	try
	{
	    String return_josn = CustomerHttpClient.post(param1, param2, param3, param4, param5, param6);
	    if ("success".equals(getState(return_josn)))
	    {
		// 如果提交成功了，那么更新工单到数据库中
		dInfoDao.update(jobBean.getJobId(), jobBean.getJobReply(),
			Integer.parseInt(BaseConst.getParams(context, "job_nums")));
		dInfoDao.closeDB();
		return true;
	    }
	    else
	    {
		dInfoDao.closeDB();
		String des = getDes(return_josn);
		if (!"".equals(des) || des != null)
		{
		    Log.i(TAG, des);
		}
		return false;
	    }
	}
	catch (Exception e)
	{
	    Log.i(TAG, "Handler_job_sava  Failed!" + e.getMessage());
	    dInfoDao.closeDB();
	    return false;
	}
    }

    /**
     * 发送位置信息，经纬度
     * 
     * @param context
     * @param latitude
     * @param longitude
     */
    public static void sendGpsMsg(Context context, String latitude, String longitude)
    {
	UserBean uBean = BaseConst.getUserFromApplication(context);
	NameValuePair param1 = new BasicNameValuePair("username", uBean.getUsername());
	NameValuePair param2 = new BasicNameValuePair("password", uBean.getPassword());
	NameValuePair param3 = new BasicNameValuePair("act", "send_gps");
	NameValuePair param4 = new BasicNameValuePair("latitude", latitude);
	NameValuePair param5 = new BasicNameValuePair("longitude", longitude);
	try
	{
	    CustomerHttpClient.post(param1, param2, param3, param4, param5);
	}
	catch (Exception e)
	{
	    Log.i(TAG, "发送GPS失败,可能和网络有关系!");
	}
    }

    /**
     * 发送新开户请求
     * 
     * @return
     */
    public static AddFormBean getRepostOfAddCustomerInfo(Context context)
    {
	UserBean uBean = BaseConst.getUserFromApplication(context);
	NameValuePair param1 = new BasicNameValuePair("username", uBean.getUsername());
	NameValuePair param2 = new BasicNameValuePair("password", uBean.getPassword());
	NameValuePair param3 = new BasicNameValuePair("act", "add_com_form");
	try
	{
	    String josnStr = CustomerHttpClient.post(param1, param2, param3);
	    Log.i(TAG, josnStr);
	    AddFormBean aFormBean = AnalyistJosnService.getAddUserInfo(josnStr);
	    return aFormBean;
	}
	catch (Exception e)
	{
	    Log.i(TAG, "请求新开户失败!");
	    return null;
	}
    }

    /**
     * 提交新开户
     * 
     * @param list
     * @return
     */
    public static boolean postAddCustomerInfo(Context context, List<ParamBean> list)
    {
	UserBean uBean = BaseConst.getUserFromApplication(context);
	List<NameValuePair> formparams = new ArrayList<NameValuePair>();
	NameValuePair param1 = new BasicNameValuePair("username", uBean.getUsername());
	formparams.add(param1);
	NameValuePair param2 = new BasicNameValuePair("password", uBean.getPassword());
	formparams.add(param2);
	NameValuePair param3 = new BasicNameValuePair("act", "add_com_save");
	formparams.add(param3);
	for (int i = 0; i < list.size(); i++)
	{
	    ParamBean pBean = list.get(i);
	    NameValuePair param = new BasicNameValuePair(pBean.getParamName(), pBean.getParamValue());
	    formparams.add(param);
	}
	try
	{
	    String ret_json = CustomerHttpClient.post(formparams);
	    if (!"success".equals(getState(ret_json)) || ret_json == null)
	    {
		// 提交失败了
		Log.i(TAG, ret_json);
		return false;
	    }
	    return true;
	}
	catch (Exception e)
	{
	    Log.i(TAG, e.getMessage());
	    return false;
	}
    }

    /**
     * 发送新增工单请求
     * 
     * @param telNum
     * @return
     */
    public static AddFormBean getRepostOfAddJobInfo(Context context, String telNum, String addrStr)
    {
	UserBean uBean = BaseConst.getUserFromApplication(context);
	String un = uBean.getUsername();
	String up = uBean.getPassword();
	String JsonStr = "";
	NameValuePair param1 = new BasicNameValuePair("username", un);
	NameValuePair param2 = new BasicNameValuePair("password", up);
	NameValuePair param3 = new BasicNameValuePair("act", "add_job_form");
	NameValuePair param4 = new BasicNameValuePair("customer_tel", telNum);
	NameValuePair param5 = new BasicNameValuePair("customer_addr", addrStr);
	try
	{
	    // String
	    // josnStr="{\"state\":\"success\",\"type\":\"userandjob\",\"company_id\":\"\",\"des\":\"\",\"paras\":[{\"fd_name\":\"user_name\",\"type\":\"1\",\"name\":\"姓名\",\"value\":\"\",\"choice_yesno\":\"2\"},{\"fd_name\":\"user_tel\",\"type\":\"1\",\"name\":\"电话\",\"value\":\"\",\"choice_yesno\":\"2\"},{\"fd_name\":\"user_address\",\"type\":\"1\",\"name\":\"地址\",\"value\":\"\",\"choice_yesno\":\"1\"},{\"fd_name\":\"job_title\",\"type\":\"1\",\"name\":\"工单标题\",\"value\":\"\",\"choice_yesno\":\"2\"},{\"fd_name\":\"job_content\",\"type\":\"1\",\"name\":\"工单内容\",\"value\":\"\",\"choice_yesno\":\"2\"}],\"service_items\":[{\"fd_name\":\"1\",\"name\":\"租瓶\",\"value\":[{\"fd_name\":\"11\",\"name\":\"租用蓝天瓶\"},{\"fd_name\":\"12\",\"name\":\"租用其他瓶\"}]},{\"fd_name\":\"2\",\"name\":\"售瓶\",\"value\":[{\"fd_name\":\"21\",\"name\":\"出售蓝天瓶\"},{\"fd_name\":\"22\",\"name\":\"售出蓝天瓶\"}]}],\"service_contents\":[{\"fd_name\":\"3\",\"name\":\"5KG\"},{\"fd_name\":\"4\",\"name\":\"15KG\"},{\"fd_name\":\"5\",\"name\":\"45KG\"}]}";
	    JsonStr = CustomerHttpClient.post(param1, param2, param3, param4, param5);
	    Log.i(TAG, JsonStr);
	    if (!"success".equals(getState(JsonStr)) || JsonStr == null)
	    {
		// 没有访问成功
		StringBuffer sql = new StringBuffer();
		sql.append("新增工单操作失败，未通过服务器验证，收到的Http回复为：").append(JsonStr).append("\n");
		sql.append("此时的Context为：").append(context).append("\n");
		sql.append("此时的Context数据为：").append(context.getPackageName()).append(",")
			.append(context.getApplicationContext()).append("\n");
		sql.append("此时的用户名userName=").append(un).append("\n");
		sql.append("此时的密码为passWord=").append(up).append("\n");
		FillUtil.writeLogToFile(sql.toString());
		return null;
	    }
	    else
	    {
		AddFormBean aFormBean = AnalyistJosnService.getAddJobInfo(JsonStr);
		return aFormBean;
	    }
	}
	catch (Exception e)
	{
	    Log.i(TAG, "发送新增工单请求失败，失败信息是：!" + e.getMessage());

	    StringBuffer logStr = new StringBuffer();
	    logStr.append("新增工单请求操作失败了，失败描述如下：\n").append("\n");
	    logStr.append("此时的用户名userName=").append(un).append("\n");
	    logStr.append("此时的密码为passWord=").append(up).append("\n");
	    logStr.append("收到的Http回复为：" + JsonStr).append("\n");
	    logStr.append("造成失败的原因为:").append(e.getMessage()).append("\n");
	    FillUtil.writeLogToFile(logStr.toString());
	    return null;
	}
    }

    /**
     * 提交新增工单
     * 
     * @param list
     * @return
     */
    public static boolean postAddJobInfo(Context context, List<ParamBean> list, String flag, JobBean job, String code)
    {
	UserBean uBean = BaseConst.getUserFromApplication(context);
	DBJobInfoDao dInfoDao = new DBJobInfoDao(context);
	List<NameValuePair> formparams = new ArrayList<NameValuePair>();
	NameValuePair param1 = new BasicNameValuePair("username", uBean.getUsername());
	formparams.add(param1);
	NameValuePair param2 = new BasicNameValuePair("password", uBean.getPassword());
	formparams.add(param2);
	NameValuePair param3 = new BasicNameValuePair("act", "add_job_save");
	formparams.add(param3);
	NameValuePair param4 = new BasicNameValuePair("flag", flag);
	formparams.add(param4);
	NameValuePair param5 = new BasicNameValuePair("code", code);
	formparams.add(param5);
	for (int i = 0; i < list.size(); i++)
	{
	    ParamBean pBean = list.get(i);
	    NameValuePair param = new BasicNameValuePair(pBean.getParamName(), pBean.getParamValue());
	    formparams.add(param);
	}
	try
	{
	    Log.i(TAG, "Send Confirm add new job!");
	    String ret_json = CustomerHttpClient.post(formparams);
	    if (!"success".equals(getState(ret_json)) || ret_json == null)
	    {
		// 提交失败了
		Log.i(TAG, "Confirm form add new job is error,the returen josn is:" + ret_json);
		dInfoDao.closeDB();
		return false;
	    }
	    // 如果是直办工单的话，本地保存一份
	    if ("1".equals(flag))
	    {
		String rolNumber = getJson(ret_json, "roll_number");
		if (!"".equals(rolNumber) && rolNumber != null)
		{
		    String jobContent = "受理号:" + rolNumber + "\n" + job.getJobContent();
		    job.setJobContent(jobContent);
		}
		// 将工单存到本地
		dInfoDao.add2(job, Integer.parseInt(BaseConst.getParams(context, "job_nums")));
		dInfoDao.closeDB();
	    }
	    return true;
	}
	catch (Exception e)
	{
	    Log.i(TAG, "Confirm form add new job is error , the msg is :" + e.getMessage());
	    dInfoDao.closeDB();
	    return false;
	}
    }

    /**
     * 获取钢瓶信息
     * 
     * @param strNums
     * @return
     */
    public static String getRepostOfBotterInfo(Context context, String strNums)
    {
	UserBean uBean = BaseConst.getUserFromApplication(context);
	NameValuePair param1 = new BasicNameValuePair("username", uBean.getUsername());
	NameValuePair param2 = new BasicNameValuePair("password", uBean.getPassword());
	NameValuePair param3 = new BasicNameValuePair("act", "read_cylinder");
	NameValuePair param4 = new BasicNameValuePair("code", strNums);
	try
	{
	    String josnStr = CustomerHttpClient.post(param1, param2, param3, param4);
	    Log.i(TAG, josnStr);
	    return getContent(josnStr);
	}
	catch (Exception e)
	{
	    Log.i(TAG, "获取钢瓶档案失败，失败信息是：!" + e.getMessage());
	    return null;
	}
    }

    /**
     * 获取http访问结果
     * 
     * @param josntr
     * @return
     */
    public static String getState(String josntr)
    {
	JSONObject jsonObject;
	try
	{
	    jsonObject = new JSONObject(josntr);
	    String state = jsonObject.getString("state");
	    return state;
	}
	catch (JSONException e)
	{
	    Log.i(TAG, e.getMessage() + josntr);
	    return null;
	}
    }

    /**
     * http访问失败，获取原因
     * 
     * @param josntr
     * @return
     */
    public static String getDes(String josntr)
    {
	JSONObject jsonObject;
	try
	{
	    jsonObject = new JSONObject(josntr);
	    String des = jsonObject.getString("des");
	    return des;
	}
	catch (JSONException e)
	{
	    Log.i(TAG, e.getMessage() + josntr);
	    return null;
	}
    }

    /**
     * 获取内容
     * 
     * @param josntr
     * @return
     */
    public static String getContent(String josntr)
    {
	JSONObject jsonObject;
	try
	{
	    jsonObject = new JSONObject(josntr);
	    String des = jsonObject.getString("content");
	    return des;
	}
	catch (JSONException e)
	{
	    Log.i(TAG, e.getMessage() + josntr);
	    return null;
	}
    }

    /**
     * 解析json
     * 
     * @param josnStr
     * @param parasName
     * @return
     */
    public static String getJson(String josnStr, String parasName)
    {
	JSONObject jsonObject;
	try
	{
	    jsonObject = new JSONObject(josnStr);
	    String des = jsonObject.getString(parasName);
	    return des;
	}
	catch (JSONException e)
	{
	    Log.i(TAG, e.getMessage() + josnStr);
	    return null;
	}
    }

    /**
     * 判断当前APP的网络情况
     * 
     * @param context
     * @return
     */
    public static boolean isConnect(Context context)
    {
	// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
	try
	{
	    ConnectivityManager connectivity = (ConnectivityManager) context
		    .getSystemService(Context.CONNECTIVITY_SERVICE);
	    if (connectivity != null)
	    {
		// 获取网络连接管理的对象
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		if (info != null && info.isConnected())
		{
		    // 判断当前网络是否已经连接
		    if (info.getState() == NetworkInfo.State.CONNECTED) { return true; }
		}
	    }
	}
	catch (Exception e)
	{
	    Log.v("error", e.toString());
	}
	return false;
    }

    /**
     * 根据网络地址，返回具体的文件流
     * 
     * @param url
     * @return
     */
    public static InputStream getInputStreambyURL(String url)
    {
	URL urlDownload;
	InputStream is = null;
	try
	{
	    urlDownload = new URL(url);
	    HttpURLConnection connection = (HttpURLConnection) urlDownload.openConnection();
	    connection.connect();
	    is = connection.getInputStream();
	}
	catch (MalformedURLException e)
	{
	    e.printStackTrace();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	return is;
    }

    /**
     * 直接从网络读取图片
     * 
     * @param url
     * @return
     */
    public static Bitmap getImg(String url)
    {
	URL imgurl = null;
	Bitmap bitmap = null;
	try
	{
	    imgurl = new URL(url);

	}
	catch (MalformedURLException e)
	{
	    e.printStackTrace();
	}
	try
	{
	    HttpURLConnection conn = (HttpURLConnection) imgurl.openConnection();
	    conn.setRequestMethod("POST");
	    conn.setDoInput(true);
	    conn.setDoOutput(true);
	    conn.setUseCaches(false);
	    conn.connect();
	    InputStream is = conn.getInputStream();
	    bitmap = BitmapFactory.decodeStream(is);
	    is.close();
	    conn.disconnect();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	return bitmap;

    }

}
