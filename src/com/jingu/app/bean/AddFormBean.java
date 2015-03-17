package com.jingu.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhouc52 工单的实体类
 */
public class AddFormBean implements Serializable
{
    private static final long serialVersionUID = 1L;
    public String state;//状态，success  failed
    public String des;//原因，失败原因
    public String compand_id;//工单的ID
    public String type;//类型，新增工单，newjob
    public List<ItemBean> itemList;//存储
    public AttrBean aBean; // 菜单和规则
    public List<ParamBean> listParamBeans;// 二级菜单的id和名称

    public List<ParamBean> getListParamBeans()
    {
	return listParamBeans;
    }

    public void setListParamBeans(List<ParamBean> listParamBeans)
    {
	this.listParamBeans = listParamBeans;
    }

    public AttrBean getaBean()
    {
	return aBean;
    }

    public void setaBean(AttrBean aBean)
    {
	this.aBean = aBean;
    }

    public String getCompand_id()
    {
	return compand_id;
    }

    public void setCompand_id(String compand_id)
    {
	this.compand_id = compand_id;
    }

    public String getType()
    {
	return type;
    }

    public void setType(String type)
    {
	this.type = type;
    }

    public String getState()
    {
	return state;
    }

    public void setState(String state)
    {
	this.state = state;
    }

    public String getDes()
    {
	return des;
    }

    public void setDes(String des)
    {
	this.des = des;
    }

    public List<ItemBean> getItemList()
    {
	return itemList;
    }

    public void setItemList(List<ItemBean> itemList)
    {
	this.itemList = itemList;
    }
}
