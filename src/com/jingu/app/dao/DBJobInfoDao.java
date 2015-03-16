package com.jingu.app.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jingu.app.bean.JobBean;
import com.jingu.app.util.BaseConst;
import com.jingu.app.util.DatabaseHelper;

public class DBJobInfoDao
{
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public DBJobInfoDao(Context context)
    {
	helper = new DatabaseHelper(context);
	db = helper.getWritableDatabase();
    }

    /**
     * 新增工单
     * 
     * @param user
     */
    public void add(JobBean job)
    {
	if (job != null)
	{
	    db.execSQL(
		    "INSERT INTO " + DatabaseHelper.JOB_TABLE
			    + "('username','jobid','jobtitle','jobcontent','telnum','jobdate') VALUES(?,?,?,?,?,?)",
		    new Object[] { job.getUsername(), job.getJobId(), job.getJobTitle(), job.getJobContent(),
			    job.getTelNum(), job.getJobDate() });
	}
    }

    /**
     * 设置保存保存已完成工单个数时候的更新操作
     * 
     * @param job
     */
    public void update(String jobid, String note, int nums)
    {
	ContentValues cv = new ContentValues();
	cv.put("jobreply", note);
	cv.put("confirmdate", BaseConst.getDate2(new Date()));
	cv.put("jobstate", "O");
	if (BaseConst.TAG_CANCEL.equals(note)&&note!="")
	{
	    cv.put("jobtype", "Q");
	}
	db.update(DatabaseHelper.JOB_TABLE, cv, "jobid = ?", new String[] { jobid });
	// 检测下，删除那些多余的
	delRecord(nums);
    }

    /**
     * 更新工单的状态
     * 
     * @param jobid
     */
    public void updateState(String jobid, String state)
    {
	ContentValues cv = new ContentValues();
	if ("Q".equals(state) || state == "Q")
	{
	    cv.put("jobreply", "取消");
	    cv.put("jobstate", "O");
	    cv.put("confirmdate", BaseConst.getDate2(new Date()));
	}
	else
	{
	    cv.put("jobstate", state);
	}
	db.update(DatabaseHelper.JOB_TABLE, cv, "jobid = ?", new String[] { jobid });
    }

    /**
     * 更新工单的类型，催办 or 取消
     * 
     * @param jobid
     * @param type
     */
    public void updateType(String jobid, String type)
    {
	ContentValues cv = new ContentValues();
	cv.put("jobtype", type);
	db.update(DatabaseHelper.JOB_TABLE, cv, "jobid = ?", new String[] { jobid });
    }

    /**
     * 根据工单id查询工单
     * 
     * @param jobid
     * @return
     */
    public JobBean query(String jobid)
    {
	StringBuffer sql = new StringBuffer();
	sql.append("select * from ").append(DatabaseHelper.JOB_TABLE).append(" where jobid=?");
	Cursor c = db.rawQuery(sql.toString(), new String[] { jobid });
	if (c.moveToFirst())
	{
	    JobBean job = new JobBean();
	    job.setId(c.getInt(c.getColumnIndex("id")));
	    job.setJobId(jobid);
	    job.setJobTitle(c.getString(c.getColumnIndex("jobtitle")));
	    job.setJobContent(c.getString(c.getColumnIndex("jobcontent")));
	    job.setJobDate(c.getString(c.getColumnIndex("jobdate")));
	    job.setJobReply(c.getString(c.getColumnIndex("jobreply")));
	    job.setJobState(c.getString(c.getColumnIndex("jobstate")));
	    job.setConfirmdate(c.getString(c.getColumnIndex("confirmdate")));
	    job.setTelNum(c.getString(c.getColumnIndex("telnum")));
	    job.setJobType(c.getString(c.getColumnIndex("jobtype")));
	    c.close();
	    return job;
	}
	c.close();
	return null;
    }

