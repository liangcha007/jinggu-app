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
	// 正式退出，修改password为空
	BaseConst.setParams(ExitActivity.this, "password", "");
	// 修改标识
	MyApplication.getInstance().setExit(true);
	// 退activity和进程
	MyApplication.getInstance().exit();
	// 停服务
	stopService(MainActivityFrag.serviceIntent);
	// 退出
	// ActivityManager manager = (ActivityManager)
	// getSystemService(ACTIVITY_SERVICE);
	// manager.killBackgroundProcesses(getPackageName());
	// System.exit(0);
    }
}
