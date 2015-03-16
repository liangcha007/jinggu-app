package com.jingu.app.main.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.jingu.app.R;

public class MainActivity extends Activity
{
    private ViewPager mTabPager;
    private ImageView mTabImg;
    private ImageView mTab1, mTab2, mTab3, mTab4;
    private int currIndex = 0;
    private int zero = 0;
    private int one;
    private int two;
    private int three;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main_jingu);
	initTab();
    }

    public void initTab()
    {
	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

	mTabPager = (ViewPager) findViewById(R.id.tabpager);
	mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());

	mTab1 = (ImageView) findViewById(R.id.img_weixin);
	mTab2 = (ImageView) findViewById(R.id.img_address);
	mTab3 = (ImageView) findViewById(R.id.img_scan);
	mTab4 = (ImageView) findViewById(R.id.img_settings);
	mTabImg = (ImageView) findViewById(R.id.img_tab_now);

	mTab1.setOnClickListener(new MyOnClickListener(0));
	mTab2.setOnClickListener(new MyOnClickListener(1));
	mTab3.setOnClickListener(new MyOnClickListener(2));
	mTab4.setOnClickListener(new MyOnClickListener(3));

	Display currDisplay = getWindowManager().getDefaultDisplay();
	@SuppressWarnings("deprecation")
	int displayWidth = currDisplay.getWidth();
	one = displayWidth / 4;
	two = one * 2;
	three = one * 3;

	LayoutInflater mLi = LayoutInflater.from(this);
	View view1 = mLi.inflate(R.layout.main_tab_newjob, null);
	View view2 = mLi.inflate(R.layout.main_tab_donejob, null);
	View view3 = mLi.inflate(R.layout.main_tab_scan, null);
	View view4 = mLi.inflate(R.layout.main_tab_settings, null);

	final ArrayList<View> views = new ArrayList<View>();
	views.add(view1);
	views.add(view2);
	views.add(view3);
	views.add(view4);

	PagerAdapter mPagerAdapter = new PagerAdapter()
	{

	    @Override
	    public boolean isViewFromObject(View arg0, Object arg1)
	    {
		return arg0 == arg1;
	    }

	    @Override
	    public int getCount()
	    {
		return views.size();
	    }

	    @Override
	    public void destroyItem(View container, int position, Object object)
	    {
		((ViewPager) container).removeView(views.get(position));
	    }

	    @Override
	    public Object instantiateItem(View container, int position)
	    {
		((ViewPager) container).addView(views.get(position));
		return views.get(position);
	    }
	};
	mTabPager.setAdapter(mPagerAdapter);
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
	    mTabPager.setCurrentItem(index);
	}
    };

    public class MyOnPageChangeListener implements OnPageChangeListener
    {
	@Override
	public void onPageSelected(int arg0)
	{
	    Animation animation = null;
	    switch (arg0)
	    {
	    case 0:
		mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_pressed));
		if (currIndex == 1)
		{
		    animation = new TranslateAnimation(one, 0, 0, 0);
		    mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
		}
		else if (currIndex == 2)
		{
		    animation = new TranslateAnimation(two, 0, 0, 0);
		    mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_search_normal));
		}
		else if (currIndex == 3)
		{
		    animation = new TranslateAnimation(three, 0, 0, 0);
		    mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
		}
		break;
	    case 1:
		mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_pressed));
		if (currIndex == 0)
		{
		    animation = new TranslateAnimation(zero, one, 0, 0);
		    mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
		}
		else if (currIndex == 2)
		{
		    animation = new TranslateAnimation(two, one, 0, 0);
		    mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_search_normal));
		}
		else if (currIndex == 3)
		{
		    animation = new TranslateAnimation(three, one, 0, 0);
		    mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
		}
		break;
	    case 2:
		mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_search_press));
		if (currIndex == 0)
		{
		    animation = new TranslateAnimation(zero, two, 0, 0);
		    mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
		}
		else if (currIndex == 1)
		{
		    animation = new TranslateAnimation(one, two, 0, 0);
		    mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
		}
		else if (currIndex == 3)
		{
		    animation = new TranslateAnimation(three, two, 0, 0);
		    mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
		}
		break;
	    case 3:
		mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_pressed));
		if (currIndex == 0)
		{
		    animation = new TranslateAnimation(zero, three, 0, 0);
		    mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
		}
		else if (currIndex == 1)
		{
		    animation = new TranslateAnimation(one, three, 0, 0);
		    mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
		}
		else if (currIndex == 2)
		{
		    animation = new TranslateAnimation(two, three, 0, 0);
		    mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_search_normal));
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

}
