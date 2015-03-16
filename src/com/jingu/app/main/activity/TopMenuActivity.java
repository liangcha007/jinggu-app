package com.jingu.app.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.jingu.app.R;
import com.jingu.app.util.MyActivity;

public class TopMenuActivity extends MyActivity
{
    public static TopMenuActivity tActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main_top_dialog);
	tActivity = this;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
	finish();
	return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	if (requestCode == 0)
	{
	    switch (resultCode)
	    {
	    case 0:
		// 网络出问题
		Toast.makeText(TopMenuActivity.this, R.string.login_net_fial, Toast.LENGTH_SHORT).show();
		break;
	    case 1:
		// 请求失败
		Toast.makeText(TopMenuActivity.this, R.string.confirm_fail, Toast.LENGTH_SHORT).show();
		break;
	    case 2:
		// 正常返回
		break;
	    default:
		break;
	    }
	}
    }

    /**
     * 新开户
     * 
     * @param v
     */
    public void addCustom(View v)
    {
	Intent intent = new Intent(TopMenuActivity.this, WaitingActivity.class);
	intent.putExtra("str", "add");
	startActivityForResult(intent, 0);
    }

    
    /**
     * 新增工单--输入客户手机号码查询界面
     * @param v
     */
    public void addNewJob(View v)
    {
	Intent intent = new Intent(TopMenuActivity.this, AddJobCheckActivity.class);
	startActivity(intent);
	this.finish();
    }
}
