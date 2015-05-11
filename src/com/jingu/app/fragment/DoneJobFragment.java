package com.jingu.app.fragment;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jingu.app.R;
import com.jingu.app.bean.JobBean;
import com.jingu.app.main.activity.DoneJobActivity;
import com.jingu.app.util.MyApplication;
import com.jingu.app.util.MyListViewUtil;

public class DoneJobFragment extends Fragment
{
    public static String TAG = "JinGu";
    private MyListViewUtil listViewUtil = null;
    ListView jobList = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	MyApplication.getInstance().addActivity(this.getActivity());
	super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
	Log.i(TAG, "Now in DoneJobFragment_ViewCreate");
	View DoneJobView = inflater.inflate(R.layout.main_tab_donejob, container, false);
	// 获取viewList
	jobList = (ListView) DoneJobView.findViewById(R.id.done_job_list);
	// 设置Adapter
	listViewUtil = new MyListViewUtil(getActivity());
	jobList.setAdapter(listViewUtil.getAdapterForDoneJob());
	// 点击item事件，显示工单详情
	jobList.setOnItemClickListener(new MyItemClickListener());
	return DoneJobView;
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
}
