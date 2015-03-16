package com.jingu.app.main.activity;

import android.os.Bundle;
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
	if (MainActivityFrag.serviceIntent != null)
	{
	    stopService(MainActivityFrag.serviceIntent);
	}
	BaseConst.updateExit(this, 1);
	MyApplication.getInstance().exit();

	// android.os.Process.killProcess(android.os.Process.myPid()); // 获取PID
	// System.exit(0);
    }
}
