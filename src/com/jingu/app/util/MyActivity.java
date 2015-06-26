package com.jingu.app.util;

import android.app.Activity;

public class MyActivity extends Activity
{

    @Override
    protected void onPause()
    {
	// 取消注册home键
	BaseConst.unregisterHomeKeyReceiver(this);
	super.onPause();
    }

    @Override
    protected void onResume()
    {
	// 注册监听home键
	BaseConst.registerHomeKeyReceiver(this);
	super.onResume();
    }

    @Override
    protected void onStop()
    {
	super.onStop();
    }

    @Override
    protected void onDestroy()
    {
	super.onDestroy();
    }
}
