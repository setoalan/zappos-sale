package com.setoalan.zappossale;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ZapposProductListFragment extends ListFragment {
	
	public static final String PRODUCT_ID = "productId";
	public static final String STYLE_ID = "styleId";
	
	public static String productId, styleId;
	
	ZapposAdapter adapter;
	SharedPreferences sharedPref;
	SharedPreferences.Editor editor;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		productId = sharedPref.getString(PRODUCT_ID, null);
		styleId = sharedPref.getString(STYLE_ID, null);
		adapter = new ZapposAdapter(ZapposSaleFragment.mProducts);
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, final int position, long id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Would you like to get a notification when this " +
				"product is 20% off or more?");
		builder.setPositiveButton("Yes", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (productId == null || styleId == null) {
					Toast.makeText(getActivity(), "New Save: " + productId + " " + styleId, Toast.LENGTH_SHORT).show();
					saveProductStartService(position);
				} else {
					AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
					builder2.setTitle("Are you sure you want to override current saved product?" +
							"\nProduct Id: " + productId + "\t" + "Style Id: " + styleId);
					builder2.setPositiveButton("Yes", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Toast.makeText(getActivity(), "Override Save: " + productId + " " + 
									ZapposSaleFragment.mProducts.get(position).getStyleId(), Toast.LENGTH_SHORT).show();
							saveProductStartService(position);
						}
					});
					builder2.setNegativeButton("No", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					});
					AlertDialog dialog2 = builder2.create();
					dialog2.show();
				}
			}
		});
		builder.setNegativeButton("No", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void saveProductStartService(int position) {
		editor = sharedPref.edit();
		editor.putString(PRODUCT_ID, ZapposSaleFragment.mProducts.get(position).getProductId());
		editor.putString(STYLE_ID, ZapposSaleFragment.mProducts.get(position).getStyleId());
		editor.commit();
		styleId = ZapposSaleFragment.mProducts.get(position).getStyleId();
		ProductService.setServiceAlarm(getActivity());
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
