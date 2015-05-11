package com.jingu.app.util;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

public class MyApplication extends Application
{
    private List<Activity> activityList = new LinkedList<Activity>();
    private static MyApplication instance;

    private String userName;
    private String passWord;

    @Override
    public void onCreate()
    {
	super.onCreate();
    }

    public String getUserName()
    {
	return userName;
    }

    public void setUserName(String userName)
    {
	this.userName = userName;
    }

    public String getPassWord()
    {
	return passWord;
    }

    public void setPassWord(String passWord)
    {
	this.passWord = passWord;
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
