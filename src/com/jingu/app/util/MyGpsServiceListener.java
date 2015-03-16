package com.jingu.app.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.jingu.app.service.HttpClientService;

/**
 * @author zhouc52
 * 
 *         GPS位置监听
 */
public class MyGpsServiceListener implements LocationListener
{
    private static final String TAG = "JIN_GU";
    private static final float minAccuracyMeters = 35;
    public int GPSCurrentStatus;
    public Context context;

    public MyGpsServiceListener(Context context)
    {
	this.context = context;
    }

    @Override
    public void onLocationChanged(Location location)
    {
	if (location != null)
	{
	    if (location.hasAccuracy() && location.getAccuracy() <= minAccuracyMeters)
	    {
		String latitude = Double.toString(location.getLatitude());
		String longitude = Double.toString(location.getLongitude());
		HttpClientService.sendGpsMsg(latitude, longitude);
		Log.v(TAG, "send GPSmsg!");
	    }
	}
    }

    @Override
    public void onProviderDisabled(String provider)
    {
    }

    @Override
    public void onProviderEnabled(String provider)
    {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
	GPSCurrentStatus = status;
    }
}
