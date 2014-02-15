package com.setoalan.zappossale;

public class Product {

	private String mProductId;
	private String mBrandName;
	private String mProductName;
	private String mStyleId;
	private String mPercentOff;
	private String mImageUrl;
	
	public String getProductId() {
		return mProductId;
	}
	
	public void setProductId(String productId) {
		mProductId = productId;
	}
	
	public String getBrandName() {
		return mBrandName;
	}
	
	public void setBrandName(String brandName) {
		mBrandName = brandName;
	}
	
	public String getProductName() {
		return mProductName;
	}
	
	public void setProductName(String productName) {
		mProductName = productName;
	}
	
	public String getStyleId() {
		return mStyleId;
	}
	
	public void setStyleId(String styleId) {
		mStyleId = styleId;
	}
	
	public String getPercentOff() {
		return mPercentOff;
	}
	
	public void setPercentOff(String percentOff) {
		mPercentOff = percentOff;
	}

	public String getImageUrl() {
		return mImageUrl;
	}

	public void setImageUrl(String imageUrl) {
		mImageUrl = imageUrl;
	}
	
}
