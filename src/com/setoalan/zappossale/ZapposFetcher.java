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

import android.net.Uri;
import android.util.Log;

public class ZapposFetcher {

	public static final String TAG = "ZapposFetcher";
	
	private static final String URL = "http://api.zappos.com/Product/";
	private static final String API_KEY = "a73121520492f88dc3d33daf2103d7574f1a3166";
	
	public Void fetchItems()  {
		String url = Uri.parse(URL + "7515478?").buildUpon()
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
					Log.i(TAG, result);
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
		
		return null;
	}
}
