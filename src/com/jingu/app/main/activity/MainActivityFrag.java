package com.jingu.app.main.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jingu.app.R;
import com.jingu.app.dao.DBJobInfoDao;
import com.jingu.app.fragment.DoneJobFragment;
import com.jingu.app.fragment.NewJobFragment;
import com.jingu.app.fragment.ScanFragment;
import com.jingu.app.fragment.SettingFragment;
import com.jingu.app.service.BackGroundService;
import com.jingu.app.service.UpdateManager;
import com.jingu.app.util.BaseConst;
import com.jingu.app.util.MyApplication;
import com.jingu.app.util.MyFragmentAdapter;

public class MainActivityFrag extends FragmentActivity
{
    public static String TAG = "JIN_GU";
    public static MainActivityFrag instance = null;

    private long exitTime = 0;
    // 四个图片按钮
    ImageView ImageNewJob;
    ImageView ImageOldJob;
    ImageView ImageScan;
    ImageView ImageSetting;
    // 被选中的图片
    ImageView mTabImg;
    // 页面容器
    ViewPager mViewPager;
    // 页面集合
    List<Fragment> fragmentList;
    // 四个Fragemnt
    NewJobFragment nJobFragment;
    DoneJobFragment dJobFragment;
    ScanFragment scanFragment;
    SettingFragment settingFragment;

    // 屏幕宽度
    int screenWidth;
    // 当前选中的项
    private int currIndex = 0;
    private int zero = 0;
    private int one;
    private int two;
    private int three;

    private boolean menu_display = false;
    private PopupWindow menuWindow;
    // 服务
    public static Intent serviceIntent = null;

    @Override
    protected void onCreate(Bundle arg0)
    {
	super.onCreate(arg0);
	setContentView(R.layout.main_jingu);
	instance = this;
	MyApplication.getInstance().addActivity(this);
	initHandler(arg0);
    }

    public void initHandler(Bundle arg0)
    {
	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	mViewPager = (ViewPager) findViewById(R.id.tabpager);
	mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

	ImageNewJob = (ImageView) findViewById(R.id.img_weixin);
	ImageOldJob = (ImageView) findViewById(R.id.img_address);
	ImageScan = (ImageView) findViewById(R.id.img_scan);
	ImageSetting = (ImageView) findViewById(R.id.img_settings);
	mTabImg = (ImageView) findViewById(R.id.img_tab_now);

	ImageNewJob.setOnClickListener(new MyOnClickListener(0));
	ImageOldJob.setOnClickListener(new MyOnClickListener(1));
	ImageScan.setOnClickListener(new MyOnClickListener(2));
	ImageSetting.setOnClickListener(new MyOnClickListener(3));

	Display currDisplay = getWindowManager().getDefaultDisplay();
	@SuppressWarnings("deprecation")
	int displayWidth = currDisplay.getWidth();
	one = displayWidth / 4;
	two = one * 2;
	three = one * 3;

	fragmentList = new ArrayList<Fragment>();
	nJobFragment = new NewJobFragment();
	dJobFragment = new DoneJobFragment();
	scanFragment = new ScanFragment();
	settingFragment = new SettingFragment();

	fragmentList.add(nJobFragment);
	fragmentList.add(dJobFragment);
	fragmentList.add(scanFragment);
	fragmentList.add(settingFragment);

	MyFragmentAdapter fAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragmentList);
	mViewPager.setAdapter(fAdapter);

	// 开启服务
	updateIsBack(0);
	if (serviceIntent == null)
	{
	    serviceIntent = new Intent(getApplicationContext(), BackGroundService.class);
	    startService(serviceIntent);
	    Log.i(TAG, "now start a new service");
	}

