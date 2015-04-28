package com.jingu.app.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore.Audio;
import android.util.Log;

import com.jingu.app.R;
import com.jingu.app.fragment.NewJobFragment;
import com.jingu.app.main.activity.MainActivityFrag;
import com.jingu.app.util.BaseConst;
import com.jingu.app.util.CustomerHttpClient;
import com.jingu.app.util.MyGpsServiceListener;

public class BackGroundService extends Service
{

    private String TAG = "JIN_GU";
    // 线程句柄
    private MessageThread messageThread = null;
    // 点击查看
    private Intent messageIntent = null;
    private PendingIntent messagePendingIntent = null;
    // 通知栏消息
    private int messageNotificationID = 1000;
    private Notification messageNotification = null; // 是具体的状态栏通知对象，可以设置icon、文字、提示声音、振动等等参数。
    private NotificationManager messageNotificatioManager = null; // 是状态栏通知的管理类，负责发通知、清楚通知等。
    public String msgTitile = "";
    public String msgString = "";
    // 位置服务
    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    public Long sleepTime;// 系统扫描时间

    @Override
    public IBinder onBind(Intent intent)
    {
	return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
	// 初始化
	msgTitile = getResources().getString(R.string.title_msg);
	msgString = getResources().getString(R.string.title_new_job);

	int icon = R.drawable.new_wait_job;
	CharSequence tickerText = msgTitile;
	long when = System.currentTimeMillis();
	messageNotification = new Notification(icon, tickerText, when);
	// 指定个性化视图,设置提示音、震动
	messageNotification.contentView = null;
	// --
	messageNotification.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "2");// 系统内部的提示音
	messageNotification.defaults |= Notification.DEFAULT_VIBRATE; // 震动
	messageNotification.defaults |= Notification.DEFAULT_LIGHTS; // 默认灯光提示
	messageNotification.flags |= Notification.FLAG_INSISTENT; // 一直震动响铃，直到用户响应
	// --
	//--> messageNotification.defaults = Notification.DEFAULT_ALL;
	messageNotification.flags |= Notification.FLAG_AUTO_CANCEL;// 点击消息后,该消息自动退出

	messageNotificatioManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	if (messageIntent == null)
	{
	    messageIntent = new Intent(this, MainActivityFrag.class);
	}
	messagePendingIntent = PendingIntent.getActivity(this, 0, messageIntent, 0);
	// 读取系统扫描参数
	String setScan = BaseConst.getParams(this, BaseConst.SCAN_TIMES);
	sleepTime = Long.valueOf(setScan) * 1000;
	Log.i(TAG, setScan);
	// 开启线程
	if (messageThread == null)
	{
	    messageThread = new MessageThread();// 该线程每10秒,发布一条消息出来
	    messageThread.isRunning = true;// 设置为false后,线程跳出循环并结束对
	    messageThread.isNet = true;
	    messageThread.start();
	}
	// 开启位置服务监听
	if (locationManager == null)
	{
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    locationListener = new MyGpsServiceListener(BackGroundService.this);
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, BaseConst.minTime,
		    BaseConst.minDistance, locationListener);
	}
	Log.i(TAG, "Back Ground Service is Stard!!");
	return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 从服务器端获取消息
     */
    class MessageThread extends Thread
    {
	// 设置为false后,线程跳出循环并结束
	public boolean isRunning = true;
	public boolean isNet = true;

	@SuppressWarnings("deprecation")
	public void run()
	{
	    while (isRunning)
	    {
		try
		{
		    // 先判断网路
		    if (!HttpClientService.isConnect(BackGroundService.this))
		    {
			if (isNet)
			{
			    isNet = false;
			    Message msg = new Message();
			    msg.what = 1;
			    NewJobFragment.mHandler.sendMessage(msg);
			}
			Thread.sleep(5000);
			continue;
		    }
		    else
		    {
			if (!isNet)
			{
			    isNet = true;
			    Message msg1 = new Message();
			    msg1.what = 2;
			    NewJobFragment.mHandler.sendMessage(msg1);
			}
		    }
		    // 向服务器发送请求获取新的工单的请求，同时返回工单的个数
		    int msgNum = HttpClientService.getRepostOfNewJob(BackGroundService.this);
		    int isBack = getIsBackGround();
		    if (isBack == 0)
		    {
			// 如果是前台界面并且存在通知栏信息，那么此时清除通知栏信息
			if (messageNotificationID > 1000)
			{
			    messageNotificatioManager.cancel(messageNotificationID - 1);
			    messageNotificationID = 1000;
			}
			if (msgNum > 0)
			{
			    NewJobFragment.mHandler.sendEmptyMessage(0);
			}
		    }
		    // 如果有新消息，并且当前应用是在后台运行，那么在通知栏显示消息
		    if (msgNum > 0 && isBack == 1)
		    {
			int nums = getMsgNum();
			nums += msgNum;
			// 更新通知栏
			String textMsg = Integer.toString(nums) + msgString;
			messageNotification.tickerText = textMsg;
			messageNotification.setLatestEventInfo(BackGroundService.this, textMsg, msgTitile,
				messagePendingIntent);
			if (messageNotificationID > 0)
			{
			    messageNotificatioManager.cancel(messageNotificationID - 1);
			}
			messageNotificatioManager.notify(messageNotificationID, messageNotification);
			messageNotificationID++;
			// 更新消息个数
			saveMsgNum(nums);
			Log.i(TAG, textMsg);
		    }
		    // 休息10秒钟
		    Thread.sleep(sleepTime);
		}
		catch (InterruptedException e)
		{
		    Log.i(TAG, e.getMessage());
		}
	    }
	    Log.i(TAG, "Thread is out!");
	}
    }

    @SuppressWarnings("static-access")
    @Override
    public void onDestroy()
    {
	// 清除提示栏
	if (messageNotificationID > 0)
	{
	    messageNotificatioManager.cancel(messageNotificationID - 1);
	}
	// 停止线程
	messageThread.isRunning = false;
	messageIntent = null;
	// 停止位置服务
	if (locationManager != null && locationListener != null)
	{
	    locationManager.removeUpdates(locationListener);
	}
	// 关闭连接池
	if (CustomerHttpClient.mHttpClient != null)
	{
	    CustomerHttpClient.mHttpClient.getConnectionManager().shutdown();
	    CustomerHttpClient.mHttpClient = null;
	}
	MainActivityFrag.instance.serviceIntent = null;
	super.onDestroy();
	Log.i(TAG, "destroy");
    }

    /**
     * 1：后台运行 0：前台运行
     * 
     * @return
     */
    public int getIsBackGround()
    {
	SharedPreferences settings = getSharedPreferences("setting", 0);
	return settings.getInt("isBack", 0);
    }

    /**
     * 返回当前消息数
     * 
     * @return
     */
    public int getMsgNum()
    {
	SharedPreferences settings = getSharedPreferences("setting", 0);
	return settings.getInt("nums", 0);
    }

    /**
     * 每次更新消息数目
     * 
     * @param nums
     */
    public void saveMsgNum(int nums)
    {
	SharedPreferences settings = getSharedPreferences("setting", 0);
	SharedPreferences.Editor editor = settings.edit();
	editor.putInt("nums", nums);
	editor.commit();
    }
}
