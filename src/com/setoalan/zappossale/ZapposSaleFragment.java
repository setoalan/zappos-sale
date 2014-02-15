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

	public static ArrayList<Product> mProducts = new ArrayList<Product>();
	
	public static String PRODUCT = "7515478";
	public static String EMAIL = "aseto@umich.edu";
	
	public static View mProgressContainer;
	private EditText mProduct, mEmail;
	private Button mSearch;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_zappos_sale, container, false);
		
		mProduct = (EditText) v.findViewById(R.id.product);
		mEmail = (EditText) v.findViewById(R.id.email);
		mSearch = (Button) v.findViewById(R.id.search);
		mSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mProduct.getText().toString().equals("") || mEmail.getText().toString().equals("")) {
					Toast.makeText(getActivity(), "Please enter a product and/or email.", Toast.LENGTH_SHORT).show();
				} else {
					mProgressContainer.setVisibility(View.VISIBLE);
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
			return new ZapposFetcher().fetchItems();
		}
		
		@Override
		protected void onPostExecute(ArrayList<Product> result) {
			mProducts = result;
			Intent i = new Intent(getActivity(), ZapposProductListActivity.class);
			startActivity(i);
		}
		
	}
	
}
