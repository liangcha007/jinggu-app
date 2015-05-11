package com.jingu.app.fragment;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.jingu.app.R;
import com.jingu.app.bean.JobBean;
import com.jingu.app.main.activity.DoneJobActivity;
import com.jingu.app.main.activity.WaitingActivity;
import com.jingu.app.service.HttpClientService;
import com.jingu.app.ui.RefreshableView;
import com.jingu.app.ui.RefreshableView.PullToRefreshListener;
import com.jingu.app.util.MyApplication;
import com.jingu.app.util.MyListViewUtil;

public class NewJobFragment extends Fragment
{
    public static String TAG = "JinGu";
    RefreshableView refreshableView = null;
    private static MyListViewUtil listViewUtil = null;
    public static Handler mHandler = null;
    public static ListView jobList = null;
    private static TextView NetWork = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	MyApplication.getInstance().addActivity(this.getActivity());
	if (mHandler == null)
	{
	    mHandler = new Handler()
	    {
		public void handleMessage(Message msg)
		{
		    switch (msg.what)
		    {
		    case 1:
			NetWork.setVisibility(TextView.VISIBLE);
			break;
		    case 2:
			NetWork.setVisibility(TextView.GONE);
			break;
		    default:
			jobList.setAdapter(listViewUtil.getAdapterForNewJob());
			break;
		    }
		};
	    };
	}
	super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
	Log.i(TAG, "Now in NewJobFragment_ViewCreate");
	// 解析xml获取View
	View NewJobView = inflater.inflate(R.layout.main_tab_newjob, container, false);
	// 获取刷新List
	refreshableView = (RefreshableView) NewJobView.findViewById(R.id.refreshable_view);
	// 获取viewList
	jobList = (ListView) NewJobView.findViewById(R.id.new_job_list);
	NetWork = (TextView) NewJobView.findViewById(R.id.net_work_msg);
	// 设置Adapter
	listViewUtil = new MyListViewUtil(getActivity());
	jobList.setAdapter(listViewUtil.getAdapterForNewJob());
	// 点击item事件，显示工单详情
	jobList.setOnItemClickListener(new MyItemClickListener());
	refreshableView.setOnRefreshListener(new MyRefreshListener(), 0);
	return NewJobView;
    }

    // 单击查看对应工单详情
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
		JobBean newJobItem = MyListViewUtil.getJob(item);
		Bundle data = new Bundle();
		data.putSerializable("job", newJobItem);

		// 如果工单是取消类型，直接跳转到工单对应的已完成页面
		if ("Q".equals(newJobItem.getJobType()) || newJobItem.getJobType() == "Q")
		{
		    Intent intent = new Intent(getActivity(), DoneJobActivity.class);
		    intent.putExtras(data);
		    startActivityForResult(intent, 0);
		}
		else
		{
		    Intent intent = new Intent(getActivity(), WaitingActivity.class);
		    intent.putExtra("str", "detail");
		    intent.putExtras(data);
		    startActivityForResult(intent, 0);
		}
	    }
	}
    }

    // 下拉刷新
    public class MyRefreshListener implements PullToRefreshListener
    {
	@Override
	public void onRefresh()
	{
	    try
	    {
		// 更新UI
		mHandler.sendEmptyMessage(0);
		Thread.sleep(2000);
		// 判断网络
		if (HttpClientService.isConnect(getActivity()))
		{
		    Message msg = new Message();
		    msg.what = 2;

		    mHandler.sendMessage(msg);
		}
	    }
	    catch (InterruptedException e)
	    {
		e.printStackTrace();
	    }
	    refreshableView.finishRefreshing();
	}
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	/*
	 * requestCode:0 NewJobActivity
	 */
	if (requestCode == 0)
	{
	    switch (resultCode)
	    {
	    case 2:
		// 正常返回
		break;
	    default:
		break;
	    }
	}
    }
}
