package com.jingu.app.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * @author zhouc52 广播，监听home按键，动态注册
 */
public class MyHomeWatcherReceiver extends BroadcastReceiver
{
    private static final String LOG_TAG = "JIN_GU";
    private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";
    private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";

    @Override
    public void onReceive(Context context, Intent intent)
    {
	String action = intent.getAction();
	Log.i(LOG_TAG, "onReceive: action: " + action);
	if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
	{
	    // android.intent.action.CLOSE_SYSTEM_DIALOGS
	    String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
	    Log.i(LOG_TAG, "reason: " + reason);

	    if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason))
	    {
		updateIsBack(context);
		// 短按Home键
		Log.i(LOG_TAG, "homekey");
	    }
	    else if (SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason))
	    {
		updateIsBack(context);
		// 长按Home键 或者 activity切换键
		Log.i(LOG_TAG, "long press home key or activity switch");
	    }
	    else if (SYSTEM_DIALOG_REASON_LOCK.equals(reason))
	    {
		updateIsBack(context);
		// 锁屏
		Log.i(LOG_TAG, "lock");
	    }
	    else if (SYSTEM_DIALOG_REASON_ASSIST.equals(reason))
	    {
		// samsung 长按Home键
		Log.i(LOG_TAG, "assist");
	    }
	}
    }
    public void updateIsBack(Context context)
    {
	SharedPreferences settings = context.getSharedPreferences("setting", 0);
	SharedPreferences.Editor editor = settings.edit();
	editor.putInt("isBack", 1);
	editor.putInt("nums", 0);
	editor.commit(); 
    }
}
