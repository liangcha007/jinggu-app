package com.jingu.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jingu.app.R;
import com.jingu.app.util.MyApplication;

public class SettingFragment extends Fragment
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	MyApplication.getInstance().addActivity(this.getActivity());
	super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
	return inflater.inflate(R.layout.main_tab_settings, container, false);
    }
}
