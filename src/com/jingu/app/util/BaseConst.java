package com.jingu.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;

import com.jingu.app.bean.Update;
import com.jingu.app.bean.UserBean;
import com.jingu.app.service.HttpClientService;

public class BaseConst
{
    // public static final String HTTP_URL =
    // "http://crm.jinguc.com/api/app.php";
    // public static final String HTTP_UPDATE_URL =
    // "http://crm.jinguc.com/app/update.xml";
    //
    // public static final String HTTP_URL =
    // "http://219.156.138.98:8001/api/app.php";
    // public static final String HTTP_UPDATE_URL =
    // "http://219.156.138.98:8001/app/update.xml";

    // public static final String RPARAM = "connect_error";
    // public static final String LOGINSTR = "login_address";
    // public static final String CONFIG = "jingu_config";
    // public static final String SUCCESS = "success";
    // public static final String FAILE = "faile";
    // 访问的act定义
    // public static final String LOGIN_ACT = "login";
    // public static final String CHECK_ACT = "check_job";
    // public static final String READ_ACT = "read_job";
    // public static final String CONFIRM_ACT = "handle_job_save";
    // public static final String GPS_ACT = "send_gps";
    // public static final String CHECK_VERSION_ACT = "check_version";
    //
    // public static final String ADD_CUSTOMER_ACT = "add_com_form";
    // public static final String ADD_CUSTOMER_CONFIRM_ACT = "add_com_save";
    //
    // public static final String ADD_JOB_ACT = "add_job_form";
    // public static final String ADD_JOB_CONFIRM_ACT = "add_job_save";
    //
    // public static final String GET_BOTTLE_INFO = "read_cylinder";
    //
    // public static final String JSON_TYPE_NEW = "newJob";// 暂时为用到，用来区分存量用户和非存量
    // public static final String JSON_TYPE_ALL_NEW = "userandjob";//
    // 暂时未用到，用来区分存量用户和非存量
    // public static final String JSON_COMPANY_ID = "company_id";//
    // 存量用户id(为那个用户新增工单)
    // public static final String JSON_COMPANY_TEL = "company_tel";// 用户电话(唯一标识)
    //
    // public static final String JSON_ATTRS = "service_items";// 用户电话(唯一标识)
    // public static final String JSON_RULES = "service_contents";// 用户电话(唯一标识)
    // public static final String JSON_SERVICE_TIME = "re_service_time_start";//
    // 服务器开始时间
    //
    // public static final String JSON_ACT = "act_flag";// 状态标识 1.新增 2.催办 3.取消
    // public static final String JSON_SRC_ROLL_NUMBER = "src_roll_number";//
    // 关联单号
    //
    // public static final String CONFIRM_STR = "handle_situation";
    // public static final String JSON_TEL = "company_tel";
    //
    // public static final String JOB_NUMS = "job_nums";// 设置存储的工单个数
    // public static final String SCAN_TIMES = "scan_times";// 设置自动刷新的参数

    // public static final String UPDATE_PARAM = "update_param";// 是否自动更新
    // public static final String UPDATE_CONTENT = "update_content";// 更新参数
    // public static final String BOTTLE_CODE = "bottle_code";// 钢瓶编号
    // 分组显示的tag
    // public static final String TAG_NEW = "新增";
    // public static final String TAG_CUIBAN = "催办";
    // public static final String TAG_CANCEL = "取消";

    // 扫码popMeun
    // public static final int SCAN_BOTTLE_CONFIRM = 1;// 扫码提交
    // public static final int SCAN_BOTTLES_CONFIRM = 2;// 批量扫码
    // public static final int SCAN_BOTTLE_RETURN = 3;// 结合扫码

    // 定义GPS更新参数
    // public static final long minTime = 2000;// 2000ms
    // public static final float minDistance = 10;// 10m

    // 监听home键的接收器
    private static MyHomeWatcherReceiver mHomeKeyReceiver = null;

    public static String getDate(Date date)
    {
	SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
	return sdf2.format(date);
    }

    public static String getDate2(Date date)
    {
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	return sdf1.format(date);
    }

