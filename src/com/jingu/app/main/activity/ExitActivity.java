package com.jingu.app.main.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.jingu.app.R;
import com.jingu.app.util.BaseConst;
import com.jingu.app.util.MyActivity;
import com.jingu.app.util.MyApplication;

public class ExitActivity extends MyActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.exit_dialog);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
	finish();
	return true;
    }

    public void exitNo(View v)
    {
	this.finish();
    }

    /**
     * 彻底退出应用，不再收到消息
     * 
     * @param v
     */
    public void exitYes(View v)
    {
	this.finish();
	// 停服务
	if (MainActivityFrag.serviceIntent != null)
	{
	    Log.i("JINGU", "stop service now !!");
	    stopService(MainActivityFrag.serviceIntent);
	}
	// 改标志
	BaseConst.updateExit(this, 1);
	// 退activity和进程
	MyApplication.getInstance().exit();
	// 退出
//	ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//	manager.killBackgroundProcesses(getPackageName());
//	System.exit(0);
    }
}
