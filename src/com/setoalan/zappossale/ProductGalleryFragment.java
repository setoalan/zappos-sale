package com.setoalan.zappossale;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductGalleryFragment extends Fragment {
	
	GridView mGridView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		ZapposSaleFragment.mProgressContainer.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().getActionBar().setTitle("Search Results");
		getActivity().getActionBar().setIcon(R.drawable.zappos_logo_square);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		
		View v = inflater.inflate(R.layout.fragment_product_gallery, container, false);
		
		mGridView = (GridView) v.findViewById(R.id.gridView);
		
		mGridView.setAdapter(new GalleryItemAdapter(ZapposSaleFragment.mProducts));
		
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> gridView, View view, int pos, long id) {
				displayDialog(pos);
			}
		});
		
		return v;
	}
	
	private void displayDialog(final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(R.layout.dialog_product, null);
		new ImageFetcher(v, ZapposSaleFragment.mProducts.get(position), false).execute();
		builder.setView(v);
		builder.setTitle("Get Notification?");
		builder.setMessage("Would you like to get a notification when this " +
				"product is 20% off or more?");
		builder.setPositiveButton("Yes", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getActivity(), "New Save: " +
						ZapposSaleFragment.mProducts.get(position).getBrandName() + " " +
						ZapposSaleFragment.mProducts.get(position).getProductName(), Toast.LENGTH_SHORT).show();
				saveProductStartService(position);
			}
		});
		builder.setNeutralButton("View on Website", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Uri pageUri = Uri.parse(ZapposSaleFragment.mProducts.get(position).getProductUrl());
				Intent intent = new Intent(Intent.ACTION_VIEW, pageUri);
				startActivity(intent);
			}
		});
		builder.setNegativeButton("No", null);
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void saveProductStartService(int position) {
		ZapposSaleFragment.db.addProduct(position);
		ProductService.setServiceAlarm(getActivity());
	}
	
	private class GalleryItemAdapter extends ArrayAdapter<Product> {
		
		public GalleryItemAdapter(ArrayList<Product> products) {
			super(getActivity(), 0, products);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.gallery_item, null);
			}
			
			Product p = getItem(position);
			
			new ImageFetcher(convertView, p, true).execute();
			
			return convertView;
		}
		
	}
	
	public class ImageFetcher extends AsyncTask<Void, Void, Drawable> {
		
		InputStream mInputStream;
		Product mProduct;
		Drawable mDrawable;
		View mView;
		boolean mIsMain;
		
		public ImageFetcher(View convertView, Product p, boolean isMain) {
			mView = convertView;
			mProduct = p;
			mIsMain = isMain; 
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
			if (mIsMain) {
				ImageView iv = (ImageView) mView.findViewById(R.id.gallery_item_imageView);
				iv.setImageDrawable(mDrawable);
			} else {
				ImageView iv = (ImageView) mView.findViewById(R.id.product_image);
				TextView tv0 = (TextView) mView.findViewById(R.id.style_id);
				TextView tv1 = (TextView) mView.findViewById(R.id.product_id);
				TextView tv2 = (TextView) mView.findViewById(R.id.color_id);
				TextView tv3 = (TextView) mView.findViewById(R.id.brand_name);
				TextView tv4 = (TextView) mView.findViewById(R.id.product_name);
				TextView tv5 = (TextView) mView.findViewById(R.id.price);
				TextView tv6 = (TextView) mView.findViewById(R.id.original_price);
				TextView tv7 = (TextView) mView.findViewById(R.id.percent_off);
				
				iv.setImageDrawable(mDrawable);
				tv0.setText(mProduct.getStyleId());
				tv1.setText(mProduct.getProductId());
				tv2.setText(mProduct.getColorId());
				tv3.setText(mProduct.getBrandName());
				tv4.setText(mProduct.getProductName());
				tv5.setText(mProduct.getPrice());
				tv6.setText(mProduct.getOriginalPrice());
				tv7.setText(mProduct.getPercentOff());
			}
			return;
		}
		
	}
	
}
