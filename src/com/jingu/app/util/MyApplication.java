package com.jingu.app.util;

import java.util.LinkedList;
import java.util.List;

import com.jingu.app.bean.UserBean;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

public class MyApplication extends Application
{
    private List<Activity> activityList = new LinkedList<Activity>();
    private static MyApplication instance;

    private UserBean userBean;
    private String http_update_url;
    private String http_url;

    @Override
    public void onCreate()
    {
	Log.i("JINGU", "Now in the OnCreate of Application!!");
	userBean = new UserBean();
	super.onCreate();
    }

    public String getHttp_url()
    {
	return http_url;
    }

    public void setHttp_url(String http_url)
    {
	this.http_url = http_url;
    }

    public String getHttp_update_url()
    {
	return http_update_url;
    }

    public void setHttp_update_url(String http_update_url)
    {
	this.http_update_url = http_update_url;
    }

    public UserBean getUser()
    {
	return userBean;
    }

    public void setUser(UserBean user)
    {
	this.userBean = user;
    }

    public MyApplication()
    {
	super();
    }

    public static MyApplication getInstance()
    {
	if (null == instance)
	{
	    instance = new MyApplication();
	    instance.setHttp_url("http://crm.jinguc.com/api/app.php");
	    instance.setHttp_update_url("http://crm.jinguc.com/app/update.xml");

	    // instance.setHttp_url("http://219.156.138.98:8001/api/app.php");
	    // instance.setHttp_update_url("http://219.156.138.98:8001/app/update.xml");
	    Log.i("JINGU", "Now in getInstance!!");
	}
	return instance;
    }

    public void addActivity(Activity activity)
    {
	activityList.add(activity);
    }

    public void exit()
    {
	for (Activity activity : activityList)
	{
	    Log.i("JinGu", "activity out" + activity.getPackageName());
	    activity.finish();
	}
    }
}
