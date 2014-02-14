package com.setoalan.zappossale;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

public class ProductService extends IntentService {

	private static final String TAG = "ProductService";
	private static final int POLL_INTERVAL = 1000 * 10; // 60 seconds	
	
	public ProductService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		ConnectivityManager cm = (ConnectivityManager)
				getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() == null) return;
		
		Log.i(TAG, "Received an intent: " + intent);
		
		new FetchItemTask().execute();
	}
	
	private class FetchItemTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			return new ItemFetcher().fetchItems();
		}
		
	}
	
	public static void setServiceAlarm(Context context) {
		Intent i  = new Intent(context, ProductService.class);
		PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
		
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), POLL_INTERVAL, pi);
	}

}
