package com.jingu.app.test;

import android.test.AndroidTestCase;
import android.util.Log;

import com.jingu.app.bean.AddFormBean;
import com.jingu.app.bean.ItemBean;
import com.jingu.app.service.HttpClientService;
import com.jingu.app.util.CustomerHttpClient;

public class HttpClientTest extends AndroidTestCase
{
    static String TAG = "JUINT_TAG";
    static String URL = "http://www.baidu.com";

    public void testPost()
    {
	String return_josn = CustomerHttpClient.post(URL);
	Log.i(TAG, return_josn);
    }

    public void testGetRepostOfAddCustomerInfo()
    {
	AddFormBean aBean = HttpClientService.getRepostOfAddCustomerInfo();
	Log.i(TAG, aBean.getState());
	Log.i(TAG, aBean.getDes());
	for (int i = 0; i < aBean.getItemList().size(); i++)
	{
	    ItemBean iBean = aBean.getItemList().get(i);
	    Log.i(TAG, "item" + Integer.toString(i + 1));
	    Log.i(TAG, iBean.getFd_name());
	    Log.i(TAG, iBean.getName());
	    Log.i(TAG, iBean.getType());
	    if (iBean.getItemList() != null)
	    {
		for (int j = 0; j < iBean.getItemList().size(); j++)
		{
		    String str = iBean.getItemList().get(j);
		    Log.i(TAG, str);
		}
	    }
	}
    }
}
