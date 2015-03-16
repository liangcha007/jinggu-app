package com.jingu.app.bean;

import java.util.List;

import android.view.View;
import android.widget.RadioButton;

/**
 * @author zhouc52 用来存储paras参数控件的实体对象的
 */
public class ItemViewBean
{
    public String fd_name;
    public int type;
    public View view;
    public List<RadioButton> rbList;

    public String getFd_name()
    {
	return fd_name;
    }

    public void setFd_name(String fd_name)
    {
	this.fd_name = fd_name;
    }

    public List<RadioButton> getRbList()
    {
	return rbList;
    }

    public void setRbList(List<RadioButton> rbList)
    {
	this.rbList = rbList;
    }

    public View getView()
    {
	return view;
    }

    public void setView(View view)
    {
	this.view = view;
    }

    public int getType()
    {
	return type;
    }

    public void setType(int type)
    {
	this.type = type;
    }

}
