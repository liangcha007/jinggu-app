package com.jingu.app.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.jingu.app.R;
import com.jingu.app.bean.JobBean;
import com.jingu.app.main.activity.DoneJobActivity;
import com.jingu.app.util.BaseConst;
import com.jingu.app.util.DateTimePickDialogUtil;
import com.jingu.app.util.FillUtil;
import com.jingu.app.util.MyApplication;
import com.jingu.app.util.MyListViewUtil;

public class ScanFragment extends Fragment
{
    public static View sView = null;
    private String initDate;
    private EditText beginDate;
    private EditText endDate;
    public ListView jobList = null;
    public static List<JobBean> listJobs = null;
    public Spinner sp;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	MyApplication.getInstance().addActivity(this.getActivity());
	super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
	Log.i("JINGU", "Now in ScanFragment CreateView!");
	View scanView = inflater.inflate(R.layout.main_tab_scan, container, false);
	sView = scanView;
	listJobs = new ArrayList<JobBean>();
	initDate = BaseConst.getDate(new Date(), 3);
	// 获取viewList
	jobList = (ListView) scanView.findViewById(R.id.check_job_list);
	// 设置Adapter
	MyListViewUtil listViewUtil = new MyListViewUtil(getActivity());
	jobList.setAdapter(listViewUtil.getAdapterForAllJob());
	// 点击item事件，显示工单详情
	jobList.setOnItemClickListener(new MyItemClickListener());
	// 设置Spinner
	sp = (Spinner) scanView.findViewById(R.id.job_typestr);
	String[] spStrs = { "全部", "未收到", "未阅读", "办理中" ,"完成","取消","直办"};
	ArrayAdapter<String> spAaAdapter = new ArrayAdapter<String>(getActivity(),
		android.R.layout.simple_spinner_item, spStrs);
	spAaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	sp.setAdapter(spAaAdapter);
	//sp.setSelection(0, true);
	// --以下设置时间选择器
	beginDate = (EditText) scanView.findViewById(R.id.begin_date);
	endDate = (EditText) scanView.findViewById(R.id.end_date);

	beginDate.setText(initDate);
	endDate.setText(initDate);
	beginDate.setInputType(InputType.TYPE_NULL);
	endDate.setInputType(InputType.TYPE_NULL);
	hideIM(beginDate);
	hideIM(endDate);

	beginDate.setOnFocusChangeListener(new OnFocusChangeListener()
	{

	    @Override
	    public void onFocusChange(View v, boolean hasFocus)
	    {
		if (beginDate.isFocused())
		{
		    DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(getActivity(), initDate);
		    dateTimePicKDialog.dateTimePicKDialog(beginDate);
		    beginDate.clearFocus();
		}
	    }
	});
	endDate.setOnFocusChangeListener(new OnFocusChangeListener()
	{

	    @Override
	    public void onFocusChange(View v, boolean hasFocus)
	    {
		if (endDate.isFocused())
		{
		    DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(getActivity(), initDate);
		    dateTimePicKDialog.dateTimePicKDialog(endDate);
		    endDate.clearFocus();
		}
	    }
	});
	return scanView;
    }

    public class MyItemClickListener implements OnItemClickListener
    {
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
	    @SuppressWarnings("unchecked")
	    HashMap<String, Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);
	    Object object = item.get("content");
	    if (object == null || "".equals(object))
	    {
		// 如果是工单，而非提示条目时候，获取jobBean跳转到工单详情页面
		JobBean JobItem = MyListViewUtil.getDoneJob(item);
		Bundle data = new Bundle();
		data.putSerializable("job", JobItem);
		Intent intent = new Intent(getActivity(), DoneJobActivity.class);
		intent.putExtras(data);
		startActivityForResult(intent, 0);
	    }
	}
    }

    // 隐藏手机键盘
    private void hideIM(View edt)
    {
	try
	{
	    InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
	    IBinder windowToken = edt.getWindowToken();
	    if (windowToken != null)
	    {
		im.hideSoftInputFromWindow(windowToken, 0);
	    }
	}
	catch (Exception e)
	{
	    FillUtil.writeLogToFile(e.getMessage());
	}
    }
}
