package com.jingu.app.main.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.jingu.app.R;
import com.jingu.app.bean.AddFormBean;
import com.jingu.app.bean.ItemBean;
import com.jingu.app.bean.ItemViewBean;
import com.jingu.app.bean.ParamBean;
import com.jingu.app.util.MyActivity;

public class AddCustomerActivity extends MyActivity
{
    public static AddCustomerActivity cActivity = null;
    @SuppressWarnings("deprecation")
    private final int WIGTH = ViewGroup.LayoutParams.FILL_PARENT;
    private final int HEIGTH = ViewGroup.LayoutParams.WRAP_CONTENT;
    public List<ItemViewBean> aList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.add_new_customer);
	cActivity = this;
	// 默认不显示键盘
	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	Intent intent = getIntent();
	AddFormBean aBean = (AddFormBean) intent.getSerializableExtra("item");
	// 声明容器
	aList = new ArrayList<ItemViewBean>();
	// 获取TableLayout
	TableLayout tLayout = (TableLayout) findViewById(R.id.add_table);
	int rowNums = aBean.getItemList().size();
	for (int i = 0; i < rowNums; i++)
	{
	    // 取一个控件节点信息
	    ItemBean iBean = aBean.getItemList().get(i);
	    // 声明一个行
	    TableRow row = new TableRow(this);
	    // TextView控件名称
	    TextView fdName = new TextView(this);
	    // 设置文本
	    fdName.setText(iBean.getName());
	    fdName.setTextColor(getResources().getColor(R.color.black));
	    fdName.setTextSize(16);
	    row.addView(fdName);
	    // 设置value
	    ItemViewBean iViewBean = new ItemViewBean();
	    int type = Integer.parseInt(iBean.getType());
	    switch (type)
	    {
	    case 1:
		// 文本
		EditText dText = new EditText(this);
		String str = "请输入" + iBean.getName();
		dText.setHint(str);
		// 放入容器
		iViewBean.setType(type);
		iViewBean.setView(dText);
		iViewBean.setFd_name(iBean.getFd_name());
		aList.add(iViewBean);
		row.addView(dText);
		break;
	    case 2:
		// 单选
		RadioGroup radioGroup = new RadioGroup(this);
		radioGroup.setGravity(Gravity.LEFT);
		radioGroup.setOrientation(RadioGroup.VERTICAL);
		List<RadioButton> liButtons = new ArrayList<RadioButton>();
		for (int j = 0; j < iBean.getItemList().size(); j++)
		{
		    RadioButton rButton = new RadioButton(this);
		    rButton.setText(iBean.getItemList().get(j));
		    rButton.setTextColor(getResources().getColor(R.color.black));
		    liButtons.add(rButton);
		    radioGroup.addView(rButton);
		}
		// 放入容器
		iViewBean.setType(type);
		iViewBean.setView(radioGroup);
		iViewBean.setRbList(liButtons);
		iViewBean.setFd_name(iBean.getFd_name());
		aList.add(iViewBean);
		row.addView(radioGroup);
		break;
	    case 3:
		// 下拉控件
		Spinner mySpinner = new Spinner(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
			iBean.getItemList());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mySpinner.setAdapter(adapter);
		iViewBean.setType(type);
		iViewBean.setView(mySpinner);
		iViewBean.setFd_name(iBean.getFd_name());
		aList.add(iViewBean);
		row.addView(mySpinner);
		break;
	    case 4:
		// 多行文本
		EditText mText = new EditText(this);
		mText.setHeight(100);
		String strhit = "请输入" + iBean.getName();
		mText.setHint(strhit);
		// 放入容器
		iViewBean.setType(type);
		iViewBean.setView(mText);
		iViewBean.setFd_name(iBean.getFd_name());
		aList.add(iViewBean);
		row.addView(mText);
		break;
	    default:
		break;
	    }
	    //添加*号
	    TextView fTextView =new TextView(this);
	    fTextView.setText("*");
	    fTextView.setTextColor(getResources().getColor(R.color.red));
	    fTextView.setTextSize(16);
	    row.addView(fTextView);
	    tLayout.addView(row, new TableLayout.LayoutParams(WIGTH, HEIGTH));
	}
    }

    /**
     * 返回按钮
     */
    public void backHandler(View v)
    {
	onBackPressed();
    }

    /**
     * 提交处理结果
     */
    public void postHandler(View v)
    {
	if (aList == null)
	{
	    Toast.makeText(this, "这个问题很严重!", Toast.LENGTH_SHORT).show();
	}
	else
	{
	    List<ParamBean> pList = new ArrayList<ParamBean>();
	    for (int i = 0; i < aList.size(); i++)
	    {
		ItemViewBean iViewBean = aList.get(i);
		ParamBean pbBean = new ParamBean();
		pbBean.setParamName(iViewBean.getFd_name());
		int type = iViewBean.getType();
		switch (type)
		{
		case 2:
		    // 单选
		    RadioGroup rGroup = (RadioGroup) iViewBean.getView();
		    RadioButton radioButton = (RadioButton) findViewById(rGroup.getCheckedRadioButtonId());
		    if (radioButton != null)
		    {
			pbBean.setParamValue(radioButton.getText().toString());
		    }
		    else
		    {
			Toast.makeText(this, "请确定相关项是否录入正确!", Toast.LENGTH_SHORT).show();
			return;
		    }
		    break;
		case 3:
		    // 下拉
		    Spinner spinner = (Spinner) iViewBean.getView();
		    String spTx = spinner.getSelectedItem().toString();
		    if ("".equals(spTx) || spTx == null)
		    {
			Toast.makeText(this, "请确定相关项是否录入正确!", Toast.LENGTH_SHORT).show();
			return;
		    }
		    else
		    {
			pbBean.setParamValue(spTx);
		    }
		    break;
		default:
		    // 文本
		    TextView tx = (TextView) iViewBean.getView();
		    String strTx = tx.getText().toString();
		    if ("".equals(strTx) || strTx == null)
		    {
			Toast.makeText(this, "请确定相关项是否录入正确!", Toast.LENGTH_SHORT).show();
			return;
		    }
		    else
		    {
			pbBean.setParamValue(strTx);
		    }
		    break;
		}
		pList.add(pbBean);
	    }

	    Bundle data = new Bundle();
	    data.putSerializable("add", (Serializable) pList);
	    Intent intentWait = new Intent(AddCustomerActivity.this, WaitingActivity.class);
	    intentWait.putExtras(data);
	    intentWait.putExtra("str", "confirm_add");
	    startActivityForResult(intentWait, 0);
	}
    }

    @Override
    protected void onDestroy()
    {
	cActivity = null;
	super.onDestroy();
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
		Toast.makeText(AddCustomerActivity.this, R.string.login_net_fial, Toast.LENGTH_SHORT).show();
		break;
	    case 1:
		// 提交失败
		Toast.makeText(AddCustomerActivity.this, R.string.confirm_add_fail, Toast.LENGTH_SHORT).show();
		break;
	    case 2:
		// 正常返回
		break;
	    default:
		break;
	    }
	}
    }

}
