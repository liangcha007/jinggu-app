package com.jingu.app.login.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jingu.app.R;
import com.jingu.app.bean.UserBean;
import com.jingu.app.util.BaseConst;

public class LoginActivity extends Activity
{
    private EditText user_name;
    private EditText pass_word;
    private Button btn;
    public static LoginActivity lActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.login_main);

	lActivity = this;

	btn = (Button) findViewById(R.id.button_login);
	user_name = (EditText) findViewById(R.id.edit_user_name);
	pass_word = (EditText) findViewById(R.id.edit_pass_word);
	TextChange tChange = new TextChange();
	user_name.addTextChangedListener(tChange);
	pass_word.addTextChangedListener(tChange);
//	TextView tVersion = (TextView) findViewById(R.id.id_version);
//	try
//	{
//	    tVersion.setText("程序版本:" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
//	}
//	catch (NameNotFoundException e)
//	{
//	    e.printStackTrace();
//	}
	DBHandler();

    }

    public void DBHandler()
    {
	UserBean user = BaseConst.getUser(this);
	user_name.setText(user.getUsername());
	pass_word.setText(user.getPassword());
	if (getExitMode() == 0)
	{
	    // 如果是后台退出又重新登录，那么肯定保存的有用户名和密码，则直接登录
	    Bundle data = new Bundle();
	    data.putSerializable("user", user);
	    Intent intent = new Intent(LoginActivity.this, LoginingActivity.class);
	    intent.putExtras(data);
	    startActivityForResult(intent, 0);
	}
    }

    class TextChange implements TextWatcher
    {
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
	    if (user_name.getText().length() == 0 || pass_word.getText().length() == 0)
	    {
		btn.setTextColor(getResources().getColor(R.color.royalblue));
		btn.setEnabled(false);
	    }
	    else if (user_name.getText().length() != 0 && pass_word.getText().length() != 0)
	    {
		btn.setTextColor(getResources().getColor(R.color.ivory));
		btn.setEnabled(true);
	    }
	}

	@Override
	public void afterTextChanged(Editable s)
	{
	}
    }

    public void LoginHandler(View view)
    {
	UserBean user = new UserBean(user_name.getText().toString(), pass_word.getText().toString());
	Bundle data = new Bundle();
	data.putSerializable("user", user);
	Intent intent = new Intent(LoginActivity.this, LoginingActivity.class);
	intent.putExtras(data);
	startActivityForResult(intent, 0);
    }

    public int getExitMode()
    {
	SharedPreferences settings = getSharedPreferences("setting", 0);
	return settings.getInt("exit", 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	if (requestCode == 0)
	{
	    switch (resultCode)
	    {
	    case 1:
		// 网络失败
		Toast.makeText(this, R.string.login_net_fial, Toast.LENGTH_SHORT).show();
		break;
	    case 2:
		// 取消登录
		Toast.makeText(this, R.string.login_cancel, Toast.LENGTH_SHORT).show();
		break;
	    case 4:
		// 访问失败
		Toast.makeText(this, R.string.login_faile_address, Toast.LENGTH_SHORT).show();
		break;
	    default:
		// 登录失败
		Bundle bundle2 = data.getExtras();
		String des = bundle2.getSerializable("des").toString();
		if (!"".equals(des) && des != null)
		{
		    Toast.makeText(this, des, Toast.LENGTH_SHORT).show();
		}
		else
		{
		    Toast.makeText(this, R.string.login_fail, Toast.LENGTH_SHORT).show();
		}
		break;
	    }
	}
    }
}
