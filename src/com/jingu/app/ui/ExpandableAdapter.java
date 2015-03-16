package com.jingu.app.ui;

import java.util.List;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * @author zhouc52 二级树形菜单
 */
public class ExpandableAdapter extends BaseExpandableListAdapter
{

    private List<String> groupArray;
    private List<List<String>> childArray;
    private Activity activity;

    public ExpandableAdapter(Activity a, List<String> groupArray, List<List<String>> childArray)
    {
	activity = a;
	this.groupArray = groupArray;
	this.childArray = childArray;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
	// TODO Auto-generated method stub
	return childArray.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
	// TODO Auto-generated method stub
	return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
	    ViewGroup parent)
    {
	// TODO Auto-generated method stub
	String string = childArray.get(groupPosition).get(childPosition);
	return getGenericView(string);
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
	// TODO Auto-generated method stub
	return childArray.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
	// TODO Auto-generated method stub
	return groupArray.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
	// TODO Auto-generated method stub
	return groupArray.size();
    }

    @Override
    public long getGroupId(int groupPosition)
    {
	// TODO Auto-generated method stub
	return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
	// TODO Auto-generated method stub
	String string = groupArray.get(groupPosition);
	return getGenericView(string);
    }

    @Override
    public boolean hasStableIds()
    {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
	// TODO Auto-generated method stub
	return true;
    }

    /**************************************** 以下为自定义方法 *********************************************/
    /**
     * Children 's View
     * 
     * @param string
     * @return
     */
    public TextView getGenericView(String string)
    {
	@SuppressWarnings("deprecation")
	AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 64);
	TextView text = new TextView(activity);
	text.setLayoutParams(layoutParams);
	// Center the text vertically
	text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
	// Set the text starting position
	text.setPadding(36, 0, 0, 0);
	text.setText(string);
	return text;
    }
}