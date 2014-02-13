package com.setoalan.zappossale;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ZapposSaleActivity extends Activity {

	private ArrayList<ZapposProduct> mProducts;
	
	EditText mProduct;
	Button mSearch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zappos_sale);
		
		mProducts = new ArrayList<ZapposProduct>();
		
		mProduct = (EditText) findViewById(R.id.product);
		mSearch = (Button) findViewById(R.id.search);
		mSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new FetchItemsTask().execute();
			}
		});
	}
	
	private class FetchItemsTask extends AsyncTask<Void, Void, ArrayList<ZapposProduct>> {
		
		@Override
		protected ArrayList<ZapposProduct> doInBackground(Void... params) {
			return new ZapposFetcher().fetchItems();
		}
		
		@Override
		protected void onPostExecute(ArrayList<ZapposProduct> result) {
			mProducts = result;
		}
		
	}
	
}
