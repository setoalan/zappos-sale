package com.setoalan.zappossale;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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

public class ZapposFetcher {

	public static final String TAG = "ZapposFetcher";
	
	private static final String URL = "http://api.zappos.com/Product/";
	private static final String API_KEY = "a73121520492f88dc3d33daf2103d7574f1a3166";
	
	private ArrayList<Product> mProducts = new ArrayList<Product>();
	
	public ArrayList<Product> fetchItems()  {
		String url = Uri.parse(URL + "7515478" + "?").buildUpon()
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

	private ArrayList<Product> deserialize(String result) {
		try {
			Product mZP;
					
			JSONObject obj = new JSONObject(result);
			
			for (int i=0; i<obj.getJSONArray("product").getJSONObject(0).getJSONArray("styles").length(); i++) {
				mZP = new Product();
				
				String productId = obj.getJSONArray("product").getJSONObject(0).getString("productId");
				String brandName = obj.getJSONArray("product").getJSONObject(0).getString("brandName");
				String productName = obj.getJSONArray("product").getJSONObject(0).getString("productName");
				String styleId = obj.getJSONArray("product").getJSONObject(0).getJSONArray("styles").getJSONObject(i).getString("styleId");
				String percentOff = obj.getJSONArray("product").getJSONObject(0).getJSONArray("styles").getJSONObject(i).getString("percentOff");
				String imageUrl = obj.getJSONArray("product").getJSONObject(0).getJSONArray("styles").getJSONObject(i).getString("imageUrl");
			
				mZP.setProductId(productId);
				mZP.setBrandName(brandName);
				mZP.setProductName(productName);
				mZP.setStyleId(styleId);
				mZP.setPercentOff(percentOff);
				mZP.setImageUrl(imageUrl);
				
				mProducts.add(mZP);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mProducts;
	}
	
}
