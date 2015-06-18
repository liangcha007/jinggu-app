package com.jingu.app.main.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.jingu.app.R;
import com.jingu.app.dao.DBJobInfoDao;
import com.jingu.app.util.BaseConst;
import com.jingu.app.util.MyActivity;
import com.jingu.app.util.MyApplication;

public class SysSettingActivity extends MyActivity
{
    Spinner uSpinner;
    EditText set_scan;
    EditText set_jobNum;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.system_setting);
	uSpinner = (Spinner) findViewById(R.id.set_update);
	String[] str = { "是", "否" };
	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, str);
	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	uSpinner.setSelection(0, true);
	uSpinner.setAdapter(adapter);
	MyApplication.getInstance().addActivity(this);
	set_jobNum = (EditText) findViewById(R.id.set_job_num);
	set_scan = (EditText) findViewById(R.id.set_scan_time);

	// 初始化，并设置
	String jobNum = BaseConst.getParams(this, "job_nums");
	if ("".equals(jobNum) || jobNum == "" || jobNum == null)
	{
	    // 如果不存在默认参数，设置一下
	    BaseConst.setParams(this, "job_nums", set_jobNum.getText().toString());
	}
	else
	{
	    set_jobNum.setText(jobNum);
	}
	// 初始化扫描参数
	String setScan = BaseConst.getParams(this, "scan_times");
	if ("".equals(setScan) || setScan == "" || setScan == null)
	{
	    BaseConst.setParams(this, "scan_times", set_scan.getText().toString());
	}
	else
	{
	    set_scan.setText(setScan);
	}
	// 初始化是否自动更新参数
	String setUpdate = BaseConst.getParams(this, "update_param");
	if ("".equals(setScan) || setScan == "" || setScan == null)
	{
	    BaseConst.setParams(this, "update_param", String.valueOf(uSpinner.getSelectedItemId()));
	}
	else
	{
	    if ("0".equals(setUpdate))
	    {
		uSpinner.setSelection(0, true);
	    }
	    else
	    {
		uSpinner.setSelection(1, true);
	    }
	}
    }

    /**
     * 返回按钮
     */
    public void backHandler(View v)
    {
	SysSettingActivity.this.finish();
    }

    public void confirmHandler(View v)
    {
	// 校验
	if (set_jobNum.getText() == null || set_scan.getText() == null)
	{
	    Toast.makeText(this, getResources().getString(R.string.sys_set_msg), Toast.LENGTH_LONG).show();
	    return;
	}
	// 设置工单保存个数
	BaseConst.setParams(this, "job_nums", set_jobNum.getText().toString());
	int jNums = Integer.parseInt(set_jobNum.getText().toString());
	DBJobInfoDao djDao = new DBJobInfoDao(this);
	// 直接清除掉那些多余的数据，按照时间先后清除
	djDao.delRecord(jNums);
	djDao.closeDB();
	// 设置是否自动更新
	BaseConst.setParams(this, "update_param", String.valueOf(uSpinner.getSelectedItemId()));
	// 系统刷新时间
	String setScan = BaseConst.getParams(this, "scan_times");
	if (setScan != null && !setScan.equals(set_scan.getText().toString()))
	{
	    BaseConst.setParams(this, "scan_times", set_scan.getText().toString());
	    Toast.makeText(this, getResources().getString(R.string.sys_set_rload), Toast.LENGTH_LONG).show();
	}
	else
	{
	    Toast.makeText(this, getResources().getString(R.string.sys_set_mark), Toast.LENGTH_SHORT).show();
	}
	SysSettingActivity.this.finish();
    }
}
