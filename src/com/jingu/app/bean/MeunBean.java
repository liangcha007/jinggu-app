package com.jingu.app.bean;

import java.io.Serializable;
import java.util.List;

public class MeunBean implements Serializable
{
    private static final long serialVersionUID = 1L;
    public String menuId;// 一级菜单id
    public String menuName;// 一级菜单名称
    public List<ParamBean> second;// 对应的二级菜单

    public String getMenuId()
    {
	return menuId;
    }

    public void setMenuId(String menuId)
    {
	this.menuId = menuId;
    }

    public String getMenuName()
    {
	return menuName;
    }

    public void setMenuName(String menuName)
    {
	this.menuName = menuName;
    }

    public List<ParamBean> getSecond()
    {
	return second;
    }

    public void setSecond(List<ParamBean> second)
    {
	this.second = second;
    }

}
