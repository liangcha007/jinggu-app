package com.jingu.app.bean;

import java.io.Serializable;

import android.widget.TableRow;

public class TableSecondBean implements Serializable
{
    private static final long serialVersionUID = 1L;
    public TableRow tRow;
    // 出租/出租钢瓶(id=1) 5kg(id=2) 5
    public String muneId;// 菜单id
    public String ruleId;// 规格id
    public String nums;// 个数
    public String muneName;// 菜单名称
    public String ruleName;// 规则名称

    public TableSecondBean(String muneId, String ruleId, String nums, String muneName, String ruleName, TableRow tRow)
    {
	super();
	this.muneId = muneId;
	this.ruleId = ruleId;
	this.nums = nums;
	this.muneName = muneName;
	this.ruleName = ruleName;
	this.tRow = tRow;
    }

    public TableRow gettRow()
    {
	return tRow;
    }

    public void settRow(TableRow tRow)
    {
	this.tRow = tRow;
    }

    public String getMuneName()
    {
	return muneName;
    }

    public void setMuneName(String muneName)
    {
	this.muneName = muneName;
    }

    public String getRuleName()
    {
	return ruleName;
    }

    public void setRuleName(String ruleName)
    {
	this.ruleName = ruleName;
    }

    public String getMuneId()
    {
	return muneId;
    }

    public void setMuneId(String muneId)
    {
	this.muneId = muneId;
    }

    public String getRuleId()
    {
	return ruleId;
    }

    public void setRuleId(String ruleId)
    {
	this.ruleId = ruleId;
    }

    public String getNums()
    {
	return nums;
    }

    public void setNums(String nums)
    {
	this.nums = nums;
    }
}
