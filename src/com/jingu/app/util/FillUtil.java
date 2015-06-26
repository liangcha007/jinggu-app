package com.jingu.app.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;


import android.os.Environment;
import android.util.Log;

public class FillUtil
{
    /**
     * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
     * 
     * @return
     */
    public static boolean isSdCardExist()
    {
	return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡根目录路径
     * 
     * @return
     */
    public static String getSdCardPath()
    {
	boolean exist = isSdCardExist();
	String sdpath = "";
	if (exist)
	{
	    sdpath = Environment.getExternalStorageDirectory().getAbsolutePath();
	}
	else
	{
	    sdpath = "不适用";
	}
	return sdpath;

    }

    /**
     * 获取默认的文件路径
     * 
     * @return
     */
    public static String getDefaultFilePath()
    {
	String filepath = "";
	File file = new File(Environment.getExternalStorageDirectory(), "abc.txt");
	if (file.exists())
	{
	    filepath = file.getAbsolutePath();
	}
	else
	{
	    filepath = "不适用";
	}
	return filepath;
    }

    /**
     * 写入日志
     * 
     * @param logStr
     */
    public static void writeLogToFile(String logStr)
    {
	try
	{
	    String wString = "\n\n日志写入时间：" + BaseConst.getDate(new Date(),2) + logStr;
	    File file = new File(Environment.getExternalStorageDirectory(), "JinGuLog.txt");
	    // 第二个参数意义是说是否以append方式添加内容
	    BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
	    bw.write(wString);
	    bw.flush();
	    Log.i("JinGu", "写文件成功，文件路径为：" + file.getPath());
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    Log.i("JinGu", "写文件失败，失败原因为：" + e.getMessage());
	}
    }
}
