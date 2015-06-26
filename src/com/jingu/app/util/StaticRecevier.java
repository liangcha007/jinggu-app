package com.jingu.app.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jingu.app.service.BackGroundService;

public class StaticRecevier extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
	Intent i = new Intent(context, BackGroundService.class);
	context.startService(i);
    }

}
