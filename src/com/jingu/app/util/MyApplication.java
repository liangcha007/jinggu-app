package com.jingu.app.util;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.jingu.app.bean.UserBean;
import com.jingu.app.login.activity.LoginActivity;
import com.jingu.app.main.activity.MainActivityFrag;

public class MyApplication extends Application
{
    private List<Activity> activityList = new LinkedList<Activity>();
    private static MyApplication instance;

    private UserBean userBean;// 用户信息
    private String http_update_url;// 自动更新地址
    private String http_url; // http访问地址
    private boolean isBack; // 是否后台运行
    private boolean isExit; // 是否完全退出
    private int msgNums;// 状态栏提示时消息数目

    @Override
    public void onCreate()
    {
	Log.i("JINGU", "Now in the OnCreate of Application!!");
	userBean = new UserBean();
	// 程序崩溃时触发线程 以下用来捕获程序崩溃异常
	Thread.setDefaultUncaughtExceptionHandler(restartHandler);
	super.onCreate();
    }

    // 创建服务用于捕获崩溃异常
    private UncaughtExceptionHandler restartHandler = new UncaughtExceptionHandler()
    {
	public void uncaughtException(Thread thread, Throwable ex)
	{
	    StringBuffer str = new StringBuffer();
	    str.append("发生异常情况，APP崩溃掉了!异常信息如下：\n");
	    str.append(ex.getMessage());
	    FillUtil.writeLogToFile(str.toString());
	    // restartApp();// 发生崩溃异常时,重启应用
	}
    };

    public void restartApp()
    {
	Intent intent = new Intent(instance, LoginActivity.class);
	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	instance.startActivity(intent);
	// 结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
	android.os.Process.killProcess(android.os.Process.myPid());
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

    public boolean isBack()
    {
	return isBack;
    }

    public void setBack(boolean isBack)
    {
	this.isBack = isBack;
    }

    public boolean isExit()
    {
	return isExit;
    }

    public void setExit(boolean isExit)
    {
	this.isExit = isExit;
    }

    public int getMsgNums()
    {
	return msgNums;
    }

    public void setMsgNums(int msgNums)
    {
	this.msgNums = msgNums;
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

	    instance.setBack(true);
	    instance.setExit(false);
	    instance.setMsgNums(0);
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
	MainActivityFrag.instance.finish();
    }
}
