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
	
	public static final String PREF_SALE = "sale";
	
	ZapposAdapter adapter;
	SharedPreferences sharedPref;
	SharedPreferences.Editor editor;
	String productId, styleId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new ZapposAdapter(ZapposSaleFragment.mProducts);
		setListAdapter(adapter);
		sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		productId = sharedPref.getString("productId", null);
		styleId = sharedPref.getString("styleId", null);
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
					Toast.makeText(getActivity(), "New Saved", Toast.LENGTH_SHORT).show();
				} else {
					AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
					builder2.setTitle("Are you sure you want to override current saved product?");
					builder2.setPositiveButton("Yes", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Toast.makeText(getActivity(), "Saved: " + productId + " " + styleId,  Toast.LENGTH_SHORT).show();
							editor = sharedPref.edit();
							editor.putString("productId", ZapposSaleFragment.mProducts.get(position).getProductId());
							editor.putString("styleId", ZapposSaleFragment.mProducts.get(position).getStyleId());
							editor.commit();
						}
					});
					builder2.setNegativeButton("NO", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					});
					AlertDialog dialog2 = builder2.create();
					dialog2.show();
				}
				//Intent i = new Intent(getActivity(), ProductService.class);
				//getActivity().startService(i);
			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
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
