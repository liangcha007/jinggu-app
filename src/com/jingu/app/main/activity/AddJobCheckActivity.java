package com.jingu.app.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jingu.app.R;
import com.jingu.app.util.MyActivity;

public class AddJobCheckActivity extends MyActivity
{
    public static AddJobCheckActivity acActivity = null;
    EditText etel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.add_job_check);
	acActivity = this;
	etel = (EditText) findViewById(R.id.tel_num);
	etel.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    /**
     * 返回按钮
     * 
     * @param v
     */
    public void backHandler(View v)
    {
	this.finish();
	TopMenuActivity.tActivity.finish();
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
		Toast.makeText(AddJobCheckActivity.this, R.string.login_net_fial, Toast.LENGTH_SHORT).show();
		break;
	    case 1:
		// 请求失败
		Toast.makeText(AddJobCheckActivity.this, R.string.confirm_fail, Toast.LENGTH_SHORT).show();
		break;
	    case 2:
		// 正常返回
		break;
	    default:
		break;
	    }
	}
    }

    @Override
    protected void onDestroy()
    {
	acActivity = null;
	super.onDestroy();
    }

    /**
     * 提交新增工单按钮
     * 
     * @param v
     */
    public void postAddJob(View v)
    {

	String telNum = etel.getText().toString();
	if ("".equals(telNum) || telNum == null)
	{
	    Toast.makeText(this, "请输入号码!", Toast.LENGTH_SHORT).show();
	    return;
	}
	// 跳转到新增页面
	Intent intent = new Intent(AddJobCheckActivity.this, WaitingActivity.class);
	intent.putExtra("str", "addJob");
	intent.putExtra("telNum", telNum);
	startActivityForResult(intent, 0);
    }
}
