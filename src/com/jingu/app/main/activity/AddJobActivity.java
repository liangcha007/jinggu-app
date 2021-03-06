package com.jingu.app.main.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.jingu.app.R;
import com.jingu.app.bean.AddFormBean;
import com.jingu.app.bean.AttrBean;
import com.jingu.app.bean.ItemBean;
import com.jingu.app.bean.ItemViewBean;
import com.jingu.app.bean.JobBean;
import com.jingu.app.bean.ParamBean;
import com.jingu.app.bean.TableSecondBean;
import com.jingu.app.service.AnalyistJosnService;
import com.jingu.app.ui.BottleMenu;
import com.jingu.app.ui.PopMenu;
import com.jingu.app.util.BaseConst;
import com.jingu.app.util.MyActivity;

public class AddJobActivity extends MyActivity
{
    public static AddJobActivity jActivity = null;
    @SuppressWarnings("deprecation")
    private final int WIGTH = ViewGroup.LayoutParams.FILL_PARENT;
    private final int HEIGTH = ViewGroup.LayoutParams.WRAP_CONTENT;
    public List<ItemViewBean> aList = null;
    public AddFormBean aBean = null;
    // 二级联动
    private Spinner firstSpinner = null; // 一级菜单
    private Spinner secondSpinner = null; // 二级菜单
    ArrayAdapter<String> firstAdapter = null; // 一级适配器
    ArrayAdapter<String> secondAdapter = null; // 二级适配器

    public List<TableSecondBean> tSecondList = null;// 存放规格表格行记录
    public TableLayout tableSecond = null;// 规格表格(在不同的地方操作)
    public String[] Fir = null; // 一级菜单数组
    public String[][] Sec = null;// 二级菜单数组

