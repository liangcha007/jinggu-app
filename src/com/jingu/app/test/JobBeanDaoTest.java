package com.jingu.app.test;

import android.test.AndroidTestCase;

import com.jingu.app.bean.JobBean;
import com.jingu.app.dao.DBJobInfoDao;

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

}
