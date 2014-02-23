package com.setoalan.zappossale;

import java.util.List;

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
	private static final int POLL_INTERVAL = 1000 * 10; // 10 seconds
	private static Context mContext;
	
	public static AlarmManager am;
	public static PendingIntent pi;
	
	public ProductService() {
		super(TAG);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		ConnectivityManager cm = (ConnectivityManager)
				getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if (cm.getActiveNetworkInfo() == null) return;
		
		Log.i(TAG, "Received an intent: " + intent);
		
		List<Product> products = ZapposSaleFragment.db.getAllProducts();
		for (Product p : products) {
			Log.i(p.getProductId(), p.getStyleId());
			new FetchItemTask(p).execute();
		}
	}
	
	private class FetchItemTask extends AsyncTask<Void, Void, Void> {

		Product mProduct;
		
		public FetchItemTask(Product p) {
			mProduct = p;
		}

		@Override
		protected Void doInBackground(Void... params) {
			return new StyleFetcher(mContext, mProduct).fetchItems();
		}
		
	}
	
	public static void setServiceAlarm(Context context) {
		mContext = context;
		
		Intent i = new Intent(context, ProductService.class);
		
		pi = PendingIntent.getService(context, 0, i, 0);
		
		am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), POLL_INTERVAL, pi);
	}

}
