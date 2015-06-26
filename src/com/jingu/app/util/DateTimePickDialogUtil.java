package com.jingu.app.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.jingu.app.R;

public class DateTimePickDialogUtil implements OnDateChangedListener
{
    private DatePicker datePicker;
    private AlertDialog ad;
    private String dateTime;
    private String initDateTime;
    private Activity activity;

    /**
     * 日期时间弹出选择框构造函数
     * 
     * @param activity
     *            ：调用的父activity
     * @param initDateTime
     *            初始日期时间值，作为弹出窗口的标题和日期时间初始值
     */
    public DateTimePickDialogUtil(Activity activity, String initDateTime)
    {
	this.activity = activity;
	this.initDateTime = initDateTime;

    }

    public void init(DatePicker datePicker)
    {
	Calendar calendar = Calendar.getInstance(Locale.CHINA);
	if (!(null == initDateTime || "".equals(initDateTime)))
	{
	    calendar = this.getCalendarByInintData(initDateTime);
	}
	else
	{
	    initDateTime = calendar.get(Calendar.YEAR) + "年" + calendar.get(Calendar.MONTH) + "月"
		    + calendar.get(Calendar.DAY_OF_MONTH) + "日 ";
	}

	datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
		this);
    }

    /**
     * 弹出日期时间选择框方法
     * 
     * @param inputDate
     *            :为需要设置的日期时间文本编辑框
     * @return
     */
    public AlertDialog dateTimePicKDialog(final EditText inputDate)
    {
	LinearLayout dateTimeLayout = (LinearLayout) activity.getLayoutInflater().inflate(R.drawable.common_datetime,
		null);
	datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
	init(datePicker);

	ad = new AlertDialog.Builder(activity).setTitle("请选择日期").setView(dateTimeLayout)
		.setPositiveButton("设置", new DialogInterface.OnClickListener()
		{
		    public void onClick(DialogInterface dialog, int whichButton)
		    {
			inputDate.setText(dateTime);
		    }
		}).setNegativeButton("取消", new DialogInterface.OnClickListener()
		{
		    public void onClick(DialogInterface dialog, int whichButton)
		    {
			inputDate.setText(initDateTime);
		    }
		}).show();

	onDateChanged(null, 0, 0, 0);
	return ad;
    }

    public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
    {
	onDateChanged(null, 0, 0, 0);
    }

    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
    {
	// 获得日历实例
	Calendar calendar = Calendar.getInstance();

	calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	dateTime = sdf.format(calendar.getTime());
	ad.setTitle(dateTime);
    }

    /**
     * 实现将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒,并赋值给calendar
     * 
     * @param initDateTime
     *            初始日期时间值 字符串型
     * @return Calendar
     */
    private Calendar getCalendarByInintData(String initDateTime)
    {
	Calendar calendar = Calendar.getInstance();
	String[] sts = initDateTime.split("-");
	int currentYear = Integer.valueOf(sts[0]).intValue();
	int currentMonth = Integer.valueOf(sts[1]).intValue() - 1;
	int currentDay = Integer.valueOf(sts[2]).intValue();
	calendar.set(currentYear, currentMonth, currentDay);
	return calendar;
    }
}
