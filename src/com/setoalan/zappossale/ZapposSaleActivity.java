package com.setoalan.zappossale;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ZapposSaleActivity extends Activity {

	EditText mProduct;
	Button mSearch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zappos_sale);
		
		mProduct = (EditText) findViewById(R.id.product);
		mSearch = (Button) findViewById(R.id.search);
	}
	
}
