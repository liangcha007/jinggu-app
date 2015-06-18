package com.jingu.app.login.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.jingu.app.R;
import com.jingu.app.bean.UserBean;
import com.jingu.app.dao.DBUserInfoDao;
import com.jingu.app.main.activity.MainActivityFrag;
import com.jingu.app.service.HttpClientService;
import com.jingu.app.util.BaseConst;

public class LoginingActivity extends Activity
{
    private DBUserInfoDao dbManager;
    private LoginHandler loginHandler = null;
    private LoginThread loginThread = null;
    private Intent intent = null;
    private Message msg = null;
    public Boolean isRun;;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.logining);

	intent = getIntent();
	msg = new Message();

	isRun = true;

	dbManager = new DBUserInfoDao(this);
	if (loginHandler == null)
	{
	    loginHandler = new LoginHandler();
	}
	if (loginThread == null)
	{
	    loginThread = new LoginThread();
	    loginThread.start();
	}
    }

    @Override
    public void onBackPressed()
    {
	isRun = false;
	msg.what = 3;
	loginHandler.sendMessage(msg);
	super.onBackPressed();
    }

    class LoginHandler extends Handler
    {
	@Override
	public void handleMessage(Message msg)
	{
	    switch (msg.what)
	    {
	    case 0:
		// 连接失败
		LoginingActivity.this.setResult(1);
		LoginingActivity.this.finish();
		break;
	    case 1:
		// 登录成功
		Intent intent2 = new Intent(LoginingActivity.this, MainActivityFrag.class);
		startActivity(intent2);
		LoginingActivity.this.finish();
		LoginActivity.lActivity.finish();
		Toast.makeText(LoginingActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
	    case 2:
		// 验证失败
		LoginingActivity.this.setResult(3, intent);
		LoginingActivity.this.finish();
	    case 4:
		// 访问失败，网络可用，但是访问失败
		LoginingActivity.this.setResult(4);
		LoginingActivity.this.finish();
	    default:
		LoginingActivity.this.setResult(2);
		LoginingActivity.this.finish();
		break;
	    }
	}
    }

    class LoginThread extends Thread
    {
	@Override
	public void run()
	{
	    try
	    {
		// 判断当前网络是否可用，如果不可用，结束线程
		if (!HttpClientService.isConnect(LoginingActivity.this))
		{
		    dbManager.closeDB();
		    msg.what = 0;
		    loginHandler.sendMessage(msg);
		    return;
		}
		UserBean user = (UserBean) intent.getSerializableExtra("user");
		// 登录请求，获取回复信息
		String return_str = HttpClientService.getRpostOfLogin(LoginingActivity.this, user);
		// 判断http回复
		if ("connect_error".equals(return_str) || return_str == null)
		{
		    // conntect_error
		    dbManager.closeDB();
		    msg.what = 0;
		    if (!isRun) { return; }
		    loginHandler.sendMessage(msg);
		}
		else
		{
		    // 解析http回复的消息
		    JSONObject jsonObject = new JSONObject(return_str);
		    String state = jsonObject.getString("state");
		    if ("success".equals(state))
		    {
			// 获取了用户信息,将用户信息写入到数据库中
			UserBean userBean = dbManager.query(user.getUsername());
			if (userBean == null)
			{
			    // 如果是第一次登录，新增用户
			    dbManager.add(user);
			}
			else
			{
			    // 不是第一次登录，更新用户信息
			    dbManager.update(user);
			}
			// 更新用户参数
			BaseConst.updateUser(LoginingActivity.this, user.getUsername(), user.getPassword());
			dbManager.closeDB();
			// 登录成功
			msg.what = 1;
			if (!isRun) { return; }
			loginHandler.sendMessage(msg);
		    }
		    else
		    {
			// 验证失败，带原因
			String des = jsonObject.getString("des");
			if (!"".equals(des) || des != null)
			{
			    Bundle data = new Bundle();
			    data.putSerializable("des", des);
			    intent.putExtras(data);
			}
			dbManager.closeDB();
			msg.what = 2;
			if (!isRun) { return; }
			loginHandler.sendMessage(msg);
		    }
		}
	    }
	    catch (JSONException e)
	    {
		dbManager.closeDB();
		e.printStackTrace();
		msg.what = 4;
		if (!isRun) { return; }
		loginHandler.sendMessage(msg);
	    }
	}
    }
}
