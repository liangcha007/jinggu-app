package com.jingu.app.util;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

/**
 * @author zhouc52 重新Fragment的Adapter
 */
public class MyFragmentAdapter extends FragmentStatePagerAdapter
{
    private List<Fragment> fragments;
    private FragmentManager fm;

    public MyFragmentAdapter(FragmentManager fm, List<Fragment> fragments)
    {
	super(fm);
	this.fm = fm;
	this.fragments = fragments;
    }

    public void setFragments(List<Fragment> fragments)
    {
	if (this.fragments != null)
	{
	    FragmentTransaction ft = fm.beginTransaction();
	    for (Fragment f : this.fragments)
	    {
		ft.remove(f);
	    }
	    ft.commit();
	    ft = null;
	    fm.executePendingTransactions();
	}
	this.fragments = fragments;
	notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object)
    {
	return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int arg0)
    {
	return fragments.get(arg0);
    }

    @Override
    public int getCount()
    {
	return fragments.size();
    }
}
