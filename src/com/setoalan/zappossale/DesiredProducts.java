package com.setoalan.zappossale;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class DesiredProducts extends SQLiteOpenHelper {
	
	private static final String DB_NAME = "zappossales";
	private static final String TABLE_NAME = "desired_products";
	private static final String KEY_ID = "id";
	private static final String PRODUCT_ID = "product_id";
	private static final String STYLE_ID = "style_id";
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
			KEY_ID + " INTEGER PRIMARY KEY," +
			PRODUCT_ID + TEXT_TYPE + COMMA_SEP + 
			STYLE_ID + TEXT_TYPE + " )";
	private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;
	private static final int DB_VERSION = 1;
	
	public DesiredProducts(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE);
		onCreate(db);
	}
	
	public void addProduct(int position) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(PRODUCT_ID, ZapposSaleFragment.mProducts.get(position).getProductId());
		values.put(STYLE_ID, ZapposSaleFragment.mProducts.get(position).getStyleId());
		
		db.insert(TABLE_NAME, null, values);
		db.close();
	}
	
	public Product getProduct(int style_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_NAME, new String[] {
				KEY_ID, PRODUCT_ID, STYLE_ID }, STYLE_ID + "=?",
				new String[] { String.valueOf(style_id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		
		Product product = new Product();
		product.setProductId(cursor.getString(1));
		product.setStyleId(cursor.getString(2));
		
		return product;
	}
	
	public void deleteProduct(int style_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, STYLE_ID + " = ?",
				new String[] { String.valueOf(style_id) });
		db.close();
	}
	
	public List<Product> getAllProducts() {
		List<Product> productList = new ArrayList<Product>();
		
		String selectQuery = "SELECT * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				Product product = new Product();
				product.setProductId(cursor.getString(1));
				product.setStyleId(cursor.getString(2));
				productList.add(product);
			} while (cursor.moveToNext());
		}
		
		return productList;
	}
	
	public int getProductCount() {
		String countQuery = "SELECT * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count =  cursor.getCount();
		cursor.close();
		
		return count;
	}
	
}
