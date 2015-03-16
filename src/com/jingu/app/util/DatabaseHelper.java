package com.jingu.app.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
    // 数据库版本号
    private static final int DATABASE_VERSION = 6;
    // 数据库名
    private static final String DATABASE_NAME = "jingu.db";
    // user表
    public static final String USER_TABLE = "user";
    // 工单表
    /*
     * 工单状态jobstate:N 新工单；R 已阅读 ；O 已完成 ; 默认新工单
     * 工类类型jobtype: N 新增； C 催办；Q 取消； 默认新增
     */
    public static final String JOB_TABLE = "job";

    public DatabaseHelper(Context context)
    {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
	// 自动创建用户表
	StringBuffer sql = new StringBuffer();
	sql.append("CREATE TABLE [" + USER_TABLE + "] (");
	sql.append("[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
	sql.append("[username] TEXT,");
	sql.append("[password] TEXT)");
	db.execSQL(sql.toString());
	sql.setLength(0);
	// 自动创建工单表
	sql.append("CREATE TABLE [" + JOB_TABLE + "] (");
	sql.append("[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
	sql.append("[username] TEXT,");
	sql.append("[jobid] TEXT,");
	sql.append("[jobtitle] TEXT,");
	sql.append("[jobcontent] TEXT,");
	sql.append("[telnum] TEXT,");
	sql.append("[jobreply] TEXT DEFAULT NULL,");
	sql.append("[jobstate] TEXT NOT NULL DEFAULT 'N',");
	sql.append("[jobtype] TEXT NOT NULL DEFAULT 'N',");
	sql.append("[confirmdate] TEXT,");
	sql.append("[jobdate] TimeStamp NOT NULL DEFAULT (datetime('now','localtime')))");
	db.execSQL(sql.toString());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oleversion, int newversion)
    {
	// 如果数据库版本DATABASE_VERSION发生改变，新建表或者修改表在这里执行
	db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
	db.execSQL("DROP TABLE IF EXISTS " + JOB_TABLE);
	onCreate(db);
    }
}
