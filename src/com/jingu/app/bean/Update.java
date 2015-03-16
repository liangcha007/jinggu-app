package com.jingu.app.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * 应用程序更新实体类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class Update implements Serializable
{

    private static final long serialVersionUID = -1356876892850891288L;
    public final static String UTF8 = "UTF-8";
    public final static String NODE_ROOT = "jingu";

    private int versionCode;
    private String versionName;
    private String downloadUrl;
    private String updateLog;

    public int getVersionCode()
    {
	return versionCode;
    }

    public void setVersionCode(int versionCode)
    {
	this.versionCode = versionCode;
    }

    public String getVersionName()
    {
	return versionName;
    }

    public void setVersionName(String versionName)
    {
	this.versionName = versionName;
    }

    public String getDownloadUrl()
    {
	return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl)
    {
	this.downloadUrl = downloadUrl;
    }

    public String getUpdateLog()
    {
	return updateLog;
    }

    public void setUpdateLog(String updateLog)
    {
	this.updateLog = updateLog;
    }

    /**
     * Dom方式解析xml文件
     * 
     * @param inStream
     * @return
     * @throws Exception
     */
    public static Update parseXml(InputStream inStream) throws Exception
    {
	Update update = null;
	// 实例化一个文档构建器工厂
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	// 通过文档构建器工厂获取一个文档构建器
	DocumentBuilder builder = factory.newDocumentBuilder();
	// 通过文档通过文档构建器构建一个文档实例
	Document document = builder.parse(inStream);
	// 获取XML文件根节点
	Element root = document.getDocumentElement();
	// 获得所有子节点
	NodeList childNodes = root.getChildNodes();
	for (int j = 0; j < childNodes.getLength(); j++)
	{
	    // 遍历子节点
	    Node childNode = (Node) childNodes.item(j);
	    if (childNode.getNodeType() == Node.ELEMENT_NODE)
	    {
		update = new Update();
		Element childElement = (Element) childNode;
		// 版本号
		if ("version".equals(childElement.getNodeName()))
		{
		    update.setVersionCode(Integer.parseInt(childElement.getFirstChild().getNodeValue()));
		}
		// 软件名称
		else if (("name".equals(childElement.getNodeName())))
		{
		    update.setVersionName(childElement.getFirstChild().getNodeValue());
		}
		// 下载地址
		else if (("url".equals(childElement.getNodeName())))
		{
		    update.setDownloadUrl(childElement.getFirstChild().getNodeValue());
		}
		// 下载日志
		else if ("log".equals(childElement.getNodeName()))
		{
		    update.setUpdateLog(childElement.getFirstChild().getNodeValue());
		}
	    }
	}
	return update;
    }

    /**
     * SAX方式解析
     * 
     * @param inputStream
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static Update parse(InputStream inputStream) throws IOException, Exception
    {
	Update update = null;
	// 获得XmlPullParser解析器
	XmlPullParser xmlParser = Xml.newPullParser();
	try
	{
	    xmlParser.setInput(inputStream, UTF8);
	    // 获得解析到的事件类别，这里有开始文档，结束文档，开始标签，结束标签，文本等等事件。
	    int evtType = xmlParser.getEventType();
	    // 一直循环，直到文档结束
	    while (evtType != XmlPullParser.END_DOCUMENT)
	    {
		String tag = xmlParser.getName();
		switch (evtType)
		{
		case XmlPullParser.START_TAG:
		    // 通知信息
		    if (tag.equalsIgnoreCase("update"))
		    {
			update = new Update();
		    }
		    else if (update != null)
		    {
			if (tag.equalsIgnoreCase("version"))
			{
			    update.setVersionCode(Integer.parseInt(xmlParser.nextText()));
			}
			else if (tag.equalsIgnoreCase("name"))
			{
			    update.setVersionName(xmlParser.nextText());
			}
			else if (tag.equalsIgnoreCase("url"))
			{
			    update.setDownloadUrl(xmlParser.nextText());
			}
			else if (tag.equalsIgnoreCase("log"))
			{
			    String logStr = xmlParser.nextText();
			    logStr=logStr.replace("|","\n");
			    update.setUpdateLog(logStr);
			}
		    }
		    break;
		case XmlPullParser.END_TAG:
		    break;
		}
		// 如果xml没有结束，则导航到下一个节点
		evtType = xmlParser.next();
	    }
	}
	catch (XmlPullParserException e)
	{

	}
	finally
	{
	    inputStream.close();
	}
	return update;
    }
}
