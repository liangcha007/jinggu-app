package com.jingu.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhouc52
 * 
 */
public class ItemBean implements Serializable
{
    private static final long serialVersionUID = 1L;
    public String fd_name;// 返回名称
    public String type;// 类型
    public String name;// 名称
    public List<String> valueList;// 内容

    public String getFd_name()
    {
	return fd_name;
    }

    public void setFd_name(String fd_name)
    {
	this.fd_name = fd_name;
    }

    public String getType()
    {
	return type;
    }

    public void setType(String type)
    {
	this.type = type;
    }

    public String getName()
    {
	return name;
    }

    public void setName(String name)
    {
	this.name = name;
    }

    public List<String> getItemList()
    {
	return valueList;
    }

    public void setItemList(List<String> itemList)
    {
	this.valueList = itemList;
    }
}
