package com.jingu.app.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jingu.app.R;

public class MyListViewAdapter extends BaseAdapter
{
    private FragmentActivity context;
    public ArrayList<HashMap<String, Object>> listItem;
    public List<String> groupkey;

    public MyListViewAdapter(ArrayList<HashMap<String, Object>> listItem, FragmentActivity ct)
    {
	super();
	this.listItem = listItem;
	this.context = ct;
	groupkey = new ArrayList<String>();
	groupkey.add(BaseConst.TAG_NEW);
	groupkey.add(BaseConst.TAG_CUIBAN);
	groupkey.add(BaseConst.TAG_CANCEL);
    }

    @Override
    public int getCount()
    {
	return listItem.size();
    }

    @Override
    public Object getItem(int position)
    {
	return listItem.get(position);
    }

    @Override
    public long getItemId(int position)
    {
	return position;
    }

    @Override
    public boolean isEnabled(int position)
    {
	if (groupkey.contains(getItem(position))) { return false; }
	return super.isEnabled(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
	View view = convertView;
	@SuppressWarnings("unchecked")
	HashMap<String, Object> map = (HashMap<String, Object>) getItem(position);
	if (groupkey.contains(map.get("tag_content")) && !"".equals(map.get("tag_content"))
		&& map.get("tag_content") != null)
	{
	    // 标题分类
	    view = LayoutInflater.from(context).inflate(R.anim.list_item_tag, null);
	    TextView text = (TextView) view.findViewById(R.id.tag_content);
	    text.setText(map.get("tag_content").toString());
	}
	else if (!"".equals(map.get("content")) && map.get("content") != null)
	{
	    // 没有工单，只显示一条没有工单的提示
	    view = LayoutInflater.from(context).inflate(R.anim.list_item_nomsg, null);
	    ImageView v1 = (ImageView) view.findViewById(R.id.image);
	    v1.setImageDrawable((Drawable) map.get("image"));
	    TextView text = (TextView) view.findViewById(R.id.content);
	    text.setText(map.get("content").toString());
	}
	else
	{
	    // 分类下面的工单显示
	    view = LayoutInflater.from(context).inflate(R.anim.list_item, null);

	    // 设置图片
	    ImageView v1 = (ImageView) view.findViewById(R.id.image);
	    v1.setImageDrawable((Drawable) map.get("image"));
	    // 设置jobid
	    TextView t1 = (TextView) view.findViewById(R.id.job_id);
	    t1.setText(map.get("job_id").toString());
	    TextView t2 = (TextView) view.findViewById(R.id.jobtitle);
	    t2.setText(map.get("jobtitle").toString());
	    TextView t3 = (TextView) view.findViewById(R.id.jobdate);
	    t3.setText(map.get("jobdate").toString());
	    TextView t4 = (TextView) view.findViewById(R.id.jobrealdate);
	    t4.setText(map.get("jobrealdate").toString());
	    TextView t5 = (TextView) view.findViewById(R.id.jobcontent);
	    t5.setText(map.get("jobcontent").toString());
	    TextView t6 = (TextView) view.findViewById(R.id.jobstate);
	    t6.setText(map.get("jobstate").toString());
	    TextView t7 = (TextView) view.findViewById(R.id.telnum);
	    t7.setText(map.get("telnum").toString());
	    TextView t8 = (TextView) view.findViewById(R.id.jobtype);
	    t8.setText(map.get("jobtype").toString());

	    if (!"".equals(map.get("j_comfirTime")) && map.get("j_comfirTime") != null)
	    {
		TextView t9 = (TextView) view.findViewById(R.id.jobreply);
		t9.setText(map.get("jobreply").toString());
		// 如果回复时间不为空，说明是已办件
		if ("Q".equals(map.get("jobtype")) && map.get("jobtype") != null)
		{
		    t2.setTextColor(context.getResources().getColor(R.color.red));
		    t3.setTextColor(context.getResources().getColor(R.color.red));
		    t5.setTextColor(context.getResources().getColor(R.color.red));
		}
	    }
	}
	return view;
    }
}
