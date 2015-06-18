package com.jingu.app.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.jingu.app.bean.AddFormBean;
import com.jingu.app.bean.AttrBean;
import com.jingu.app.bean.ItemBean;
import com.jingu.app.bean.MeunBean;
import com.jingu.app.bean.ParamBean;

/**
 * @author zhouc52 动态新增
 */
public class AnalyistJosnService
{
    public static String TAG = "jin_gu_add_user";

    /**
     * 新开户，json数据解析
     * 
     * @param JosnStr
     * @return
     */
    public static AddFormBean getAddUserInfo(String JosnStr)
    {
	try
	{
	    AddFormBean formBean = new AddFormBean();
	    JSONObject jsonObject = new JSONObject(JosnStr);
	    String states = jsonObject.getString("state");
	    formBean.setState(states);
	    formBean.setDes(jsonObject.getString("des"));
	    if (!"success".equals(states) || "".equals(states) || states == null) { return null; }
	    // 开始解析数组
	    List<ItemBean> itemList = new ArrayList<ItemBean>();
	    JSONArray jsonArray = jsonObject.getJSONArray("paras");
	    int nums = jsonArray.length();
	    for (int i = 0; i < nums; i++)
	    {
		JSONObject jObject = (JSONObject) jsonArray.opt(i);
		ItemBean iBean = new ItemBean();
		iBean.setFd_name(jObject.getString("fd_name"));
		iBean.setType(jObject.getString("type"));
		iBean.setName(jObject.getString("name"));
		iBean.setItemList(getValueList(jObject.getString("value")));
		itemList.add(iBean);
	    }
	    formBean.setItemList(itemList);
	    return formBean;
	}
	catch (JSONException e)
	{
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 新增工单json解析
     * 
     * @param JosnStr
     * @return
     * @throws JSONException
     */
    public static AddFormBean getAddJobInfo(String JosnStr) throws JSONException
    {
	AddFormBean formBean = new AddFormBean();
	JSONObject jsonObject = new JSONObject(JosnStr);
	String states = jsonObject.getString("state");
	formBean.setState(states);
	formBean.setDes(jsonObject.getString("des"));
	formBean.setType(jsonObject.getString("type"));
	formBean.setCompand_id(jsonObject.getString("company_id"));
	if (!"success".equals(states) || "".equals(states) || states == null) { return null; }
	// 开始解析参数数据
	List<ItemBean> itemList = new ArrayList<ItemBean>();
	JSONArray jsonArray = jsonObject.getJSONArray("paras");
	int nums = jsonArray.length();
	for (int i = 0; i < nums; i++)
	{
	    JSONObject jObject = (JSONObject) jsonArray.opt(i);
	    ItemBean iBean = new ItemBean();
	    iBean.setFd_name(jObject.getString("fd_name"));
	    iBean.setType(jObject.getString("type"));
	    iBean.setName(jObject.getString("name"));
	    iBean.setItemList(getValueList(jObject.getString("value")));
	    itemList.add(iBean);
	}
	formBean.setItemList(itemList);
	// ArrtBean
	AttrBean aBean = new AttrBean();
	// 开始解析attrs
	List<ParamBean> paramBeans = new ArrayList<ParamBean>();
	List<MeunBean> mList = new ArrayList<MeunBean>();
	JSONArray jsonArray2 = jsonObject.getJSONArray("service_items");
	for (int i = 0; i < jsonArray2.length(); i++)
	{
	    MeunBean mbBean = new MeunBean();
	    JSONObject jObject = (JSONObject) jsonArray2.opt(i);
	    mbBean.setMenuId(jObject.getString("value"));
	    mbBean.setMenuName(jObject.getString("name"));
	    // 解析二级
	    JSONArray jsonArray3 = jObject.getJSONArray("child");
	    List<ParamBean> pbList = new ArrayList<ParamBean>();
	    for (int j = 0; j < jsonArray3.length(); j++)
	    {
		ParamBean pBean = new ParamBean();
		JSONObject jObject2 = (JSONObject) jsonArray3.opt(j);
		pBean.setParamName(jObject2.getString("value"));
		pBean.setParamValue(jObject2.getString("name"));
		pbList.add(pBean);
		paramBeans.add(pBean);
	    }
	    mbBean.setSecond(pbList);
	    mList.add(mbBean);
	}
	aBean.setmList(mList);
	formBean.setListParamBeans(paramBeans);
	// 开始解析rules
	JSONArray jsonArray3 = jsonObject.getJSONArray("service_contents");
	List<ParamBean> pBeans = new ArrayList<ParamBean>();
	for (int i = 0; i < jsonArray3.length(); i++)
	{
	    ParamBean pBean = new ParamBean();
	    JSONObject jObject = (JSONObject) jsonArray3.opt(i);
	    pBean.setParamName(jObject.getString("value"));
	    pBean.setParamValue(jObject.getString("name"));
	    pBeans.add(pBean);
	}
	aBean.setrList(pBeans);
	// set二级菜单参数
	formBean.setaBean(aBean);
	return formBean;
    }

    /**
     * 解析item内容
     * 
     * @param str
     * @return
     */
    public static List<String> getValueList(String str)
    {
	if ("".equals(str) || str == null)
	{
	    return null;
	}
	else
	{
	    String[] strarray = str.split(",");
	    List<String> aList = new ArrayList<String>();
	    for (int i = 0; i < strarray.length; i++)
	    {
		aList.add(strarray[i]);
	    }
	    return aList;
	}
    }

    /**
     * 获取一级菜单
     * 
     * @return
     */
    public static String[] getFirstMune(AttrBean abBean)
    {
	List<MeunBean> mList = abBean.getmList();
	if (mList == null || mList.size() < 1)
	{
	    return null;
	}
	else
	{
	    int nums = mList.size();
	    String str[] = new String[nums];
	    for (int i = 0; i < nums; i++)
	    {
		str[i] = mList.get(i).getMenuName();
	    }
	    return str;
	}
    }

    /**
     * 获取二级菜单
     * 
     * @return
     */
    public static String[][] getSecondMune(AttrBean abBean)
    {
	List<MeunBean> mList = abBean.getmList();
	if (mList == null || mList.size() < 1)
	{
	    return null;
	}
	else
	{
	    int nums = mList.size();
	    String str[][] = new String[nums][];
	    for (int i = 0; i < nums; i++)
	    {
		List<ParamBean> pList = mList.get(i).getSecond();
		int nums2 = pList.size();
		str[i] = new String[nums2];
		for (int j = 0; j < pList.size(); j++)
		{
		    str[i][j] = pList.get(j).getParamValue();
		}
	    }
	    return str;
	}
    }

    /**
     * 根据中文名获取对应菜单的ID---其实使用haspmap效率会更高一些，算了，以后再说吧
     * 
     * @param Name
     * @return
     */
    public static String getMuneIdByName(String Name, List<ParamBean> pBeans)
    {
	for (int i = 0; i < pBeans.size(); i++)
	{
	    ParamBean pBean = pBeans.get(i);
	    if (Name.equals(pBean.getParamValue()) || Name == pBean.getParamValue()) { return pBean.getParamName(); }
	}
	Log.i(TAG, "出错了，根据已有的名称，没找到对应的iD");
	return "";
    }
}
