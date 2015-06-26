package com.jingu.app.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import com.jingu.app.R;

public class BottleMenu extends PopMenu
{
    public BottleMenu(Context context)
    {
	super(context);
    }

    @Override
    protected ListView findListView(View view)
    {
	return (ListView) view.findViewById(R.id.menu_listview);
    }

    @Override
    protected View onCreateView(Context context)
    {
	View view = LayoutInflater.from(context).inflate(R.anim.popmeun, null);
	return view;
    }

    @Override
    protected ArrayAdapter<Item> onCreateAdapter(Context context, ArrayList<Item> items)
    {
	return new ArrayAdapter<Item>(context, R.anim.pop_menu_padding, items);
    }
}