    /**
     * 根据状态查询对应的工单 1 新工单 2 已完成工单
     * 
     * @param mark
     * @return
     */
    public synchronized List<JobBean> query(int mark, String username)
    {
	ArrayList<JobBean> jobs = new ArrayList<JobBean>();
	StringBuffer sql = new StringBuffer();
	if (mark == 1)
	{
	    // 未完成
	    sql.append("select * from ").append(DatabaseHelper.JOB_TABLE)
		    .append(" where username=? and jobstate<>'O' order by jobdate desc");
	}
	else
	{
	    // 已完成
	    sql.append("select * from ").append(DatabaseHelper.JOB_TABLE)
		    .append(" where username=? and  jobstate='O'  order by jobdate desc");
	}
	Cursor c = db.rawQuery(sql.toString(), new String[] { username });
	while (c.moveToNext())
	{
	    JobBean job = new JobBean();
	    job.setId(c.getInt(c.getColumnIndex("id")));
	    job.setJobId(c.getString(c.getColumnIndex("jobid")));
	    job.setJobTitle(c.getString(c.getColumnIndex("jobtitle")));
	    job.setJobContent(c.getString(c.getColumnIndex("jobcontent")));
	    job.setJobDate(c.getString(c.getColumnIndex("jobdate")));
	    job.setJobReply(c.getString(c.getColumnIndex("jobreply")));
	    job.setJobState(c.getString(c.getColumnIndex("jobstate")));
	    job.setConfirmdate(c.getString(c.getColumnIndex("confirmdate")));
	    job.setTelNum(c.getString(c.getColumnIndex("telnum")));
	    job.setJobType(c.getString(c.getColumnIndex("jobtype")));
	    jobs.add(job);
	}
	c.close();
	return jobs;
    }

    /**
     * 游标方式返回
     * 
     * @param mark
     * @return
     */
    public Cursor queryCursor(int mark, String username)
    {
	StringBuffer sql = new StringBuffer();
	if (mark == 1)
	{
	    sql.append("select * from ").append(DatabaseHelper.JOB_TABLE)
		    .append(" where username=? and   jobstate<>'O'  order by jobdate desc");
	}
	else
	{
	    sql.append("select * from ").append(DatabaseHelper.JOB_TABLE)
		    .append(" where username=? and   jobstate='O'  order by jobdate desc");
	}
	Cursor c = db.rawQuery(sql.toString(), new String[] { username });
	return c;
    }

    /**
     * 清空表，根据用户清空表
     */
    public void delall(String user)
    {
	StringBuffer sql = new StringBuffer();
	sql.append("delete from ").append(DatabaseHelper.JOB_TABLE).append(" where jobstate='O' and username='")
		.append(user).append("';");
	db.execSQL(sql.toString());
    }

    /**
     * 获取记录条数
     * 
     * @return
     */
    public int getRecordNum()
    {
	StringBuffer sql = new StringBuffer();
	sql.append("select count(*) as num from ").append(DatabaseHelper.JOB_TABLE);
	sql.append(" where jobstate='O'");
	Cursor cursor = db.rawQuery(sql.toString(), null);
	if (cursor.moveToFirst())
	{
	    int count = cursor.getInt(cursor.getColumnIndex("num"));
	    cursor.close();
	    return count;
	}
	cursor.close();
	return 0;
    }

    /**
     * 删除nums个记录，从时间最久的开始删除 删除那些已完成的工单
     * 
     * @param nums
     */
    public void delRecord(int nums)
    {
	int n = getRecordNum();
	if (n > nums)
	{
	    // 删除掉那些数据
	    n = n - nums;
	    StringBuffer sql = new StringBuffer();
	    sql.append("delete from ").append(DatabaseHelper.JOB_TABLE).append(" where ");
	    sql.append("id in(select id from ").append(DatabaseHelper.JOB_TABLE)
		    .append(" where jobstate='O' order by jobdate asc limit 0,").append(n).append(")");
	    db.execSQL(sql.toString());
	}
    }

    public void closeDB()
    {
	// 释放数据库资源
	db.close();
    }
}
