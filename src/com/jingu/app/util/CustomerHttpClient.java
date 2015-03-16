package com.jingu.app.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class CustomerHttpClient
{
    private static final String TAG = "CustomerHttpClient";
    private static final String CHARSET = HTTP.UTF_8;
    public static HttpClient mHttpClient;

    public static synchronized HttpClient getHttpClient()
    {
	if (null == mHttpClient)
	{
	    HttpParams params = new BasicHttpParams();
	    // 设置一些基本参数
	    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	    HttpProtocolParams.setContentCharset(params, CHARSET);
	    HttpProtocolParams.setUseExpectContinue(params, true);

	    // 超时设置
	    /* 从连接池中取连接的超时时间 */
	    ConnManagerParams.setTimeout(params, 1000);
	    /* 连接超时 */
	    HttpConnectionParams.setConnectionTimeout(params, 2000);
	    /* 请求超时 */
	    HttpConnectionParams.setSoTimeout(params, 5000);

	    // 设置我们的HttpClient支持HTTP和HTTPS两种模式
	    SchemeRegistry schReg = new SchemeRegistry();
	    schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	    schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

	    // 使用线程安全的连接管理来创建HttpClient
	    ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
	    mHttpClient = new DefaultHttpClient(conMgr, params);
	}
	return mHttpClient;
    }

    private static synchronized HttpClient getHttpClient2()
    {
	if (mHttpClient == null)
	{
	    final HttpParams httpParams = new BasicHttpParams();

	    // timeout: get connections from connection pool
	    ConnManagerParams.setTimeout(httpParams, 1000);
	    // timeout: connect to the server
	    HttpConnectionParams.setConnectionTimeout(httpParams, 2000);
	    // timeout: transfer data from server
	    HttpConnectionParams.setSoTimeout(httpParams, 15000);

	    // set max connections per host
	    ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(20));
	    // set max total connections
	    ConnManagerParams.setMaxTotalConnections(httpParams, 200);

	    // use expect-continue handshake
	    HttpProtocolParams.setUseExpectContinue(httpParams, true);
	    // disable stale check
	    HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);

	    HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
	    HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);

	    HttpClientParams.setRedirecting(httpParams, false);

	    // set user agent
	    String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
	    HttpProtocolParams.setUserAgent(httpParams, userAgent);

	    // disable Nagle algorithm
	    HttpConnectionParams.setTcpNoDelay(httpParams, true);

	    HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

	    // scheme: http and https
	    SchemeRegistry schemeRegistry = new SchemeRegistry();
	    schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	    schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

	    ClientConnectionManager manager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
	    mHttpClient = new DefaultHttpClient(manager, httpParams);
	}

	return mHttpClient;
    }

    /**
     * post方法
     * 
     * @param url
     * @param params
     * @return
     */
    public static String post(String url, NameValuePair... params)
    {
	HttpPost request = null;
	try
	{
	    // 编码参数
	    List<NameValuePair> formparams = new ArrayList<NameValuePair>(); // 请求参数
	    for (NameValuePair p : params)
	    {
		formparams.add(p);
	    }
	    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, CHARSET);
	    // 创建POST请求
	    request = new HttpPost(url);
	    request.setEntity(entity);
	    // 发送请求
	    HttpClient client = getHttpClient2();
	    HttpResponse response = client.execute(request);
	    if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
	    {
		Log.i(TAG, "httpResponse is faile,the Status Code is:" + response.getStatusLine().getStatusCode());
		return BaseConst.RPARAM;
	    }
	    HttpEntity resEntity = response.getEntity();
	    return (resEntity == null) ? null : EntityUtils.toString(resEntity, CHARSET);
	}
	catch (Exception e)
	{
	    Log.i(TAG, "Http request is ERROR,the error msg is:" + e.getMessage());
	    e.printStackTrace();
	    throw new RuntimeException(BaseConst.RPARAM, e);
	}
	finally
	{
	    if (request != null)
	    {
		request.abort();
		request = null;
		Log.i(TAG, "http request is release!");
	    }
	}
    }

    public static String post(String url, List<NameValuePair> formparams)
    {
	HttpPost request = null;
	try
	{
	    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, CHARSET);
	    // 创建POST请求
	    request = new HttpPost(url);
	    request.setEntity(entity);
	    // 发送请求
	    HttpClient client = getHttpClient2();
	    HttpResponse response = client.execute(request);
	    if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
	    {
		Log.i(TAG, "httpResponse is faile,the Status Code is:" + response.getStatusLine().getStatusCode());
		return BaseConst.RPARAM;
	    }
	    HttpEntity resEntity = response.getEntity();
	    return (resEntity == null) ? null : EntityUtils.toString(resEntity, CHARSET);
	}
	catch (Exception e)
	{
	    throw new RuntimeException(BaseConst.RPARAM, e);
	}
	finally
	{
	    if (request != null)
	    {
		request.abort();
		request = null;
		Log.i(TAG, "http request is release!");
	    }
	}
    }
}