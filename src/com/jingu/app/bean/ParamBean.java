package com.jingu.app.bean;

import java.io.Serializable;

public class ParamBean implements Serializable
{
    private static final long serialVersionUID = 1L;
    public String paramName;
    public String paramValue;

    public String getParamName()
    {
	return paramName;
    }

    public void setParamName(String paramName)
    {
	this.paramName = paramName;
    }

    public String getParamValue()
    {
	return paramValue;
    }

    public void setParamValue(String paramValue)
    {
	this.paramValue = paramValue;
    }
}
