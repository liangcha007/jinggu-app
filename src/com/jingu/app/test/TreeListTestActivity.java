package com.jingu.app.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jingu.app.R;

public class TreeListTestActivity extends Activity
{
    private Spinner firstSpinner = null; // 一级菜单
    private Spinner secondSpinner = null; // 二级菜单
    ArrayAdapter<String> provinceAdapter = null; // 省级适配器
    ArrayAdapter<String> cityAdapter = null; // 地级适配器
    static int provincePosition = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	setContentView(R.layout.add_new_job_test);
	super.onCreate(savedInstanceState);
	setSpinner();
    }

    // 省级选项值
    private String[] province = new String[] { "租瓶", "售瓶", "换瓶", "换气", "维修", "回收" };
    // 地级选项值
    private String[][] city = new String[][] { { "出租" }, { "出售" }, { "小换大", "2换1", "以旧换新", "折旧换新" }, { "换燃气" },
	    { "维护修理" }, { "回收钢瓶" } };

    /*
     * 设置下拉框
     */
    private void setSpinner()
    {
	firstSpinner = (Spinner) findViewById(R.id.first);
	secondSpinner = (Spinner) findViewById(R.id.second);

	// 绑定适配器和值
	provinceAdapter = new ArrayAdapter<String>(TreeListTestActivity.this, android.R.layout.simple_spinner_item,
		province);
	provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	firstSpinner.setAdapter(provinceAdapter);
	firstSpinner.setSelection(0, true); // 设置默认选中项，此处为默认选中第4个值

	cityAdapter = new ArrayAdapter<String>(TreeListTestActivity.this, android.R.layout.simple_spinner_item, city[province.length-1]);
	cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	secondSpinner.setAdapter(cityAdapter);
	secondSpinner.setSelection(0, true); // 默认选中第0个

	// 省级下拉框监听
	firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
	{
	    // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
	    @Override
	    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
	    {
		// position为当前省级选中的值的序号

		// 将地级适配器的值改变为city[position]中的值
		cityAdapter = new ArrayAdapter<String>(TreeListTestActivity.this, android.R.layout.simple_spinner_item,
			city[position]);
		// 设置二级下拉列表的选项内容适配器
		secondSpinner.setAdapter(cityAdapter);
		provincePosition = position; // 记录当前省级序号，留给下面修改县级适配器时用
	    }

	    @Override
	    public void onNothingSelected(AdapterView<?> arg0)
	    {

	    }

	});

    }
}
