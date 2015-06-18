package com.jingu.app.main.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jingu.app.R;
import com.jingu.app.bean.JobBean;
import com.jingu.app.ui.BottleMenu;
import com.jingu.app.ui.PopMenu;
import com.jingu.app.util.BaseConst;
import com.jingu.app.util.MyActivity;
import com.jingu.app.util.MyApplication;

public class NewJobActivity extends MyActivity
{
    public static NewJobActivity nJobActivity = null;
    public JobBean jBean;
    TextView j_title;
    TextView j_date;
    TextView j_content;
    TextView j_tel;
    EditText rl_code;
    Spinner sp;

    private BottleMenu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.new_job_content);
	nJobActivity = this;
	Intent intent = getIntent();
	j_title = (TextView) findViewById(R.id.j_title);
	j_date = (TextView) findViewById(R.id.j_date);
	j_content = (TextView) findViewById(R.id.j_content);
	j_tel = (TextView) findViewById(R.id.j_tel);
	rl_code = (EditText) findViewById(R.id.rl_code);
	MyApplication.getInstance().addActivity(this);
	// 默认不显示键盘
	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	// 只能输入数字
	rl_code.setInputType(InputType.TYPE_CLASS_NUMBER);
	rl_code.clearFocus();
	jBean = (JobBean) intent.getSerializableExtra("job");
	// 初始化下拉列表
	sp = (Spinner) findViewById(R.id.j_reply);
	String[] str = { "完成", "取消" };
	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, str);
	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	sp.setAdapter(adapter);
	// 初始化显示工单信息到界面
	j_title.setText(jBean.getJobTitle());
	j_date.setText(BaseConst.getDate(jBean.getJobDate()));
	j_content.setText(jBean.getJobContent());
	j_tel.setText(jBean.getTelNum());
	// 如果电话号码为空，那么隐藏呼叫的按钮
	if ("".equals(jBean.getTelNum()) || jBean.getTelNum() == null)
	{
	    Button b = (Button) findViewById(R.id.b_tel);
	    b.setVisibility(Button.GONE);
	}

	// 初始化弹出菜单
	initMenu();
    }

    /**
     * 初始化弹出菜单
     */
    public void initMenu()
    {
	mMenu = new BottleMenu(this);
	mMenu.addItem("扫码提交", 1);
	mMenu.addItem("多瓶扫码", 2);
	mMenu.addItem("扫码返回", 3);

	mMenu.setOnItemSelectedListener(new PopMenu.OnItemSelectedListener()
	{
	    @Override
	    public void selected(View view, PopMenu.Item item, int position)
	    {
		Intent intent = new Intent();
		switch (item.id)
		{
		case 1:
		    intent.putExtra("Scan", "Scan");
		    intent.putExtra("type", 1);
		    intent.setClass(NewJobActivity.this, MipcaActivityCapture.class);
		    startActivityForResult(intent, 1);
		    break;
		case 2:
		    intent.putExtra("Scan", "Scan");
		    intent.putExtra("type", 2);
		    intent.setClass(NewJobActivity.this, MipcaActivityCapture.class);
		    startActivityForResult(intent, 1);
		    break;
		case 3:
		    intent.putExtra("Scan", "Scan");
		    intent.putExtra("type", 3);
		    intent.setClass(NewJobActivity.this, MipcaActivityCapture.class);
		    startActivityForResult(intent, 1);
		    break;
		}
	    }
	});
    }

    /**
     * 扫描二维码提交工单
     * 
     * @param v
     */
    public void scanConfirm(View v)
    {
	mMenu.showAsDropDown(v);
    }

    /**
     * 返回按钮
     */
    public void backHandler(View v)
    {
	if ("N".equals(jBean.getJobState()) || jBean.getJobState() == "N")
	{
	    // 如果工单是未读状态，返回的时候刷新
	    NewJobActivity.this.finish();
	    Intent i = new Intent(NewJobActivity.this, MainActivityFrag.class);
	    startActivity(i);
	}
	else
	{
	    NewJobActivity.this.finish();
	}
    }

    @Override
    public void onBackPressed()
    {
	if ("N".equals(jBean.getJobState()) || jBean.getJobState() == "N")
	{
	    // 如果工单是未读状态，返回的时候刷新
	    NewJobActivity.this.finish();
	    Intent i = new Intent(NewJobActivity.this, MainActivityFrag.class);
	    startActivity(i);
	}
	else
	{
	    NewJobActivity.this.finish();
	}
    }

    /**
     * 提交处理结果
     */
    public void confirmPost(View v)
    {
	String replyStr = sp.getSelectedItem().toString();
	jBean.setJobReply(replyStr);
	Bundle data = new Bundle();
	data.putSerializable("job", jBean);
	String tmp = rl_code.getText().toString();
	if (!"".equals(tmp) && tmp != null && tmp.length()>0)
	{
	    if (",".equals(tmp.substring(tmp.length() - 1, tmp.length())))
	    {
		tmp = tmp.substring(0, tmp.length() - 1);
	    }
	    data.putString("result", tmp);
	}
	else
	{
	    data.putString("result", "");
	}
	Intent intentWait = new Intent(NewJobActivity.this, WaitingActivity.class);
	intentWait.putExtras(data);
	intentWait.putExtra("str", "confirm");
	startActivityForResult(intentWait, 0);
    }

    @Override
    protected void onDestroy()
    {
	nJobActivity = null;
	super.onDestroy();
    }

    /**
     * 点击呼叫此号码
     * 
     * @param v
     */
    public void TelNum(View v)
    {
	String telNum = j_tel.getText().toString().trim();
	if (!"".equals(telNum) && telNum != null)
	{
	    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telNum));
	    startActivity(intent);
	}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	if (requestCode == 0)
	{
	    switch (resultCode)
	    {
	    case 0:
		// 网络出问题
		Toast.makeText(NewJobActivity.this, R.string.login_net_fial, Toast.LENGTH_SHORT).show();
		break;
	    case 1:
		// 提交失败
		Toast.makeText(NewJobActivity.this, R.string.confirm_fail, Toast.LENGTH_SHORT).show();
		break;
	    case 2:
		// 正常返回
		break;
	    default:
		break;
	    }
	}
	else if (requestCode == 1)
	{
	    switch (resultCode)
	    {
	    case RESULT_OK:
		// 直接提交(扫码提交和扫多码提交)
		String replyStr = sp.getSelectedItem().toString();
		jBean.setJobReply(replyStr);
		String result = data.getExtras().getString("result");
		Bundle bundle2 = new Bundle();
		bundle2.putSerializable("job", jBean);
		if (!"".equals(result) && result != null && result.length()>0)
		{
		    if (",".equals(result.substring(result.length() - 1, result.length())))
		    {
			result = result.substring(0, result.length() - 1);
		    }
		}
		bundle2.putString("result", result);
		Intent intentWait = new Intent(NewJobActivity.this, WaitingActivity.class);
		intentWait.putExtras(bundle2);
		intentWait.putExtra("str", "confirm");
		startActivityForResult(intentWait, 0);
		break;
	    case RESULT_FIRST_USER:
		// 扫码后返回，结合手动输入提交
		String result1 = data.getExtras().getString("result");
		String codeTmp = rl_code.getText().toString();
		codeTmp = result1 + codeTmp;
		rl_code.setText(codeTmp);
		break;
	    default:
		break;
	    }
	}
    }

}
