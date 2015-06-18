package com.jingu.app.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.support.v4.app.FragmentActivity;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.jingu.app.R;
import com.jingu.app.bean.JobBean;
import com.jingu.app.dao.DBJobInfoDao;

/**
 * @author Administrator 针对新工单数据的ListView
 */
public class MyListViewUtil
{
    private FragmentActivity context;
    public static String TAG = "JinGu";

    public MyListViewUtil(FragmentActivity context)
    {
	this.context = context;
    }

    /**
     * 新工单的listview分组显示的adapter
     * 
     * @return
     */
    @SuppressWarnings("deprecation")
    public MyListViewAdapter getAdapterForNewJob()
    {
	List<JobBean> jobList = queryJob(1);
	int msgNum = jobList.size();
	ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	if (msgNum > 0)
	{
	    // 更新消息记录,更新消息显示个数
	    TextView t1 = (TextView) context.findViewById(R.id.msg_tag);
	    t1.setText(Integer.toString(msgNum));
	    t1.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.tab_unread_bg));
	    listItem = getNewJobListAdapter(jobList);
	}
	else
	{
	    TextView t1 = (TextView) context.findViewById(R.id.msg_tag);
	    t1.setText("");
	    t1.setBackgroundDrawable(null);
	    // 不存在新工单
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    map.put("image", context.getResources().getDrawable(R.drawable.nomsg_waring));
	    map.put("content", context.getString(R.string.no_msg_waring));
	    listItem.add(map);
	}
	MyListViewAdapter mAdapter = new MyListViewAdapter(listItem, context);
	return mAdapter;
    }

    /**
     * 已完成工单的listview分组显示的adapter
     * 
     * @return
     */
    public MyListViewAdapter getAdapterForDoneJob()
    {
	List<JobBean> jobList = queryJob(2);
	int msgNum = jobList.size();
	ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	if (msgNum > 0)
	{
	    listItem = getDoneJobListAdapter(jobList);
	}
	else
	{
	    // 不存在已办工单
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    map.put("image", context.getResources().getDrawable(R.drawable.nomsg_waring));
	    map.put("content", context.getString(R.string.no_done_job_waring));
	    listItem.add(map);
	}
	MyListViewAdapter mAdapter = new MyListViewAdapter(listItem, context);
	return mAdapter;
    }

    /**
     * 为待办件初始化adapter数据
     * 
     * @param jobList
     * @return
     */
    public ArrayList<HashMap<String, Object>> getNewJobListAdapter(List<JobBean> jobList)
    {
	ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	HashMap<String, Object> map1 = new HashMap<String, Object>();
	map1.put("tag_content", "新增");
	HashMap<String, Object> map2 = new HashMap<String, Object>();
	map2.put("tag_content", "催办");
	HashMap<String, Object> map3 = new HashMap<String, Object>();
	map3.put("tag_content", "取消");

	int mark = 0;
	// 取消单(jobtype=Q)
	list.add(map3);
	for (int i = 0; i < jobList.size(); i++)
	{
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    JobBean job = jobList.get(i);
	    String jobType = job.getJobType();
	    if ("Q".equals(jobType))
	    {
		if ("R".equals(job.getJobState()))
		{
		    map.put("image", context.getResources().getDrawable(R.drawable.old_done_job));
		}
		else
		{
		    map.put("image", context.getResources().getDrawable(R.drawable.new_wait_job));
		}
		map.put("job_id", job.getJobId());
		map.put("jobtitle", job.getJobTitle());
		map.put("jobdate", BaseConst.getDate(job.getJobDate()));
		map.put("jobrealdate", job.getJobDate());
		map.put("jobcontent", job.getJobContent());
		map.put("jobstate", job.getJobState());
		map.put("telnum", job.getTelNum());
		map.put("jobtype", job.getJobType());
		list.add(map);
		mark++;
	    }
	}
	if (mark == 0)
	{
	    list.remove(map3);
	}
	mark = 0;

	// 催办单(jobtype=C)
	list.add(map2);
	for (int i = 0; i < jobList.size(); i++)
	{
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    JobBean job = jobList.get(i);
	    String jobType = job.getJobType();
	    if ("C".equals(jobType))
	    {
		if ("R".equals(job.getJobState()))
		{
		    map.put("image", context.getResources().getDrawable(R.drawable.old_done_job));
		}
		else
		{
		    map.put("image", context.getResources().getDrawable(R.drawable.new_wait_job));
		}
		map.put("job_id", job.getJobId());
		map.put("jobtitle", job.getJobTitle());
		map.put("jobdate", BaseConst.getDate(job.getJobDate()));
		map.put("jobrealdate", job.getJobDate());
		map.put("jobcontent", job.getJobContent());
		map.put("jobstate", job.getJobState());
		map.put("telnum", job.getTelNum());
		map.put("jobtype", job.getJobType());
		list.add(map);
		mark++;
	    }
	}
	if (mark == 0)
	{
	    list.remove(map2);
	}
	mark = 0;

	// 新工单(jobtype=N)
	list.add(map1);
	for (int i = 0; i < jobList.size(); i++)
	{
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    JobBean job = jobList.get(i);
	    String jobType = job.getJobType();
	    if ("N".equals(jobType))
	    {
		if ("R".equals(job.getJobState()))
		{
		    map.put("image", context.getResources().getDrawable(R.drawable.old_done_job));
		}
		else
		{
		    map.put("image", context.getResources().getDrawable(R.drawable.new_wait_job));
		}
		map.put("job_id", job.getJobId());
		map.put("jobtitle", job.getJobTitle());
		map.put("jobdate", BaseConst.getDate(job.getJobDate()));
		map.put("jobrealdate", job.getJobDate());
		map.put("jobcontent", job.getJobContent());
		map.put("jobstate", job.getJobState());
		map.put("telnum", job.getTelNum());
		map.put("jobtype", job.getJobType());
		list.add(map);
		mark++;
	    }
	}
	if (mark == 0)
	{
	    list.remove(map1);
	}
	return list;
    }

    /**
     * 为已办件初始化adapter数据
     * 
     * @param jobList
     * @return
     */
    public ArrayList<HashMap<String, Object>> getDoneJobListAdapter(List<JobBean> jobList)
    {
	ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	for (int i = 0; i < jobList.size(); i++)
	{
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    JobBean job = jobList.get(i);
	    map.put("image", context.getResources().getDrawable(R.drawable.job_done_pic));
	    map.put("job_id", job.getJobId());
	    map.put("jobtitle", job.getJobTitle());
	    map.put("jobdate", BaseConst.getDate(job.getJobDate()));
	    map.put("jobrealdate", job.getJobDate());
	    map.put("jobcontent", job.getJobContent());
	    map.put("jobstate", job.getJobState());
	    map.put("jobreply", job.getJobReply());
	    map.put("jobtype", job.getJobType());
	    map.put("j_comfirTime", job.getConfirmdate());
	    map.put("telnum", job.getTelNum());
	    list.add(map);
	}
	return list;
    }

    public static JobBean getJob(HashMap<String, Object> item)
    {
	JobBean newJobItem = new JobBean();
	newJobItem.setJobId(item.get("job_id").toString());
	newJobItem.setJobTitle(item.get("jobtitle").toString());
	newJobItem.setJobContent(item.get("jobcontent").toString());
	newJobItem.setJobDate(item.get("jobrealdate").toString());
	newJobItem.setJobState(item.get("jobstate").toString());
	newJobItem.setTelNum(item.get("telnum").toString());
	newJobItem.setJobType(item.get("jobtype").toString());
	return newJobItem;
    }

    public static JobBean getDoneJob(HashMap<String, Object> item)
    {
	JobBean JobItem = new JobBean();
	JobItem.setJobId(item.get("job_id").toString());
	JobItem.setJobTitle(item.get("jobtitle").toString());
	JobItem.setJobContent(item.get("jobcontent").toString());
	JobItem.setJobDate(item.get("jobrealdate").toString());
	JobItem.setJobReply(item.get("jobreply").toString());
	JobItem.setConfirmdate(item.get("j_comfirTime").toString());
	JobItem.setTelNum(item.get("telnum").toString());
	return JobItem;
    }

    /**
     * 查询所有新的工单
     * 
     * @return
     */
    public List<JobBean> queryJob(int i)
    {
	DBJobInfoDao dInfoDao = new DBJobInfoDao(context);
	String un = BaseConst.getUserFromApplication(context).getUsername();
	if ("".equals(un) || un == null)
	{
	    un = BaseConst.getUser(context).getUsername();
	}
	// 根据当前用户查询他所对应的工单信息
	List<JobBean> listJobBeans = dInfoDao.query(i, un);
	dInfoDao.closeDB();
	return listJobBeans;
    }

    /**
     * 旧方法，获取已完成工单的adapter信息 原来已经完成工单的adapter的数据获取，已经废弃，2015-3-11
     * 
     * @return
     */
    public SimpleAdapter getAdapterForDoneJob_Remove()
    {
	ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	List<JobBean> jobList = queryJob(2);
	int msgNum = jobList.size();
	if (msgNum > 0)
	{
	    // 存在完成的工单
	    for (int i = 0; i < msgNum; i++)
	    {
		HashMap<String, Object> map = new HashMap<String, Object>();
		JobBean job = jobList.get(i);
		map.put("image", R.drawable.job_done_pic);
		map.put("job_id", job.getJobId());
		map.put("jobtitle", job.getJobTitle());
		map.put("jobdate", BaseConst.getDate(job.getJobDate()));
		map.put("jobrealdate", job.getJobDate());
		map.put("jobcontent", job.getJobContent());
		map.put("jobstate", job.getJobState());
		map.put("jobreply", job.getJobReply());
		map.put("j_comfirTime", job.getConfirmdate());
		map.put("telnum", job.getTelNum());
		listItem.add(map);
	    }
	    String[] strFrom = new String[] { "image", "job_id", "jobtitle", "jobdate", "jobrealdate", "jobcontent",
		    "jobstate", "jobreply", "telnum" };
	    int[] intTo = new int[] { R.id.image, R.id.job_id, R.id.jobtitle, R.id.jobdate, R.id.jobrealdate,
		    R.id.jobcontent, R.id.jobstate, R.id.jobreply, R.id.telnum };
	    SimpleAdapter listItemAdapter = new SimpleAdapter(context, listItem, R.anim.list_item, strFrom, intTo);
	    return listItemAdapter;
	}
	else
	{
	    // 不存在新工单
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    map.put("image", R.drawable.nomsg_waring);
	    map.put("content", context.getString(R.string.no_done_job_waring));
	    listItem.add(map);
	    // 生成适配器的Item和动态数组对应的元素
	    SimpleAdapter listItemAdapter = new SimpleAdapter(context, listItem, R.anim.list_item_nomsg, new String[] {
		    "image", "content" }, new int[] { R.id.image, R.id.content });
	    return listItemAdapter;
	}
    }

    /**
     * 旧方法，用于为新工单及已完成工单的ListView提供Adapter，已废弃，2015-3-9
     * 
     * @param mark
     * @return
     */
    @SuppressWarnings("deprecation")
    public SimpleAdapter getAdapter_Remove(int mark)
    {
	ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	List<JobBean> jobList = queryJob(mark);
	int msgNum = jobList.size();
	if (mark == 1)
	{// 新工单
	    if (msgNum > 0)
	    {
		// 更新消息记录,更新消息显示个数
		TextView t1 = (TextView) context.findViewById(R.id.msg_tag);
		t1.setText(Integer.toString(msgNum));
		t1.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.tab_unread_bg));
		// 存在新的工单
		for (int i = 0; i < msgNum; i++)
		{
		    HashMap<String, Object> map = new HashMap<String, Object>();
		    JobBean job = jobList.get(i);
		    map.put("image", R.drawable.new_wait_job);
		    map.put("job_id", job.getJobId());
		    map.put("jobtitle", job.getJobTitle());
		    map.put("jobdate", BaseConst.getDate(job.getJobDate()));
		    map.put("jobrealdate", job.getJobDate());
		    map.put("jobcontent", job.getJobContent());
		    map.put("jobstate", job.getJobState());
		    map.put("telnum", job.getTelNum());
		    listItem.add(map);
		}
		String[] strFrom = new String[] { "image", "job_id", "jobtitle", "jobdate", "jobrealdate",
			"jobcontent", "jobstate", "telnum" };
		int[] intTo = new int[] { R.id.image, R.id.job_id, R.id.jobtitle, R.id.jobdate, R.id.jobrealdate,
			R.id.jobcontent, R.id.jobstate, R.id.telnum };
		SimpleAdapter listItemAdapter = new SimpleAdapter(context, listItem, R.anim.list_item, strFrom, intTo);
		return listItemAdapter;
	    }
	    else
	    {
		TextView t1 = (TextView) context.findViewById(R.id.msg_tag);
		t1.setText("");
		t1.setBackgroundDrawable(null);
		// 不存在新工单
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("image", R.drawable.nomsg_waring);
		map.put("content", context.getString(R.string.no_msg_waring));
		listItem.add(map);
		// 生成适配器的Item和动态数组对应的元素
		SimpleAdapter listItemAdapter = new SimpleAdapter(context, listItem, R.anim.list_item_nomsg,
			new String[] { "image", "content" }, new int[] { R.id.image, R.id.content });
		return listItemAdapter;
	    }
	}
	else
	{// 已完成工单
	    if (msgNum > 0)
	    {
		// 存在新的工单
		for (int i = 0; i < msgNum; i++)
		{
		    HashMap<String, Object> map = new HashMap<String, Object>();
		    JobBean job = jobList.get(i);
		    map.put("image", R.drawable.old_done_job);
		    map.put("job_id", job.getJobId());
		    map.put("jobtitle", job.getJobTitle());
		    map.put("jobdate", BaseConst.getDate(job.getJobDate()));
		    map.put("jobrealdate", job.getJobDate());
		    map.put("jobcontent", job.getJobContent());
		    map.put("jobstate", job.getJobState());
		    map.put("jobreply", job.getJobReply());
		    map.put("j_comfirTime", job.getConfirmdate());
		    map.put("telnum", job.getTelNum());
		    listItem.add(map);
		}
		String[] strFrom = new String[] { "image", "job_id", "jobtitle", "jobdate", "jobrealdate",
			"jobcontent", "jobstate", "jobreply", "telnum" };
		int[] intTo = new int[] { R.id.image, R.id.job_id, R.id.jobtitle, R.id.jobdate, R.id.jobrealdate,
			R.id.jobcontent, R.id.jobstate, R.id.jobreply, R.id.telnum };
		SimpleAdapter listItemAdapter = new SimpleAdapter(context, listItem, R.anim.list_item, strFrom, intTo);
		return listItemAdapter;
	    }
	    else
	    {
		// 不存在新工单
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("image", R.drawable.nomsg_waring);
		map.put("content", context.getString(R.string.no_done_job_waring));
		listItem.add(map);
		// 生成适配器的Item和动态数组对应的元素
		SimpleAdapter listItemAdapter = new SimpleAdapter(context, listItem, R.anim.list_item_nomsg,
			new String[] { "image", "content" }, new int[] { R.id.image, R.id.content });
		return listItemAdapter;
	    }
	}
    }

}
