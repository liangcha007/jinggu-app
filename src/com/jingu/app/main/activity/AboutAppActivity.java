package com.jingu.app.main.activity;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jingu.app.R;
import com.jingu.app.util.MyActivity;

public class AboutAppActivity extends MyActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.about_app);
	TextView AppVersion = (TextView) findViewById(R.id.app_version);
	try
	{
	    AppVersion.setText("程序版本:" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
	}
	catch (NameNotFoundException e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * 返回按钮
     */
    public void backHandler(View v)
    {
	AboutAppActivity.this.setResult(0);
	AboutAppActivity.this.finish();
    }
}
