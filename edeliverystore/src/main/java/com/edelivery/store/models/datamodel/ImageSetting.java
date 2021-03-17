package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageSetting{

	@SerializedName("item_image_max_width")
	private int itemImageMaxWidth;

	@SerializedName("unique_id")
	private int uniqueId;

	@SerializedName("item_image_min_height")
	private int itemImageMinHeight;

	@SerializedName("product_image_max_width")
	private int productImageMaxWidth;

	@SerializedName("product_image_max_height")
	private int productImageMaxHeight;

	@SerializedName("product_image_ratio")
	private double productImageRatio;

	@SerializedName("item_image_ratio")
	private double itemImageRatio;

	@SerializedName("item_image_min_width")
	private int itemImageMinWidth;

	@SerializedName("product_image_min_width")
	private int productImageMinWidth;

	@SerializedName("_id")
	private String id;

	@SerializedName("product_image_min_height")
	private int productImageMinHeight;

	@SerializedName("item_image_max_height")
	private int itemImageMaxHeight;

	@SerializedName("image_type")
	private List<String> imageType;

	@SerializedName("delivery_image_ratio")
	private double deliveryImageRatio;

	@SerializedName("delivery_image_max_width")
	private int deliveryImageMaxWidth;

	@SerializedName("delivery_image_max_height")
	private int deliveryImageMaxHeight;

	@SerializedName("delivery_image_min_width")
	private int deliveryImageMinWidth;

	@SerializedName("delivery_image_min_height")
	private int deliveryImageMinHeight;

	public void setItemImageMaxWidth(int itemImageMaxWidth){
		this.itemImageMaxWidth = itemImageMaxWidth;
	}

	public int getItemImageMaxWidth(){
		return itemImageMaxWidth;
	}

	public void setUniqueId(int uniqueId){
		this.uniqueId = uniqueId;
	}

	public int getUniqueId(){
		return uniqueId;
	}

	public void setItemImageMinHeight(int itemImageMinHeight){
		this.itemImageMinHeight = itemImageMinHeight;
	}

	public int getItemImageMinHeight(){
		return itemImageMinHeight;
	}

	public void setProductImageMaxWidth(int productImageMaxWidth){
		this.productImageMaxWidth = productImageMaxWidth;
	}

	public int getProductImageMaxWidth(){
		return productImageMaxWidth;
	}

	public void setProductImageMaxHeight(int productImageMaxHeight){
		this.productImageMaxHeight = productImageMaxHeight;
	}

	public int getProductImageMaxHeight(){
		return productImageMaxHeight;
	}

	public void setProductImageRatio(double productImageRatio){
		this.productImageRatio = productImageRatio;
	}

	public double getProductImageRatio(){
		return productImageRatio;
	}

	public void setItemImageRatio(double itemImageRatio){
		this.itemImageRatio = itemImageRatio;
	}

	public double getItemImageRatio(){
		return itemImageRatio;
	}

	public void setItemImageMinWidth(int itemImageMinWidth){
		this.itemImageMinWidth = itemImageMinWidth;
	}

	public int getItemImageMinWidth(){
		return itemImageMinWidth;
	}

	public void setProductImageMinWidth(int productImageMinWidth){
		this.productImageMinWidth = productImageMinWidth;
	}

	public int getProductImageMinWidth(){
		return productImageMinWidth;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setProductImageMinHeight(int productImageMinHeight){
		this.productImageMinHeight = productImageMinHeight;
	}

	public int getProductImageMinHeight(){
		return productImageMinHeight;
	}

	public void setItemImageMaxHeight(int itemImageMaxHeight){
		this.itemImageMaxHeight = itemImageMaxHeight;
	}

	public int getItemImageMaxHeight(){
		return itemImageMaxHeight;
	}

	public void setImageType(List<String> imageType){
		this.imageType = imageType;
	}

	public List<String> getImageType(){
		return imageType;
	}

	public double getDeliveryImageRatio() {
		return deliveryImageRatio;
	}

	public void setDeliveryImageRatio(double deliveryImageRatio) {
		this.deliveryImageRatio = deliveryImageRatio;
	}

	public int getDeliveryImageMaxWidth() {
		return deliveryImageMaxWidth;
	}

	public void setDeliveryImageMaxWidth(int deliveryImageMaxWidth) {
		this.deliveryImageMaxWidth = deliveryImageMaxWidth;
	}

	public int getDeliveryImageMaxHeight() {
		return deliveryImageMaxHeight;
	}

	public void setDeliveryImageMaxHeight(int deliveryImageMaxHeight) {
		this.deliveryImageMaxHeight = deliveryImageMaxHeight;
	}

	public int getDeliveryImageMinWidth() {
		return deliveryImageMinWidth;
	}

	public void setDeliveryImageMinWidth(int deliveryImageMinWidth) {
		this.deliveryImageMinWidth = deliveryImageMinWidth;
	}

	public int getDeliveryImageMinHeight() {
		return deliveryImageMinHeight;
	}

	public void setDeliveryImageMinHeight(int deliveryImageMinHeight) {
		this.deliveryImageMinHeight = deliveryImageMinHeight;
	}

	@Override
 	public String toString(){
		return 
			"ImageSetting{" + 
			"item_image_max_width = '" + itemImageMaxWidth + '\'' + 
			",unique_id = '" + uniqueId + '\'' + 
			",item_image_min_height = '" + itemImageMinHeight + '\'' + 
			",product_image_max_width = '" + productImageMaxWidth + '\'' + 
			",product_image_max_height = '" + productImageMaxHeight + '\'' + 
			",product_image_ratio = '" + productImageRatio + '\'' + 
			",item_image_ratio = '" + itemImageRatio + '\'' + 
			",item_image_min_width = '" + itemImageMinWidth + '\'' + 
			",product_image_min_width = '" + productImageMinWidth + '\'' + 
			",_id = '" + id + '\'' + 
			",product_image_min_height = '" + productImageMinHeight + '\'' + 
			",item_image_max_height = '" + itemImageMaxHeight + '\'' + 
			",image_type = '" + imageType + '\'' + 
			"}";
		}
}