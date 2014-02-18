package com.setoalan.zappossale;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ZapposSaleFragment extends Fragment {

	public static ArrayList<Product> mProducts;
	public static String PRODUCT;// = "7515478";
	public static View mProgressContainer;
	public static DesiredProducts db;
	private EditText mProduct;
	private Button mSearch;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mProducts = new ArrayList<Product>();
		db = new DesiredProducts(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().getActionBar().setIcon(R.drawable.zappos_logo_square);
		
		View v = inflater.inflate(R.layout.fragment_zappos_sale, container, false);
		
		mProduct = (EditText) v.findViewById(R.id.product);
		mSearch = (Button) v.findViewById(R.id.search);
		mSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mProduct.getText().toString().equals("")) {
					Toast.makeText(getActivity(), "Please enter a productID or productName", Toast.LENGTH_SHORT).show();
				} else {
					mProgressContainer.setVisibility(View.VISIBLE);
					PRODUCT = mProduct.getText().toString();
					new FetchItemsTask().execute();
				}
			}
		});
		mProgressContainer =  v.findViewById(R.id.progressContainer);
		mProgressContainer.setVisibility(View.INVISIBLE);
		
		return v;
	}
	
	private class FetchItemsTask extends AsyncTask<Void, Void, ArrayList<Product>> {
		
		@Override
		protected ArrayList<Product> doInBackground(Void... params) {
			return new ProductFetcher().fetchItems();
		}
		
		@Override
		protected void onPostExecute(ArrayList<Product> result) {
			mProducts = result;
			Intent i = new Intent(getActivity(), ProductGalleryActivity.class);
			startActivity(i);
		}
		
	}
	
}
