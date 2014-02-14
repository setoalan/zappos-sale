package com.setoalan.zappossale;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

public class ProductService extends IntentService {

	private static final String TAG = "ProductService"
;	
	public ProductService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		ConnectivityManager cm = (ConnectivityManager)
				getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() == null) return;
		Log.i(TAG, "Received an intent: " + intent);
	}

}
