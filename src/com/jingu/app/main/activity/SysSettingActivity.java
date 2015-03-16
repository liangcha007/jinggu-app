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

	set_jobNum = (EditText) findViewById(R.id.set_job_num);
	set_scan = (EditText) findViewById(R.id.set_scan_time);

	// 初始化，并设置
	String jobNum = BaseConst.getParams(this, BaseConst.JOB_NUMS);
	if ("".equals(jobNum) || jobNum == "" || jobNum == null)
	{
	    // 如果不存在默认参数，设置一下
	    BaseConst.setParams(this, BaseConst.JOB_NUMS, set_jobNum.getText().toString());
	}
	else
	{
	    set_jobNum.setText(jobNum);
	}
	String setScan = BaseConst.getParams(this, BaseConst.SCAN_TIMES);
	if ("".equals(setScan) || setScan == "" || setScan == null)
	{
	    BaseConst.setParams(this, BaseConst.SCAN_TIMES, set_scan.getText().toString());
	}
	else
	{
	    set_scan.setText(setScan);
	}

	String setUpdate = BaseConst.getParams(this, BaseConst.UPDATE_PARAM);
	if ("".equals(setScan) || setScan == "" || setScan == null)
	{
	    BaseConst.setParams(this, BaseConst.UPDATE_PARAM, String.valueOf(uSpinner.getSelectedItemId()));
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
	set_scan.setEnabled(false);
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
	// 设置工单保存个数
	BaseConst.setParams(this, BaseConst.JOB_NUMS, set_jobNum.getText().toString());
	int jNums = Integer.parseInt(set_jobNum.getText().toString());
	DBJobInfoDao djDao = new DBJobInfoDao(this);
	// 直接清除掉那些多余的数据，按照时间先后清除
	djDao.delRecord(jNums);
	djDao.closeDB();
	// 系统刷新时间(暂时不用)
	BaseConst.setParams(this, BaseConst.SCAN_TIMES, set_scan.getText().toString());
	// 设置是否自动更新(暂时没实现)
	BaseConst.setParams(this, BaseConst.UPDATE_PARAM, String.valueOf(uSpinner.getSelectedItemId()));
	Toast.makeText(this, getResources().getString(R.string.sys_set_mark), Toast.LENGTH_SHORT).show();
	SysSettingActivity.this.finish();
    }
}
