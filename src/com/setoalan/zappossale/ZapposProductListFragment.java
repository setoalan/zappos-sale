package com.setoalan.zappossale;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.ListFragment;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ZapposProductListFragment extends ListFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ZapposAdapter adapter = new ZapposAdapter(ZapposSaleFragment.mProducts);
		setListAdapter(adapter);
	}
	
	private class ZapposAdapter extends ArrayAdapter<Product> {
		
		public ZapposAdapter(ArrayList<Product> products) {
			super(getActivity(), 0, products);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.list_item_product, null);
			}
			
			Product p = getItem(position);
			
			new ImageFetcher(convertView, p).execute();
			
			return convertView;
		}
		
	}
	
	public class ImageFetcher extends AsyncTask<Void, Void, Drawable> {
		
		InputStream mInputStream;
		Product mProduct;
		Drawable mDrawable;
		View mView;
		
		public ImageFetcher(View convertView, Product p) {
			mView = convertView;
			mProduct = p;
		}

		@Override
		protected Drawable doInBackground(Void... arg0) {
			try {
				mInputStream = (InputStream) new URL(mProduct.getImageUrl()).getContent();
				mDrawable = Drawable.createFromStream(mInputStream, "src name");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Drawable result) {
			super.onPostExecute(result);
			
			ImageView iv = (ImageView) mView.findViewById(R.id.product_image);
			iv.setImageDrawable(mDrawable);
			
			TextView tv0 = (TextView) mView.findViewById(R.id.product_id);
			tv0.setText(mProduct.getProductId());
			
			TextView tv1 = (TextView) mView.findViewById(R.id.brand_name);
			tv1.setText(mProduct.getBrandName());
			
			TextView tv2 = (TextView) mView.findViewById(R.id.product_name);
			tv2.setText(mProduct.getProductName());
			
			TextView tv3 = (TextView) mView.findViewById(R.id.style_id);
			tv3.setText(mProduct.getStyleId());
			
			TextView tv4 = (TextView) mView.findViewById(R.id.percent_off);
			tv4.setText(mProduct.getPercentOff());
			
			return;
		}
	}
}
