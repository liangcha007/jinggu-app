package com.jingu.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhouc52 二级联通相关Bean
 */
public class AttrBean implements Serializable
{
    private static final long serialVersionUID = 1L;
    public List<MeunBean> mList;// 联动菜单信息
    public List<ParamBean> rList;// 规格参数信息

    public List<MeunBean> getmList()
    {
	return mList;
    }

    public void setmList(List<MeunBean> mList)
    {
	this.mList = mList;
    }

    public List<ParamBean> getrList()
    {
	return rList;
    }

    public void setrList(List<ParamBean> rList)
    {
	this.rList = rList;
    }

}
