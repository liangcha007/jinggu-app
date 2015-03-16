package com.jingu.app.bean;

import java.io.Serializable;

public class UserBean implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id; // 默认自增
    private String password;// 密码
    private String username;// 用户名

    public UserBean()
    {
	super();
    }

    public UserBean(String username, String password)
    {
	super();
	this.password = password;
	this.username = username;
    }

    public String getPassword()
    {
	return password;
    }

    public void setPassword(String password)
    {
	this.password = password;
    }

    public String getUsername()
    {
	return username;
    }

    public void setUsername(String username)
    {
	this.username = username;
    }

    public String getId()
    {
	return id;
    }

    public void setId(String id)
    {
	this.id = id;
    }
}
