package com.jingu.app.test;

import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;

import com.jingu.app.bean.UserBean;
import com.jingu.app.dao.DBUserInfoDao;

public class UserBeanDaoTest extends AndroidTestCase
{
     static String TAG = "JUINT_TAG";

    public void testAdd()
    {
	UserBean user = new UserBean("123456", "zhouc");
	DBUserInfoDao uDao = new DBUserInfoDao(getContext());
	uDao.add(user);
	uDao.closeDB();
    }
    
    public void testQuery() throws Exception
    {
	DBUserInfoDao uDao = new DBUserInfoDao(getContext());
	UserBean u1 = uDao.query("zhouc");
	uDao.closeDB();
	Log.i(TAG, u1.getPassword());
	Log.i(TAG, u1.getUsername());
	Log.i(TAG, u1.getId());
    }

    public void testQuerylist() throws Exception
    {
	DBUserInfoDao uDao = new DBUserInfoDao(getContext());
	List<UserBean> list = uDao.query();
	for (int i = 0; i < list.size(); i++)
	{
	    Log.i(TAG, list.get(i).getId());
	    Log.i(TAG, list.get(i).getPassword());
	    Log.i(TAG, list.get(i).getUsername());
	}
	uDao.closeDB();
    }
    
    public void testDel() throws Exception
    {
	DBUserInfoDao uDao = new DBUserInfoDao(getContext());
	uDao.delall();
	Log.i(TAG, Integer.toString(uDao.query().size()));
	uDao.closeDB();
    }
}
