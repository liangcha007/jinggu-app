package com.jingu.app.bean;

import java.util.List;

import android.view.View;
import android.widget.RadioButton;

/**
 * @author zhouc52 用来存储paras参数控件的实体对象的
 */
public class ItemViewBean
{
    public String fd_name;// 控件id
    public int type;// 类型
    public View view;// 控件的实体对象
    public List<RadioButton> rbList;// 可选的，控件的子项

    public String tmp_static_name;// 对应中文名

    public String getTmp_static_name()
    {
	return tmp_static_name;
    }

    public void setTmp_static_name(String tmp_static_name)
    {
	this.tmp_static_name = tmp_static_name;
    }

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
