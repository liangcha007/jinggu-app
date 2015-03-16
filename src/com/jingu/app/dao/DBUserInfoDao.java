package com.jingu.app.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jingu.app.bean.UserBean;
import com.jingu.app.util.DatabaseHelper;

public class DBUserInfoDao
{
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public DBUserInfoDao(Context context)
    {
	helper = new DatabaseHelper(context);
	db = helper.getWritableDatabase();
    }

    /**
     * 新增用户
     * 
     * @param user
     */
    public void add(UserBean user)
    {
	if (user != null)
	{
	    db.execSQL("INSERT INTO " + DatabaseHelper.USER_TABLE + " VALUES(null,?, ?)", new Object[] { user.getUsername(), user.getPassword() });
	}
    }

    /**
     * 更新用户
     * 
     * @param user
     */
    public void update(UserBean user)
    {
	ContentValues cv = new ContentValues();
	cv.put("password", user.getPassword());
	db.update(DatabaseHelper.USER_TABLE, cv, "username = ?", new String[] { user.getUsername() });
    }

    /**
     * 根据用户名查询用户信息
     * 
     * @param user
     * @return
     */
    public UserBean query(String username)
    {
	StringBuffer sql = new StringBuffer();
	sql.append("select * from ").append(DatabaseHelper.USER_TABLE).append(" where username=?");
	Cursor c = db.rawQuery(sql.toString(), new String[] { username });
	while (c.moveToNext())
	{
	    String uname = c.getString(c.getColumnIndex("username"));
	    if (username.equals(uname) || username == uname)
	    {
		UserBean user = new UserBean();
		user.setId(Integer.toString(c.getInt(c.getColumnIndex("id"))));
		user.setUsername(username);
		user.setPassword(c.getString(c.getColumnIndex("password")));
		c.close();
		return user;
	    }
	}
	c.close();
	return null;
    }

    /**
     * 查询所有用户
     * 
     * @return
     */
    public List<UserBean> query()
    {
	ArrayList<UserBean> users = new ArrayList<UserBean>();
	Cursor c = queryTheCursor();
	while (c.moveToNext())
	{
	    UserBean user = new UserBean();
	    user.setId(Integer.toString(c.getInt(c.getColumnIndex("id"))));
	    user.setUsername(c.getString(c.getColumnIndex("username")));
	    user.setPassword(c.getString(c.getColumnIndex("password")));
	    users.add(user);
	}
	c.close();
	return users;
    }

    /**
     * 清空表，并置自增从0开始
     */
    public void delall()
    {
	StringBuffer sql = new StringBuffer();
	sql.append("DELETE FROM ").append(DatabaseHelper.USER_TABLE).append(";");
	db.execSQL(sql.toString());
	sql.setLength(0);
	sql.append("update sqlite_sequence set seq=0 where name='").append(DatabaseHelper.USER_TABLE).append("';");
	db.execSQL(sql.toString());
    }

    public Cursor queryTheCursor()
    {
	Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.USER_TABLE, null);
	return c;
    }

    public void closeDB()
    {
	// 释放数据库资源
	db.close();
    }
}
