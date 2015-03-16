package com.jingu.app.main.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jingu.app.R;
import com.jingu.app.bean.JobBean;
import com.jingu.app.dao.DBJobInfoDao;
import com.jingu.app.util.BaseConst;
import com.jingu.app.util.MyActivity;

public class DoneJobActivity extends MyActivity
{
    TextView j_tel;
    boolean mark;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.done_job_content);

	mark = false;
	Intent intent = getIntent();
	JobBean jBean = (JobBean) intent.getSerializableExtra("job");
	TextView j_title = (TextView) findViewById(R.id.j_title);
	TextView j_date = (TextView) findViewById(R.id.j_date);
	TextView j_content = (TextView) findViewById(R.id.j_content);
	TextView j_reply = (TextView) findViewById(R.id.j_reply);
	TextView j_comfirTime = (TextView) findViewById(R.id.confirm_time_content);
	j_tel = (TextView) findViewById(R.id.j_tel);
	j_title.setText(jBean.getJobTitle());
	j_date.setText(BaseConst.getDate(jBean.getJobDate()));
	j_content.setText(jBean.getJobContent());
	j_reply.setText(jBean.getJobReply());
	j_comfirTime.setText(jBean.getConfirmdate());
	j_tel.setText(jBean.getTelNum());
	// 如果电话号码为空，那么隐藏呼叫的按钮
	if ("".equals(jBean.getTelNum()) || jBean.getTelNum() == null)
	{
	    Button b = (Button) findViewById(R.id.b_tel);
	    b.setVisibility(Button.GONE);
	}
	// 如果是取消的类型，更新工单状态为已完成
	if ("Q".equals(jBean.getJobType()) || jBean.getJobType() == "Q")
	{
	    mark = true;
	    DBJobInfoDao dInfoDao = new DBJobInfoDao(this);
	    dInfoDao.updateState(jBean.getJobId(), "Q");
	    dInfoDao.closeDB();
	}
    }

    /**
     * 返回按钮
     */
    public void backHandler(View v)
    {
	if (mark)
	{
	    DoneJobActivity.this.finish();
	    Intent i = new Intent(DoneJobActivity.this, MainActivityFrag.class);
	    startActivity(i);
	}
	else
	{
	    DoneJobActivity.this.setResult(0);
	    DoneJobActivity.this.finish();
	}
    }

    @Override
    public void onBackPressed()
    {
	if (mark)
	{
	    DoneJobActivity.this.finish();
	    Intent i = new Intent(DoneJobActivity.this, MainActivityFrag.class);
	    startActivity(i);
	}
	else
	{
	    DoneJobActivity.this.setResult(0);
	    DoneJobActivity.this.finish();
	}
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
}
