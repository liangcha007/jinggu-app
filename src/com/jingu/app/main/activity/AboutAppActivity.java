package com.jingu.app.main.activity;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jingu.app.R;
import com.jingu.app.util.BaseConst;
import com.jingu.app.util.MyActivity;

public class AboutAppActivity extends MyActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.about_app);
	TextView AppVersion = (TextView) findViewById(R.id.app_version);
	TextView VersionMsg = (TextView) findViewById(R.id.version_content);
	RelativeLayout lbody = (RelativeLayout) findViewById(R.id.rl_body);
	try
	{
	    AppVersion.setText("程序版本:" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
	    String tmp_str = BaseConst.getParams(this, "update_content");
	    Log.i("JinGu", tmp_str);
	    if (!"50".equals(tmp_str))
	    {
		VersionMsg.setText(tmp_str);
		lbody.setVisibility(RelativeLayout.VISIBLE);
	    }
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