	// 自动检测是否需要更新
	if ("0".equals(BaseConst.getParams(instance, "update_param")))
	{
	    UpdateManager uManager = UpdateManager.getUpdateManager();
	    uManager.checkAppUpdate(this, false);
	}
    }

    public class MyOnClickListener implements View.OnClickListener
    {
	private int index = 0;

	public MyOnClickListener(int i)
	{
	    index = i;
	}

	@Override
	public void onClick(View v)
	{
	    mViewPager.setCurrentItem(index);
	}
    }

    public class MyOnPageChangeListener implements OnPageChangeListener
    {
	@Override
	public void onPageSelected(int arg0)
	{
	    Animation animation = null;
	    switch (arg0)
	    {
	    case 0:
		ImageNewJob.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_pressed));
		if (currIndex == 1)
		{
		    animation = new TranslateAnimation(one, 0, 0, 0);
		    ImageOldJob.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
		}
		else if (currIndex == 2)
		{
		    animation = new TranslateAnimation(two, 0, 0, 0);
		    ImageScan.setImageDrawable(getResources().getDrawable(R.drawable.tab_search_normal));
		}
		else if (currIndex == 3)
		{
		    animation = new TranslateAnimation(three, 0, 0, 0);
		    ImageSetting.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
		}
		break;
	    case 1:
		ImageOldJob.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_pressed));
		if (currIndex == 0)
		{
		    animation = new TranslateAnimation(zero, one, 0, 0);
		    ImageNewJob.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
		}
		else if (currIndex == 2)
		{
		    animation = new TranslateAnimation(two, one, 0, 0);
		    ImageScan.setImageDrawable(getResources().getDrawable(R.drawable.tab_search_normal));
		}
		else if (currIndex == 3)
		{
		    animation = new TranslateAnimation(three, one, 0, 0);
		    ImageSetting.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
		}
		break;
	    case 2:
		ImageScan.setImageDrawable(getResources().getDrawable(R.drawable.tab_search_press));
		if (currIndex == 0)
		{
		    animation = new TranslateAnimation(zero, two, 0, 0);
		    ImageNewJob.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
		}
		else if (currIndex == 1)
		{
		    animation = new TranslateAnimation(one, two, 0, 0);
		    ImageOldJob.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
		}
		else if (currIndex == 3)
		{
		    animation = new TranslateAnimation(three, two, 0, 0);
		    ImageSetting.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
		}
		break;
	    case 3:
		ImageSetting.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_pressed));
		if (currIndex == 0)
		{
		    animation = new TranslateAnimation(zero, three, 0, 0);
		    ImageNewJob.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
		}
		else if (currIndex == 1)
		{
		    animation = new TranslateAnimation(one, three, 0, 0);
		    ImageOldJob.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
		}
		else if (currIndex == 2)
		{
		    animation = new TranslateAnimation(two, three, 0, 0);
		    ImageScan.setImageDrawable(getResources().getDrawable(R.drawable.tab_search_normal));
		}
		break;
	    }
	    currIndex = arg0;
	    animation.setFillAfter(true);
	    animation.setDuration(150);
	    mTabImg.startAnimation(animation);
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{
	}

	@Override
	public void onPageScrollStateChanged(int arg0)
	{
	}
    }

    /*
     * (non-Javadoc) 如果是在主目录中点击返回键，那么后台运行
     * 
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @SuppressWarnings("deprecation")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
	// 返回按键，1.提示退出 2.如果存在popMenu,退出menu
	if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
	{
	    if (menu_display)
	    {
		menuWindow.dismiss();
		menu_display = false;
		return true;
	    }
	    else
	    {
		if ((System.currentTimeMillis() - exitTime) > 2000)
		{
		    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
		    exitTime = System.currentTimeMillis();
		}
		else
		{
		    BaseConst.updateExit(this, 0);
		    updateIsBack(1);
		    // 直接返回到桌面
		    Intent startMain = new Intent(Intent.ACTION_MAIN);
		    startMain.addCategory(Intent.CATEGORY_HOME);
		    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    startActivity(startMain);
		}
		return true;
	    }
	}
	else if (keyCode == KeyEvent.KEYCODE_MENU)
	{
	    // 菜单按键，弹出退出窗口
	    if (!menu_display)
	    {
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.main_menu, null);

		menuWindow = new PopupWindow(layout, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		menuWindow.showAtLocation(this.findViewById(R.id.mainjingu),
			Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		LinearLayout mCloseBtn = (LinearLayout) layout.findViewById(R.id.menu_close_btn);

		mCloseBtn.setOnClickListener(new View.OnClickListener()
		{
		    @Override
		    public void onClick(View arg0)
		    {
			Intent intent = new Intent();
			intent.setClass(MainActivityFrag.this, ExitActivity.class);
			startActivity(intent);
			menuWindow.dismiss();
		    }
		});
		menu_display = true;
	    }
	    else
	    {
		menuWindow.dismiss();
		menu_display = false;
	    }
	    return true;
	}
	return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy()
    {
	instance = null;
	super.onDestroy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onPause()
     * 当前Activity被其他Activity覆盖其上或被锁屏：系统会调用onPause方法，暂停当前Activity的执行。
     */
    @Override
    protected void onPause()
    {
	// 取消注册home键
	BaseConst.unregisterHomeKeyReceiver(this);
	updateIsBack(1);
	super.onPause();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onResume()
     * 当前Activity由被覆盖状态回到前台或解锁屏：系统会调用onResume方法，再次进入运行状态。
     */
    @Override
    protected void onResume()
    {
	// 检测是否有震动，停止震动
	if (BackGroundService.messageThread != null)
	{
	    BackGroundService.messageThread.interrupt();// 线程终端，同时激活线程
	    NewJobFragment.mHandler.sendEmptyMessage(0);
	}
	// 注册监听home键
	BaseConst.registerHomeKeyReceiver(this);
	updateIsBack(0);
	super.onResume();
    }

    /**
     * 更新状态和消息数目
     * 
     * @param i
     */
    public void updateIsBack(int i)
    {
	SharedPreferences settings = getSharedPreferences("setting", 0);
	SharedPreferences.Editor editor = settings.edit();
	editor.putInt("isBack", i);
	editor.putInt("nums", 0);
	editor.commit();
    }

    /* ========FragMent中的Click========== */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	if (requestCode == 1)
	{
	    switch (resultCode)
	    {
	    case RESULT_OK:
		Bundle bundle = data.getExtras();
		// 显示扫描到的内容
		Intent intent = new Intent();
		intent.setClass(MainActivityFrag.this, WaitingActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("str", "scan");
		intent.putExtras(bundle);
		startActivityForResult(intent, 2);
		break;
	    default:
		break;
	    }
	}
	else if (requestCode == 2)
	{
	    // 显示钢瓶档案
	    switch (resultCode)
	    {
	    case RESULT_OK:
		Bundle bundle = data.getExtras();
		@SuppressWarnings("static-access")
		ImageView igView = (ImageView) scanFragment.sView.findViewById(R.id.s_image_scan);
		igView.setVisibility(ImageView.GONE);
		@SuppressWarnings("static-access")
		TextView textView = (TextView) scanFragment.sView.findViewById(R.id.s_bottle_content);
		textView.setTextSize(22);
		textView.setText(bundle.getString("result"));
		textView.setBackgroundResource(R.anim.sharp);
		@SuppressWarnings("static-access")
		LinearLayout scanBody = (LinearLayout) scanFragment.sView.findViewById(R.id.scan_body);
		scanBody.setBackgroundResource(R.drawable.chat_bg_default);
		@SuppressWarnings("static-access")
		Button bscan = (Button) scanFragment.sView.findViewById(R.id.b_rep_scan);
		bscan.setVisibility(Button.VISIBLE);
		break;
	    case 0:
		// 网络出问题
		Toast.makeText(MainActivityFrag.this, R.string.login_net_fial, Toast.LENGTH_SHORT).show();
		break;
	    case 1:
		// 提交失败
		Toast.makeText(MainActivityFrag.this, R.string.scan_fail, Toast.LENGTH_SHORT).show();
		break;
	    case 2:
		// 正常返回
		break;
	    default:
		break;
	    }
	}
    }

    /**
     * 扫一扫
     * 
     * @param v
     */
    public void beginScan(View v)
    {
	Intent intent = new Intent();
	intent.setClass(MainActivityFrag.this, MipcaActivityCapture.class);
	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	startActivityForResult(intent, 1);
    }

    /**
     * 检查更新
     * 
     * @param v
     */
    public void CheckVersion(View v)
    {
	UpdateManager uManager = UpdateManager.getUpdateManager();
	uManager.checkAppUpdate(this, true);
    }

    /**
     * 到退出界面
     * 
     * @param v
     */
    public void ExitApp(View v)
    {
	Intent intent = new Intent();
	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	intent.setClass(this, ExitActivity.class);
	startActivity(intent);
    }

    /**
     * 显示下拉菜单
     * 
     * @param v
     */
    public void ShowMenu(View v)
    {
	Intent intent = new Intent(MainActivityFrag.this, TopMenuActivity.class);
	startActivity(intent);
    }

    /**
     * 服务器设置
     * 
     * @param v
     */
    public void ServerSetting(View v)
    {
	Toast.makeText(this, getResources().getString(R.string.can_not_user), Toast.LENGTH_SHORT).show();
    }

    /**
     * 系统设置
     * 
     * @param v
     */
    public void SystemSetting(View v)
    {
	Intent intent = new Intent(MainActivityFrag.this, SysSettingActivity.class);
	startActivity(intent);
    }

    /**
     * 清空已办工单
     * 
     * @param v
     */
    public void TruncateList(View v)
    {
	DBJobInfoDao dInfoDao = new DBJobInfoDao(this);
	dInfoDao.delall(BaseConst.getUserFromApplication(this).getUsername());
	dInfoDao.closeDB();
	Toast.makeText(this, getResources().getString(R.string.sys_clean_job_list), Toast.LENGTH_SHORT).show();
    }

    /**
     * 关于
     * 
     * @param v
     */
    public void aboutApp(View v)
    {
	Intent intent = new Intent(MainActivityFrag.this, AboutAppActivity.class);
	startActivity(intent);
    }

    /**
     * 点击设置网络
     * 
     * @param v
     */
    public void SetNet(View v)
    {
	Intent intent = null;
	// 判断手机系统的版本 即API大于10 就是3.0或以上版本
	if (android.os.Build.VERSION.SDK_INT > 10)
	{
	    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
	}
	else
	{
	    intent = new Intent();
	    ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
	    intent.setComponent(component);
	    intent.setAction("android.intent.action.VIEW");
	}
	startActivity(intent);
    }
}