    /**
     * 时间格式化函数
     * 
     * @param date
     * @return
     */
    public static String getDate(String date)
    {
	Date nowDate = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
	String returnString = "";
	try
	{
	    Date date1 = sdf1.parse(date);
	    long diff = nowDate.getTime() - date1.getTime();
	    long days = diff / (1000 * 60 * 60 * 24);

	    @SuppressWarnings("deprecation")
	    int hours = date1.getHours();
	    if (days == 0)
	    {
		// 今天
		if (hours < 12)
		{
		    returnString = "上午" + sdf2.format(date1);
		}
		else
		{
		    hours = hours - 12;
		    if (hours > 6)
		    {
			returnString = "晚上" + sdf2.format(date1);

		    }
		    else
		    {
			returnString = "下午" + sdf2.format(date1);
		    }
		}
	    }
	    else if (days == 1)
	    {
		// 昨天
		if (hours < 12)
		{
		    returnString = "昨天上午";
		}
		else
		{
		    hours = hours - 12;
		    if (hours > 6)
		    {
			returnString = "昨天晚上";

		    }
		    else
		    {
			returnString = "昨天下午";
		    }
		}
	    }
	    else if (days == 2)
	    {
		// 前天
		if (hours < 12)
		{
		    returnString = "前天上午";
		}
		else
		{
		    hours = hours - 12;
		    if (hours > 6)
		    {
			returnString = "前天晚上";

		    }
		    else
		    {
			returnString = "前天下午";
		    }
		}
	    }
	    else
	    {
		returnString = sdf.format(date1);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return returnString;
    }

    public static void registerHomeKeyReceiver(Context context)
    {
	mHomeKeyReceiver = new MyHomeWatcherReceiver();
	final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
	context.registerReceiver(mHomeKeyReceiver, homeFilter);
    }

    public static void unregisterHomeKeyReceiver(Context context)
    {
	if (null != mHomeKeyReceiver)
	{
	    context.unregisterReceiver(mHomeKeyReceiver);
	}
    }

    /**
     * 0:退出到后台 1:整体退出
     * 
     * @param i
     */
    public static void updateExit(Context context, int i)
    {
	SharedPreferences settings = context.getSharedPreferences("setting", 0);
	SharedPreferences.Editor editor = settings.edit();
	editor.putInt("exit", i);
	if (i == 1)
	{
	    editor.putString("password", "");
	}
	editor.commit();
    }

    /**
     * 更新当前用户 (用户登录成功后，会更新最新的用户信息到参数中，全局参数)
     * 
     * @param context
     * @param un
     * @param pw
     */
    public static void updateUser(Context context, String un, String pw)
    {
	// 用户名、密码促入到SharedPreferences中
	SharedPreferences settings = context.getSharedPreferences("setting", 0);
	SharedPreferences.Editor editor = settings.edit();
	editor.putString("username", un);
	editor.putString("password", pw);
	editor.commit();
	// 存入到全局变量
	MyApplication app = (MyApplication) context.getApplicationContext();
	UserBean userBean = new UserBean(un, pw);
	app.setUser(userBean);
    }

    /**
     * 获取当前用户信息,用户名唯一
     * 
     * @param context
     * @return
     */
    public static UserBean getUser(Context context)
    {
	SharedPreferences settings = context.getSharedPreferences("setting", 0);
	String username = settings.getString("username", "");
	String password = settings.getString("password", "");
	UserBean user = new UserBean(username, password);
	return user;
    }

    /**
     * 获取用户参数，如果说获取失败了，那就重新读取参数
     * 
     * @return
     */
    public static UserBean getUserFromApplication(Context context)
    {
	MyApplication app = (MyApplication) context.getApplicationContext();
	UserBean u = app.getUser();
	if (u == null || "".equals(u.getUsername()) || u.getUsername() == null || "".equals(u.getPassword())
		|| u.getPassword() == null)
	{
	    u = getUser(context);
	    app.setUser(u);
	    // 从全局对象中获取数据失败，记录日志
	    StringBuffer str = new StringBuffer();
	    str.append("从全局对象中获取参数失败，重新读取参数，用户名为：").append(u.getUsername()).append(",密码为：").append(u.getPassword())
		    .append("\n");
	    FillUtil.writeLogToFile(str.toString());
	}
	return u;
    }

    /**
     * 获取参数
     * 
     * @param context
     * @param name
     * @return
     */
    public static String getParams(Context context, String name)
    {
	SharedPreferences settings = context.getSharedPreferences("setting", 0);
	String params = settings.getString(name, "50");
	return params;
    }

    /**
     * 设置参数
     * 
     * @param context
     * @param pname
     * @param pstr
     */
    public static void setParams(Context context, String pname, String pstr)
    {
	SharedPreferences settings = context.getSharedPreferences("setting", 0);
	SharedPreferences.Editor editor = settings.edit();
	editor.putString(pname, pstr);
	editor.commit();
    }

    /**
     * 检查版本
     * 
     * @return
     * @throws Exception
     */
    public static Update checkVersion() throws Exception
    {
	try
	{
	    return Update
		    .parse(HttpClientService.getInputStreambyURL(MyApplication.getInstance().getHttp_update_url()));
	}
	catch (Exception e)
	{
	    throw e;
	}
    }

    /**
     * 判断当前应用是否在后台运行，这个暂时不用
     * 
     * @param mContext
     * @param className
     * @return
     */
    public static boolean isServiceRunning(Context mContext, String className)
    {
	boolean IsRunning = false;
	ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
	List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
	if (!(serviceList.size() > 0)) { return false; }
	for (int i = 0; i < serviceList.size(); i++)
	{
	    if (serviceList.get(i).service.getClassName().equals(className) == true)
	    {
		IsRunning = true;
		break;
	    }
	}
	return IsRunning;
    }

    /**
     * 获取app版本
     * 
     * @param mContext
     * @return
     */
    public static String getAppVersion(Context mContext)
    {
	try
	{
	    return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
	}
	catch (NameNotFoundException e)
	{
	    e.printStackTrace();
	    return "version_error";
	}
    }

    /**
     * 获取手机型号
     * 
     * @return
     */
    public static String getPhoneName()
    {
	return android.os.Build.MODEL;
    }
}
