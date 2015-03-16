package com.jingu.app.test;

import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;

import com.jingu.app.bean.JobBean;
import com.jingu.app.dao.DBJobInfoDao;
import com.jingu.app.util.BaseConst;

public class JobBeanDaoTest extends AndroidTestCase
{
    static String TAG = "JUINT_TAG";

    public void testAdd()
    {
	JobBean job = new JobBean("5", "5555", "5555", "8001", "18602031294", "2014-2-12 12:23:11");
	DBJobInfoDao jobDao = new DBJobInfoDao(getContext());
	jobDao.add(job);
	jobDao.closeDB();
    }

    public void testUpdate()
    {
	DBJobInfoDao jobDao = new DBJobInfoDao(getContext());
	JobBean job1 = jobDao.query("1");
	jobDao.updateState(job1.getJobId(), "N");
	jobDao.closeDB();
    }

    public void testQueryJobList()
    {
	DBJobInfoDao jobDao = new DBJobInfoDao(getContext());
	List<JobBean> joblist = jobDao.query(1, BaseConst.username);
	for (int i = 0; i < joblist.size(); i++)
	{
	    JobBean jobBean = joblist.get(i);
	    Log.i(TAG, jobBean.getJobId());
	    Log.i(TAG, jobBean.getJobTitle());
	    Log.i(TAG, jobBean.getJobContent());
	    Log.i(TAG, jobBean.getJobDate());
	    Log.i(TAG, jobBean.getJobState());
	}
	Log.i(TAG, "--------------");
	List<JobBean> joblist2 = jobDao.query(2, BaseConst.username);
	for (int i = 0; i < joblist2.size(); i++)
	{
	    JobBean jobBean = joblist2.get(i);
	    Log.i(TAG, jobBean.getJobId());
	    Log.i(TAG, jobBean.getJobTitle());
	    Log.i(TAG, jobBean.getJobContent());
	    Log.i(TAG, jobBean.getJobDate());
	    Log.i(TAG, jobBean.getJobReply());
	    Log.i(TAG, jobBean.getJobState());
	}
    }
}
