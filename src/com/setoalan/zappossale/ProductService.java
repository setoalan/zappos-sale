package com.setoalan.zappossale;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

public class ProductService extends IntentService {

	private static final String TAG = "ProductService";
	private static final int POLL_INTERVAL = 1000 * 60; // 60 seconds	
	
	public ProductService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		ConnectivityManager cm = (ConnectivityManager)
				getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() == null) return;
		String productId = ZapposProductListFragment.productId;
		String styleId = ZapposProductListFragment.styleId;
		
		Log.i(TAG, "Received an intent: " + intent);
		
		// do a check against service
	}
	
	public static void setServiceAlarm(Context context) {
		Intent i  = new Intent(context, ProductService.class);
		PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
		
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), POLL_INTERVAL, pi);
	}

}
