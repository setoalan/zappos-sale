package com.setoalan.zappossale;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class StyleFetcher {
	
	private static final String TAG = "ItemFetcher";
	private static final String URL = "http://api.zappos.com/Product/styleId/";
	private static final String API_KEY = "a73121520492f88dc3d33daf2103d7574f1a3166";
	
	private Context mContext;
	private Product mProduct;
	
	public StyleFetcher(Context context, Product product) {
		mContext = context;
		mProduct = product;
	}

	public Void fetchItems()  {
		
		String url = Uri.parse(URL + mProduct.getStyleId() + "?").buildUpon()
				.appendQueryParameter("includes", "[\"styles\"]")
				.appendQueryParameter("key", API_KEY)
				.build().toString();
		
		String result = null;
		
		try {
			final HttpClient httpclient = new DefaultHttpClient();;
		
			final HttpUriRequest request = new HttpGet(url);
		
			final HttpResponse response = httpclient.execute(request);
			
			final StatusLine status = response.getStatusLine();
			
			if (status.getStatusCode() == HttpStatus.SC_OK) {
				final ByteArrayOutputStream out = new ByteArrayOutputStream();
				
				try {
					response.getEntity().writeTo(out);
					result = out.toString();
				} finally {
					out.close();
				}
			} else {
				response.getEntity().getContent().close();
				throw new IOException(status.getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return deserialize(result);
	}

	private Void deserialize(String result) {
		try {
			if (result == null) return null;
			JSONObject obj = new JSONObject(result);
			String percentOff = obj.getJSONArray("product").getJSONObject(0).getJSONArray("styles").getJSONObject(0).getString("percentOff");
			if (Integer.parseInt(percentOff.replace("%", "")) >= 20) {
				Log.i(TAG, "Notification that is on sale");
				if (ProductService.am == null || ProductService.pi == null) return null;
				showNotification(mContext, obj);
				ZapposSaleFragment.db.deleteProduct(Integer.parseInt(ProductGalleryFragment.styleId));
				if (ZapposSaleFragment.db.getProductCount() == 0) {
					Log.i(TAG, "Service Stopped");
					ProductService.am.cancel(ProductService.pi);
					ProductService.pi.cancel();
				}
			} else {
				Log.i(TAG, "Not on sale");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void showNotification(Context context, JSONObject obj) {
		try {
			String productName = obj.getJSONArray("product").getJSONObject(0).getString("productName");
			String brandName = obj.getJSONArray("product").getJSONObject(0).getString("brandName");
			String color = obj.getJSONArray("product").getJSONObject(0).getJSONArray("styles").getJSONObject(0).getString("color");
			String productUrl = obj.getJSONArray("product").getJSONObject(0).getJSONArray("styles").getJSONObject(0).getString("productUrl");
			
			Uri pageUri = Uri.parse(productUrl);
			Intent intent = new Intent(Intent.ACTION_VIEW, pageUri);
			PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
			
			Notification notification = new NotificationCompat.Builder(mContext)
		        .setTicker("A saved Zappos product is on sale!")
		        .setSmallIcon(R.drawable.zappos_logo_square)
		        .setContentTitle("Zappos product is on sale.")
		        .setContentText("Your saved item of " + color + " " + brandName + " " + productName + " is 20% off or more RIGHT NOW!")
		        .setContentIntent(pIntent)
		        .setAutoCancel(true)
		        .build();

			NotificationManager notificationManager = (NotificationManager)
					mContext.getSystemService(Context.NOTIFICATION_SERVICE);

			notificationManager.notify(0, notification);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
