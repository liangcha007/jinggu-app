package com.jingu.app.util;

import android.app.Activity;
import android.content.SharedPreferences;

public class MyActivity extends Activity
{

    @Override
    protected void onPause()
    {
	// 取消注册home键
	BaseConst.unregisterHomeKeyReceiver(this);
	updateIsBack(1);
	super.onPause();
    }

    @Override
    protected void onResume()
    {
	// 注册监听home键
	BaseConst.registerHomeKeyReceiver(this);
	updateIsBack(0);
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

    public void updateIsBack(int i)
    {
	SharedPreferences settings = getSharedPreferences("setting", 0);
	SharedPreferences.Editor editor = settings.edit();
	editor.putInt("isBack", i);
	editor.putInt("nums", 0);
	editor.commit();
    }
}
