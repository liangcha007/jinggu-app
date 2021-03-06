package com.jingu.app.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore.Audio;
import android.util.Log;

import com.jingu.app.R;
import com.jingu.app.fragment.NewJobFragment;
import com.jingu.app.main.activity.MainActivityFrag;
import com.jingu.app.util.BaseConst;
import com.jingu.app.util.CustomerHttpClient;
import com.jingu.app.util.FillUtil;
import com.jingu.app.util.MyApplication;
import com.jingu.app.util.MyGpsServiceListener;
import com.jingu.app.util.StaticRecevier;

public class BackGroundService extends Service
{

    private String TAG = "JIN_GU";
    // 线程句柄
    public static MessageThread messageThread = null;
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

    // 电源锁
    private WakeLock wakeLock;
    // 广播
    public static StaticRecevier searchReceiver = null;

    @Override
    public IBinder onBind(Intent intent)
    {
	return null;
    }

    @Override
    public void onCreate()
    {
	Log.i(TAG,"in service create fun!");
	// 设置service为前台进程
	Notification notification = new Notification();
	notification.flags = Notification.FLAG_ONGOING_EVENT;
	notification.flags |= Notification.FLAG_NO_CLEAR;
	notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
	this.startForeground(0, notification);
	// 获取电源锁
	wakeLock = null;
	acquireWakeLock();
	super.onCreate();
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
	// --> messageNotification.defaults = Notification.DEFAULT_ALL;
	messageNotification.flags |= Notification.FLAG_AUTO_CANCEL;// 点击消息后,该消息自动退出

	messageNotificatioManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	if (messageIntent == null)
	{
	    messageIntent = new Intent(this, MainActivityFrag.class);
	}
	messagePendingIntent = PendingIntent.getActivity(this, 0, messageIntent, 0);
	// 读取系统扫描参数
	String setScan = BaseConst.getParams(this, "scan_times");
	sleepTime = Long.valueOf(setScan) * 1000;
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
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
	}
	Log.i(TAG, "Back Ground Service is Stard!!");

	flags = START_STICKY;
	return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
	// IntentFilter localIntentFilter = new
	// IntentFilter("android.intent.action.USER_PRESENT");
	// localIntentFilter.setPriority(Integer.MAX_VALUE);// 整形最大值
	// searchReceiver = new StaticRecevier();
	// registerReceiver(searchReceiver, localIntentFilter);
	// super.onStart(intent, startId);
    }

    /**
     * 从服务器端获取消息
     */
    public class MessageThread extends Thread
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
		    boolean isBack = MyApplication.getInstance().isBack();
		    // 先判断网路
		    if (!HttpClientService.isConnect(BackGroundService.this))
		    {
			if (isNet && !isBack)
			{
			    isNet = false;
			    Message msg = new Message();
			    msg.what = 1;//显示网路状况不佳
			    NewJobFragment.mHandler.sendMessage(msg);
			}
			Thread.sleep(5000);
			continue;
		    }
		    else
		    {
			if (!isNet && !isBack)
			{
			    isNet = true;
			    Message msg1 = new Message();
			    msg1.what = 2;
			    NewJobFragment.mHandler.sendMessage(msg1);
			}
		    }
		    // 向服务器发送请求获取新的工单的请求，同时返回工单的个数
		    int msgNum = HttpClientService.getRepostOfNewJob(BackGroundService.this);
		    if (!isBack)
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
		    if (msgNum > 0 && isBack)
		    {
			Log.i("JINGU", "Get new Message in back,next is show in title!");
			int nums = MyApplication.getInstance().getMsgNums();
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
			MyApplication.getInstance().setMsgNums(nums);
			Log.i(TAG, textMsg);
		    }
		    // 休息10秒钟
		    Thread.sleep(sleepTime);
		}
		catch (InterruptedException e)
		{
		    Log.i(TAG, "线程被打断了!");
		}
	    }
	    Log.i(TAG, "Thread is out!");
	    messageThread = null;
	}
    }

    @SuppressWarnings("static-access")
    @Override
    public void onDestroy()
    {
	if (MyApplication.getInstance().isExit())
	{
	    Log.i("JINGU", "Now in onDestory Service func");
	    // 清楚广播
	    // unregisterReceiver(searchReceiver);
	    // 清除提示栏
	    if (messageNotificationID > 0)
	    {
		messageNotificatioManager.cancel(messageNotificationID - 1);
	    }
	    // 停止线程
	    messageThread.isRunning = false;
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
	    // 取消service的前台线程
	    this.stopForeground(true);
	    // 释放电源锁
	    releaseWakeLock();
	    MainActivityFrag.instance.serviceIntent = null;
	    // 置空线程
	    messageThread.interrupt();// 打断当前线程
	    messageIntent = null;
	    super.onDestroy();
	    Log.i(TAG, "destroy");
	}
	else
	{
	    Log.i("JINGU", "Now in onDestory Service func,but it restart now !");
	    // 更新isBack
	    MyApplication.getInstance().setBack(true);
	    // 如果并没有通过正常的退出方式来到这里，那么重启service
	    Intent localIntent = new Intent();
	    localIntent.setClass(this, BackGroundService.class);
	    this.startService(localIntent);
	    // 记录日志
	    StringBuffer sql = new StringBuffer();
	    sql.append("Restart Service beacuse it's destroy in normal way !!");
	    FillUtil.writeLogToFile(sql.toString());
	}
    }

    // 获取电源锁
    private void acquireWakeLock()
    {
	if (null == wakeLock)
	{
	    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	    wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, getClass()
		    .getCanonicalName());
	    if (null != wakeLock)
	    {
		Log.i(TAG, "Service call acquireWakeLock !");
		wakeLock.acquire();
	    }
	}
    }

    // 释放设备电源锁
    private void releaseWakeLock()
    {
	if (null != wakeLock && wakeLock.isHeld())
	{
	    Log.i(TAG, "Service call releaseWakeLock !");
	    wakeLock.release();
	    wakeLock = null;
	}
    }
}
