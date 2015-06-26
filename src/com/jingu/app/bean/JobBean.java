package com.jingu.app.bean;

import java.io.Serializable;
import java.util.Date;

import com.jingu.app.util.BaseConst;

public class JobBean implements Serializable
{
    private static final long serialVersionUID = 1L;
    private int id; // 自增ID
    private String username;// 用户标识
    private String jobId; // 工单ID
    private String jobTitle; // 工单标题
    private String jobContent; // 工单内容
    private String jobReply; // 工单回复内容
    private String jobState; // 工单状态
    private String jobType; // 工单类型(新增、催办、取消)
    private String jobDate; // 工单下发日期
    private String confirmdate;// 回复日期
    private String telNum;// 联系电话

    public String getTelNum()
    {
	return telNum;
    }

    public void setTelNum(String telNum)
    {
	this.telNum = telNum;
    }

    public String getJobType()
    {
	return jobType;
    }

    public void setJobType(String jobType)
    {
	this.jobType = jobType;
    }

    public String getConfirmdate()
    {
	return confirmdate;
    }

    public void setConfirmdate(String confirmdate)
    {
	this.confirmdate = confirmdate;
    }

    public JobBean()
    {
	super();
    }

    public JobBean(String jobId, String jobTitle, String jobContent, String username, String tel, String jobDate)
    {
	super();
	this.jobId = jobId;
	this.jobTitle = jobTitle;
	this.jobContent = jobContent;
	this.username = username;
	this.telNum = tel;
	this.jobDate = jobDate;
    }

    /**
     * 根据时间段查询工单专用bean的构造函数
     * 
     * @param jobId
     * @param jobTitle
     * @param jobContent
     * @param username
     * @param tel
     * @param jobDate
     */
    public JobBean(String jobId, String jobTitle, String jobContent, String username, String tel, String jobDate,
	    String jobReply)
    {
	super();
	this.jobId = jobId;
	this.jobTitle = jobTitle;
	this.jobContent = jobContent;
	this.username = username;
	this.telNum = tel;
	this.jobDate = jobDate;
	this.jobReply = jobReply;
	this.confirmdate = BaseConst.getDate(new Date(), 2);//Check all job,just marks
	this.jobState = "C";// Check all job,just mark
	this.jobType="C";//Check all job,just mark
    }

    public int getId()
    {
	return id;
    }

    public void setId(int id)
    {
	this.id = id;
    }

    public String getUsername()
    {
	return username;
    }

    public void setUsername(String username)
    {
	this.username = username;
    }

    public String getJobId()
    {
	return jobId;
    }

    public void setJobId(String jobId)
    {
	this.jobId = jobId;
    }

    public String getJobTitle()
    {
	return jobTitle;
    }

    public void setJobTitle(String jobTitle)
    {
	this.jobTitle = jobTitle;
    }

    public String getJobContent()
    {
	return jobContent;
    }

    public void setJobContent(String jobContent)
    {
	this.jobContent = jobContent;
    }

    public String getJobReply()
    {
	return jobReply;
    }

    public void setJobReply(String jobReply)
    {
	this.jobReply = jobReply;
    }

    public String getJobState()
    {
	return jobState;
    }

    public void setJobState(String jobState)
    {
	this.jobState = jobState;
    }

    public String getJobDate()
    {
	return jobDate;
    }

    public void setJobDate(String jobDate)
    {
	this.jobDate = jobDate;
    }

}
