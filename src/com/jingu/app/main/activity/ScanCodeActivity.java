package com.jingu.app.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jingu.app.R;
import com.jingu.app.util.MyActivity;
import com.jingu.app.util.MyApplication;

public class ScanCodeActivity extends MyActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.code_scan);
	Log.i("JINGU", "now in onCreate of ScanCodeActivity!");
	MyApplication.getInstance().addActivity(this);
	// 直接跳转至扫一扫页面
	initStartScan();
    }

    // 进入扫一扫
    public void initStartScan()
    {
	Intent intent = new Intent();
	intent.setClass(ScanCodeActivity.this, MipcaActivityCapture.class);
	startActivityForResult(intent, 1);
    }

    /**
     * 点击重新扫一扫按钮
     * 
     * @param v
     */
    public void againScan(View v)
    {
	initStartScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	if (requestCode == 1)
	{
	    switch (resultCode)
	    {
	    case RESULT_OK:
		Bundle bundle = data.getExtras();
		// 显示扫描到的内容
		Intent intent = new Intent();
		intent.setClass(ScanCodeActivity.this, WaitingActivity.class);
		intent.putExtra("str", "scan");
		intent.putExtras(bundle);
		startActivityForResult(intent, 2);
		break;
	    default:
		this.finish();
		break;
	    }
	}
	else if (requestCode == 2)
	{
	    // 显示钢瓶档案
	    switch (resultCode)
	    {
	    case RESULT_OK:
		Bundle bundle = data.getExtras();
		TextView textView = (TextView) this.findViewById(R.id.s_bottle_content);
		textView.setTextSize(22);
		textView.setText(bundle.getString("result"));
		textView.setBackgroundResource(R.anim.sharp);
		LinearLayout scanBody = (LinearLayout) this.findViewById(R.id.scan_body);
		scanBody.setBackgroundResource(R.drawable.chat_bg_default);
		Button bscan = (Button) this.findViewById(R.id.b_rep_scan);
		bscan.setVisibility(Button.VISIBLE);
		break;
	    default:
		// 提交失败
		Toast.makeText(ScanCodeActivity.this, R.string.scan_fail, Toast.LENGTH_SHORT).show();
		TextView textView2 = (TextView) this.findViewById(R.id.s_bottle_content);
		textView2.setTextSize(22);
		textView2.setText("获取钢瓶信息失败，请稍后重试");
		textView2.setBackgroundResource(R.anim.sharp);
		LinearLayout scanBody2 = (LinearLayout) this.findViewById(R.id.scan_body);
		scanBody2.setBackgroundResource(R.drawable.chat_bg_default);
		Button bscan2 = (Button) this.findViewById(R.id.b_rep_scan);
		bscan2.setVisibility(Button.VISIBLE);
		break;
	    }
	}
    }

    /**
     * 返回
     * 
     * @param v
     */
    public void backHandler(View v)
    {
	this.finish();
    }
}
