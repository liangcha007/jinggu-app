package com.jingu.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jingu.app.R;
import com.jingu.app.util.MyApplication;

public class ScanFragment extends Fragment
{
    public static View sView = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	MyApplication.getInstance().addActivity(this.getActivity());
	super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
	View scanView = inflater.inflate(R.layout.main_tab_scan, container, false);
	sView = scanView;
	return scanView;
    }
}