    private BottleMenu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.add_new_job);
	jActivity = this;
	// 默认不显示键盘
	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	Intent intent = getIntent();
	aBean = (AddFormBean) intent.getSerializableExtra("item");
	/* =====开始代码生成界面===== */
	// 声明容器
	aList = new ArrayList<ItemViewBean>();
	// 获取TableLayout
	TableLayout tLayout = (TableLayout) findViewById(R.id.add_table);
	int rowNums = aBean.getItemList().size();
	for (int i = 0; i < rowNums; i++)
	{
	    // 声明一个抽象控件实体
	    ItemViewBean iViewBean = new ItemViewBean();
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
	    // 设置中文名
	    iViewBean.setTmp_static_name(iBean.getName());
	    // 类型
	    int type = Integer.parseInt(iBean.getType());
	    switch (type)
	    {
	    case 1:
		// 文本
		EditText dText = new EditText(this);
		String str = "请输入" + iBean.getName();
		dText.setHint(str);
		if (iBean.getItemList() != null)
		{
		    dText.setText(iBean.getItemList().get(0));
		}
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
		// 设置内容
		if (iBean.getItemList() != null)
		{
		    mText.setText(iBean.getItemList().get(0));
		}
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
	    // 添加*号
	    TextView fTextView = new TextView(this);
	    fTextView.setText("*");
	    fTextView.setTextColor(getResources().getColor(R.color.red));
	    fTextView.setTextSize(16);
	    row.addView(fTextView);
	    tLayout.addView(row, new TableLayout.LayoutParams(WIGTH, HEIGTH));
	}
	// 设置显示选择信息内容
	tSecondList = new ArrayList<TableSecondBean>();
	// 获取Table_first
	TableLayout tableFirst = (TableLayout) findViewById(R.id.table_first);
	AttrBean attrBean = aBean.getaBean();
	if (attrBean != null)
	{
	    RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.add_setting);
	    // 设置为可见
	    relativeLayout.setVisibility(RelativeLayout.VISIBLE);
	}
	// 设置二级联动信息
	Fir = AnalyistJosnService.getFirstMune(attrBean);
	Sec = AnalyistJosnService.getSecondMune(attrBean);
	setSpinner();
	// 显示瓶子规格
	List<ParamBean> ruList = attrBean.getrList();
	for (int i = 0; i < ruList.size(); i++)
	{
	    ParamBean p = ruList.get(i);
	    TableRow tRow = new TableRow(this);
	    // TextView控件
	    TextView fdName = new TextView(this);
	    fdName.setText(p.getParamValue());
	    fdName.setTextColor(getResources().getColor(R.color.black));
	    fdName.setTextSize(16);
	    tRow.addView(fdName);
	    // EditText控件
	    EditText et = new EditText(this);
	    et.setInputType(InputType.TYPE_CLASS_NUMBER);
	    et.setText("1");
	    tRow.addView(et);
	    // Button控件
	    Button bt = new Button(this);
	    bt.setText("确定");
	    bt.setPadding(20, 20, 20, 20);
	    bt.setBackgroundResource(R.drawable.login_button_corners);
	    bt.setTextColor(getResources().getColor(R.color.black));
	    MyButtonListener mListener = new MyButtonListener(et, p.getParamName(), p.getParamValue());
	    bt.setOnClickListener(mListener);
	    tRow.addView(bt);
	    // 显示出来
	    tableFirst.addView(tRow, new TableLayout.LayoutParams(WIGTH, HEIGTH));
	}
	// 第二个table
	tableSecond = (TableLayout) findViewById(R.id.table_second);

	// 初始化弹出菜单
	initMenu();
    }

    /**
     * @author zhouc52 设置 按钮的 点击事件
     */
    class MyButtonListener implements OnClickListener
    {
	private EditText editText = null;
	private String ruleId = "";
	private String ruleName = "";

	public MyButtonListener(EditText editText, String ruleId, String ruleName)
	{
	    super();
	    this.editText = editText;
	    this.ruleId = ruleId;
	    this.ruleName = ruleName;
	}

	@Override
	public void onClick(View v)
	{
	    // 获取一级及二级菜单的值，同时得到二级菜单的id,button对应编辑框的值，规则的id，规则名
	    String tFirstName = firstSpinner.getSelectedItem().toString();
	    String tSecondName = secondSpinner.getSelectedItem().toString();
	    String muneId = AnalyistJosnService.getMuneIdByName(tSecondName, aBean.getListParamBeans());
	    String nums = editText.getText().toString();
	    String muneName = tFirstName + "/" + tSecondName;

	    // 显示第二个表格信息
	    TableRow tb = new TableRow(AddJobActivity.this);
	    TextView t1 = new TextView(AddJobActivity.this);
	    t1.setText(muneName);
	    t1.setTextColor(getResources().getColor(R.color.black));
	    t1.setBackgroundResource(R.anim.table_frame_gray);
	    t1.setPadding(20, 20, 20, 20);
	    t1.setTextSize(16);
	    tb.addView(t1);

	    TextView t2 = new TextView(AddJobActivity.this);
	    t2.setText(ruleName);
	    t2.setTextColor(getResources().getColor(R.color.black));
	    t2.setBackgroundResource(R.anim.table_frame_gray);
	    t2.setPadding(20, 20, 20, 20);
	    t2.setTextSize(16);
	    tb.addView(t2);

	    TextView t3 = new TextView(AddJobActivity.this);
	    t3.setText(nums);
	    t3.setTextColor(getResources().getColor(R.color.black));
	    t3.setBackgroundResource(R.anim.table_frame_gray);
	    t3.setPadding(20, 20, 20, 20);
	    t3.setTextSize(16);
	    tb.addView(t3);

	    TextView tt = new TextView(AddJobActivity.this);
	    tt.setText("   ");
	    tb.addView(tt);

	    TextView t4 = new TextView(AddJobActivity.this);
	    t4.setBackgroundResource(R.drawable.del_3);
	    t4.setClickable(true);
	    tb.addView(t4);
	    // 为图片按钮添加单击事件
	    MyTextView textListenter = new MyTextView(tb);
	    t4.setOnClickListener(textListenter);

	    // 添加到容器中
	    TableSecondBean tSecondBean = new TableSecondBean(muneId, ruleId, nums, muneName, ruleName, tb);
	    tSecondList.add(tSecondBean);
	    // 显示出来
	    tableSecond.addView(tb, new TableLayout.LayoutParams(WIGTH, HEIGTH));
	}
    }

    /**
     * @author zhouc52 删除按钮的click事件
     */
    public class MyTextView implements OnClickListener
    {
	private TableRow tRow;

	public MyTextView(TableRow tRow)
	{
	    super();
	    this.tRow = tRow;
	}

	@Override
	public void onClick(View v)
	{
	    // 删除记录
	    for (int i = 0; i < tSecondList.size(); i++)
	    {
		TableSecondBean tsb = tSecondList.get(i);
		if (tRow.equals(tsb.gettRow()) || tRow == tsb.gettRow())
		{
		    tSecondList.remove(i);
		    tRow.setVisibility(TableLayout.GONE);
		    break;
		}
	    }
	}
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
		    intent.setClass(AddJobActivity.this, MipcaActivityCapture.class);
		    startActivityForResult(intent, 1);
		    break;
		case 2:
		    intent.putExtra("Scan", "Scan");
		    intent.putExtra("type", 2);
		    intent.setClass(AddJobActivity.this, MipcaActivityCapture.class);
		    startActivityForResult(intent, 1);
		    break;
		case 3:
		    intent.putExtra("Scan", "Scan");
		    intent.putExtra("type", 3);
		    intent.setClass(AddJobActivity.this, MipcaActivityCapture.class);
		    startActivityForResult(intent, 1);
		    break;
		}
	    }
	});
    }

    /**
     * 返回按钮
     */
    public void backHandler(View v)
    {
	Intent intent = new Intent(AddJobActivity.this, AddJobCheckActivity.class);
	startActivity(intent);
	this.finish();
    }

    @Override
    protected void onDestroy()
    {
	jActivity = null;
	super.onDestroy();
    }

    /**
     * 扫码直办
     * 
     * @param v
     */
    public void zhibanScan(View v)
    {
	// Intent intent = new Intent();
	// intent.putExtra("Scan", "Scan");
	// intent.setClass(AddJobActivity.this, MipcaActivityCapture.class);
	// startActivityForResult(intent, 1);
	mMenu.showAsDropDown(v);
    }

    public void zhiBan(String codeStr)
    {
	String jobId = UUID.randomUUID().toString();// 工单ID
	String jobTitle = "";// 工单标题
	StringBuffer jobContent = new StringBuffer();// 工单内容
	String jobTel = "";// 联系电话

	jobContent.append("受理人：").append(BaseConst.getUserFromApplication(this).getUsername()).append("\n");
	if (aList == null)
	{
	    Toast.makeText(this, "这个问题很严重!", Toast.LENGTH_SHORT).show();
	    return;
	}
	else
	{
	    List<ParamBean> pList = new ArrayList<ParamBean>();
	    for (int i = 0; i < aList.size(); i++)
	    {
		ItemViewBean iViewBean = aList.get(i);
		ParamBean pbBean = new ParamBean();
		pbBean.setParamName(iViewBean.getFd_name());
		// 设置直办插入的本地工单内容
		jobContent.append(iViewBean.getTmp_static_name()).append(": ");
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
			jobContent.append(radioButton.getText().toString()).append("\n");
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
			jobContent.append(spTx).append("\n");

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
			jobContent.append(strTx).append("\n");
			if ("company_tel".equals(pbBean.getParamName()))// 如果是电话号码，记录号码
			{
			    jobTel = strTx;// 设置电话号码
			}
			else if (pbBean.getParamName().equals("addr"))// 地址
			{
			    jobTitle = "直办-" + strTx;
			}
		    }
		    break;
		}
		pList.add(pbBean);
	    }
	    // 检查type参数
	    ParamBean pb = new ParamBean();
	    pb.setParamName("company_id");
	    pb.setParamValue(aBean.getCompand_id());
	    pList.add(pb);
	    // content
	    int nums = tSecondList.size();
	    StringBuffer str = new StringBuffer();
	    if (nums > 0)
	    {
		jobContent.append("客户需求：\n");
		for (int i = 0; i < nums; i++)
		{
		    TableSecondBean tsb = tSecondList.get(i);
		    String strs = tsb.getRuleId() + "," + tsb.getNums() + "," + tsb.getMuneId();
		    str.append(strs).append(";");

		    jobContent.append(i + 1).append(".").append(tsb.getMuneName()).append(",")
			    .append(tsb.getRuleName()).append(",").append(tsb.getNums()).append("个\n");
		}
	    }

	    ParamBean pBean = new ParamBean();
	    pBean.setParamName("service_contents");
	    pBean.setParamValue(str.toString());
	    pList.add(pBean);

	    Intent intentWait = new Intent(AddJobActivity.this, WaitingActivity.class);
	    if (!"".equals(codeStr) && codeStr != null && !"".equals(codeStr.trim()))
	    {
		if (",".equals(codeStr.substring(codeStr.length() - 1, codeStr.length())))
		{
		    codeStr = codeStr.substring(0, codeStr.length() - 1);
		}
		intentWait.putExtra("code", codeStr);
		jobContent.append("钢瓶编号：").append(codeStr).append("\n");
	    }

	    // 将工单插入到本地
	    // 插入新工单
	    JobBean job = new JobBean(jobId, jobTitle, jobContent.toString(), BaseConst.getUserFromApplication(this).getUsername(), jobTel,
		    BaseConst.getDate(new Date(),2));
	    Bundle data = new Bundle();
	    data.putSerializable("job", (Serializable) pList);
	    data.putSerializable("job2", job);

	    intentWait.putExtras(data);
	    intentWait.putExtra("str", "confirm_addJob");
	    intentWait.putExtra("flag", "1");
	    startActivityForResult(intentWait, 0);
	}
    }

    // 直办 --- 同时保存一份工单到本地已办件中
    public void zhiHandler(View v)
    {
	EditText codEditText = (EditText) findViewById(R.id.zhiban_num);
	String code = codEditText.getText().toString();
	zhiBan(code);
    }

    // 派单
    public void postHandler(View v)
    {
	if (aList == null)
	{
	    Toast.makeText(this, "这个问题很严重!", Toast.LENGTH_SHORT).show();
	    return;
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
	    // 检查type参数
	    ParamBean pb = new ParamBean();
	    pb.setParamName("company_id");
	    pb.setParamValue(aBean.getCompand_id());
	    pList.add(pb);
	    // content
	    int nums = tSecondList.size();
	    StringBuffer str = new StringBuffer();
	    for (int i = 0; i < nums; i++)
	    {
		TableSecondBean tsb = tSecondList.get(i);
		String strs = tsb.getRuleId() + "," + tsb.getNums() + "," + tsb.getMuneId();
		str.append(strs).append(";");
	    }
	    ParamBean pBean = new ParamBean();
	    pBean.setParamName("service_contents");
	    pBean.setParamValue(str.toString());
	    pList.add(pBean);
	    // Toast.makeText(this, str.toString(), Toast.LENGTH_LONG).show();
	    // 进行提交
	    Bundle data = new Bundle();
	    data.putSerializable("job", (Serializable) pList);
	    Intent intentWait = new Intent(AddJobActivity.this, WaitingActivity.class);
	    intentWait.putExtras(data);
	    intentWait.putExtra("str", "confirm_addJob");
	    intentWait.putExtra("flag", "2");
	    startActivityForResult(intentWait, 0);
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
		Toast.makeText(AddJobActivity.this, R.string.login_net_fial, Toast.LENGTH_SHORT).show();
		break;
	    case 1:
		// 提交失败
		Toast.makeText(AddJobActivity.this, R.string.confirm_add_job_fail, Toast.LENGTH_SHORT).show();
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
		// 原处理方式，根据获取的钢瓶编号，直接提交
		String result = data.getStringExtra("result");
		zhiBan(result);
		break;
	    case RESULT_FIRST_USER:
		// 将钢瓶编码内容更新到钢瓶编码编辑框
		String result1 = data.getStringExtra("result");
		EditText codEditText = (EditText) findViewById(R.id.zhiban_num);
		String code = codEditText.getText().toString();
		code = result1 + code;
		codEditText.setText(code);
		break;
	    default:
		break;
	    }
	}
    }

    /*
     * 设置二级联动
     */
    private void setSpinner()
    {
	firstSpinner = (Spinner) findViewById(R.id.first);
	secondSpinner = (Spinner) findViewById(R.id.second);
	// 绑定适配器和值
	firstAdapter = new ArrayAdapter<String>(AddJobActivity.this, android.R.layout.simple_spinner_item, Fir);
	firstAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	firstSpinner.setAdapter(firstAdapter);
	firstSpinner.setSelection(0, true); // 设置默认选中项

	secondAdapter = new ArrayAdapter<String>(AddJobActivity.this, android.R.layout.simple_spinner_item, Sec[0]);
	secondAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	secondSpinner.setAdapter(secondAdapter);
	secondSpinner.setSelection(0, true); // 默认选中第0个

	firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
	{
	    // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
	    @Override
	    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
	    {
		// 将地级适配器的值改变为city[position]中的值
		secondAdapter = new ArrayAdapter<String>(AddJobActivity.this, android.R.layout.simple_spinner_item,
			Sec[position]);
		secondAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 设置二级下拉列表的选项内容适配器
		secondSpinner.setAdapter(secondAdapter);
	    }

	    @Override
	    public void onNothingSelected(AdapterView<?> arg0)
	    {

	    }
	});
    }
}
