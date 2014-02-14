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

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class ItemFetcher {
	
	public static final String TAG = "ItemFetcher";
	
	private static final String URL = "http://api.zappos.com/Product/styleId/";
	private static final String API_KEY = "a73121520492f88dc3d33daf2103d7574f1a3166";

	public Void fetchItems()  {
		String url = Uri.parse(URL + ZapposProductListFragment.styleId + "?").buildUpon()
				.appendQueryParameter("includes", "[\"styles\"]")
				.appendQueryParameter("key", API_KEY)
				.build().toString();
		
		Log.i(TAG, url);
		
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
					//Log.i(TAG, result);
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
			if (result == null)
				return null;
			JSONObject obj = new JSONObject(result);
			String percentOff = obj.getJSONArray("product").getJSONObject(0).getJSONArray("styles").getJSONObject(0).getString("percentOff");
			if (Integer.parseInt(percentOff.replace("%", "")) >= 20) {
				Log.i(TAG, "SEND EMAIL");
				// Send email & turn off alarmManager
				if (ProductService.am == null || ProductService.pi == null) return null;
				ProductService.am.cancel(ProductService.pi);
				ProductService.pi.cancel();
				
				new EmailTask().execute();
				
			} else {
				Log.i(TAG, "NOT ON SALE");
				// Do nothing
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private class EmailTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected Void doInBackground(Void... params) {					
			return new EmailSender().initalize();
		}
		
	}

}
