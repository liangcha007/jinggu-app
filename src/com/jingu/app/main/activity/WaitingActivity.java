package com.jingu.app.main.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.jingu.app.R;
import com.jingu.app.bean.AddFormBean;
import com.jingu.app.bean.JobBean;
import com.jingu.app.bean.ParamBean;
import com.jingu.app.service.HttpClientService;
import com.jingu.app.util.MyApplication;

public class WaitingActivity extends Activity
{
    private LoginHandler mHandler = null;
    private MyThread mThread = null;
    private Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.waiting);
	MyApplication.getInstance().addActivity(this);
	intent = getIntent();

	if (mHandler == null)
	{
	    mHandler = new LoginHandler();
	}
	if (mThread == null)
	{
	    mThread = new MyThread();
	    mThread.start();
	}
    }

    @Override
    public void onBackPressed()
    {
	Message msg = new Message();
	msg.what = 4;
	mHandler.sendMessage(msg);
    }

    class LoginHandler extends Handler
    {
	@Override
	public void handleMessage(Message msg)
	{
	    switch (msg.what)
	    {
	    case 0:
		// 当前网络不可用
		WaitingActivity.this.setResult(0);
		WaitingActivity.this.finish();
		break;
	    case 1:
		// 跳转至待处理工单详情页面
		JobBean jBean = (JobBean) intent.getSerializableExtra("job");
		Bundle data = new Bundle();
		data.putSerializable("job", jBean);
		Intent intent2 = new Intent(WaitingActivity.this, NewJobActivity.class);
		intent2.putExtras(data);
		startActivity(intent2);
		WaitingActivity.this.finish();
		break;
	    case 2:
		// finish
		NewJobActivity.nJobActivity.finish();
		WaitingActivity.this.finish();
		// 提交回复成功，跳转到主页面
		Intent i = new Intent(WaitingActivity.this, MainActivityFrag.class);
		startActivity(i);
		// 提示提交成功
		Toast.makeText(WaitingActivity.this, R.string.confirm_success, Toast.LENGTH_SHORT).show();
		break;
	    case 3:
		// 验证失败
		WaitingActivity.this.setResult(1);
		WaitingActivity.this.finish();
		break;
	    case 4:
		// 默认点击返回按键
		WaitingActivity.this.setResult(2);
		WaitingActivity.this.finish();
		break;
	    case 5:
		TopMenuActivity.tActivity.finish();
		WaitingActivity.this.finish();
		// 新开户
		Bundle dataAdd = new Bundle();
		AddFormBean afBean = (AddFormBean) msg.obj;
		dataAdd.putSerializable("item", afBean);
		Intent intent3 = new Intent(WaitingActivity.this, AddCustomerActivity.class);
		intent3.putExtras(dataAdd);
		startActivity(intent3);
		break;
	    case 6:
		// 新开户提交成功
		// finish当前activity和AddCustomerActivity
		AddCustomerActivity.cActivity.finish();
		WaitingActivity.this.finish();
		// 提示提交成功
		Toast.makeText(WaitingActivity.this, R.string.confirm_success, Toast.LENGTH_SHORT).show();
		break;
	    case 7:
		// 跳转至新增工单页面
		Bundle jobAdd = new Bundle();
		AddFormBean ajBean = (AddFormBean) msg.obj;
		jobAdd.putSerializable("item", ajBean);
		Intent intent4 = new Intent(WaitingActivity.this, AddJobActivity.class);
		intent4.putExtras(jobAdd);
		startActivity(intent4);
		AddJobCheckActivity.acActivity.finish();
		WaitingActivity.this.finish();
		break;
	    case 8:
		// 新增工单成功
		AddJobActivity.jActivity.finish();
		WaitingActivity.this.finish();
		// 提交回复成功，跳转到主页面
		Intent ii = new Intent(WaitingActivity.this, MainActivityFrag.class);
		startActivity(ii);
		// 提示提交成功
		Toast.makeText(WaitingActivity.this, R.string.confirm_success, Toast.LENGTH_SHORT).show();
		break;
	    case 9:
		// 查询条形码成功，返回条形码信息
		Intent intent5 = new Intent();
		intent5.putExtra("result", msg.obj.toString());
		WaitingActivity.this.setResult(RESULT_OK, intent5);
		WaitingActivity.this.finish();
		break;
	    default:
		break;
	    }
	}
    }

    class MyThread extends Thread
    {
	@Override
	public void run()
	{
	    // 判断是从那个页面跳转过来的
	    String str = intent.getStringExtra("str");
	    if ("detail".equals(str))
	    {
		// 查看详情
		JobBean jBean = (JobBean) intent.getSerializableExtra("job");
		// 如果没有阅读过，那么修改为阅读状态
		if ("N".equals(jBean.getJobState()) || jBean.getJobState() == "N")
		{
		    HttpClientService.sendMsgOfReadJob(WaitingActivity.this, jBean.getJobId());
		}
		// 直接跳转到工单详情页面
		Message msg = new Message();
		msg.what = 1;
		mHandler.sendMessage(msg);
	    }
	    else if ("confirm".equals(str))
	    {
		// 提交回复
		Message msg = new Message();
		// 判断当前网络是否可用，如果不可用，结束线程
		if (!HttpClientService.isConnect(WaitingActivity.this))
		{
		    msg.what = 0;
		    mHandler.sendMessage(msg);
		    return;
		}
		// 下面提交处理工单的操作
		Bundle bundle = intent.getExtras();
		JobBean jBean = (JobBean) bundle.getSerializable("job");
		String jsString = bundle.getString("result");
		if (HttpClientService.sendConfirmJob(WaitingActivity.this, jBean, jsString))
		{
		    // 如果提交成功
		    msg.what = 2;
		    mHandler.sendMessage(msg);
		}
		else
		{
		    // 提交失败
		    msg.what = 3;
		    mHandler.sendMessage(msg);
		}
	    }
	    else if ("add".equals(str))
	    {
		// 新开户
		Message msg = new Message();
		// 判断当前网络是否可用，如果不可用，结束线程
		if (!HttpClientService.isConnect(WaitingActivity.this))
		{
		    msg.what = 0;
		    mHandler.sendMessage(msg);
		    return;
		}
		// 下面提交新开户请求
		AddFormBean aBean = HttpClientService.getRepostOfAddCustomerInfo();
		if (aBean != null)
		{
		    // 获取新开户信息成功
		    msg.what = 5;
		    msg.obj = aBean;
		    mHandler.sendMessage(msg);
		}
		else
		{
		    // 请求失败
		    msg.what = 3;
		    mHandler.sendMessage(msg);
		}
	    }
	    else if ("confirm_add".equals(str))
	    {
		// 新开户提交
		Message msg = new Message();
		// 判断当前网络是否可用，如果不可用，结束线程
		if (!HttpClientService.isConnect(WaitingActivity.this))
		{
		    msg.what = 0;
		    mHandler.sendMessage(msg);
		    return;
		}
		@SuppressWarnings("unchecked")
		List<ParamBean> pList = (List<ParamBean>) intent.getSerializableExtra("add");
		if (HttpClientService.postAddCustomerInfo(pList))
		{
		    // 提交成功
		    msg.what = 6;
		    mHandler.sendMessage(msg);
		}
		else
		{
		    // 请求失败
		    msg.what = 3;
		    mHandler.sendMessage(msg);
		}
	    }
	    else if ("addJob".equals(str))
	    {
		// 新增工单请求
		Message msg = new Message();
		// 判断当前网络是否可用，如果不可用，结束线程
		if (!HttpClientService.isConnect(WaitingActivity.this))
		{
		    msg.what = 0;
		    mHandler.sendMessage(msg);
		    return;
		}
		String telNum = intent.getStringExtra("telNum");
		String addrStr = intent.getStringExtra("addrStr");
		// 下面提交新增工单请求
		AddFormBean aBean = HttpClientService.getRepostOfAddJobInfo(telNum, addrStr);
		if (aBean != null)
		{
		    // 获取新增工单信息成功
		    msg.what = 7;
		    msg.obj = aBean;
		    mHandler.sendMessage(msg);
		}
		else
		{
		    // 请求失败
		    msg.what = 3;
		    mHandler.sendMessage(msg);
		}
	    }
	    else if ("confirm_addJob".equals(str))
	    {
		// 提交新增工单
		Message msg = new Message();
		// 判断当前网络是否可用，如果不可用，结束线程
		if (!HttpClientService.isConnect(WaitingActivity.this))
		{
		    msg.what = 0;
		    mHandler.sendMessage(msg);
		    return;
		}
		@SuppressWarnings("unchecked")
		List<ParamBean> pList = (List<ParamBean>) intent.getSerializableExtra("job");
		JobBean job = (JobBean)intent.getSerializableExtra("job2");//直办时候，保存一份工单到本地
		String flag = intent.getStringExtra("flag");
		String code = intent.getStringExtra("code");
		if (HttpClientService.postAddJobInfo(WaitingActivity.this,pList,flag,job,code))
		{
		    // 提交成功
		    msg.what = 8;
		    mHandler.sendMessage(msg);
		}
		else
		{
		    // 请求失败
		    msg.what = 3;
		    mHandler.sendMessage(msg);
		}
	    }
	    else if ("scan".equals(str))
	    {
		// 请求钢瓶档案信息
		Message msg = new Message();
		// 判断当前网络是否可用，如果不可用，结束线程
		if (!HttpClientService.isConnect(WaitingActivity.this))
		{
		    msg.what = 0;
		    mHandler.sendMessage(msg);
		    return;
		}
		Bundle bundle = intent.getExtras();
		String jsString = bundle.getString("result");
		String bContent = HttpClientService.getRepostOfBotterInfo(jsString);
		if (bContent != null)
		{
		    // 提交成功
		    msg.what = 9;
		    msg.obj = bContent;
		    mHandler.sendMessage(msg);
		}
		else
		{
		    // 请求失败
		    msg.what = 3;
		    mHandler.sendMessage(msg);
		}
	    }
	}
    }
}
