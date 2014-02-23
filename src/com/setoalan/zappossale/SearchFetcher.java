package com.setoalan.zappossale;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
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

public class SearchFetcher {

	private static final String URL = "http://api.zappos.com/Search?term=";
	private static final String API_KEY = "a73121520492f88dc3d33daf2103d7574f1a3166";
	
	private ArrayList<Product> mProducts = new ArrayList<Product>();
	
	public ArrayList<Product> fetchItems()  {
		@SuppressWarnings("deprecation")
		String url = Uri.parse(URL + URLEncoder.encode(ZapposSaleFragment.SEARCH_WORD)).buildUpon()
				.appendQueryParameter("limit", "100")
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
			JSONObject obj = new JSONObject(result);
			
			for (int i=0; i<Integer.parseInt(obj.getString("currentResultCount")); i++) {
				Product mZP = new Product();
				
				String styleId = obj.getJSONArray("results").getJSONObject(i).getString("styleId");
				String productId = obj.getJSONArray("results").getJSONObject(i).getString("productId");
				String colorId = obj.getJSONArray("results").getJSONObject(i).getString("colorId");
				String brandName = obj.getJSONArray("results").getJSONObject(i).getString("brandName");
				String productName = obj.getJSONArray("results").getJSONObject(i).getString("productName");
				String productUrl = obj.getJSONArray("results").getJSONObject(i).getString("productUrl");
				String imageUrl = obj.getJSONArray("results").getJSONObject(i).getString("thumbnailImageUrl");
				String price = obj.getJSONArray("results").getJSONObject(i).getString("price");
				String originalPrice = obj.getJSONArray("results").getJSONObject(i).getString("originalPrice");
				String percentOff = obj.getJSONArray("results").getJSONObject(i).getString("percentOff");
				
				mZP.setStyleId(styleId);
				mZP.setProductId(productId);
				mZP.setColorId(colorId);
				mZP.setBrandName(brandName);
				mZP.setProductName(productName);
				mZP.setProductUrl(productUrl);
				mZP.setImageUrl(imageUrl);
				mZP.setPrice(price);
				mZP.setOriginalPrice(originalPrice);
				mZP.setPercentOff(percentOff);
				
				mProducts.add(mZP);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mProducts;
	}
	
}
