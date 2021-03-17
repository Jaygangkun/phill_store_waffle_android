package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class SpecificationListItem {

	@SerializedName("is_user_selected")
	@Expose
	private boolean isUserSelected;

	@SerializedName("price")
	@Expose
	private double price;

	@SerializedName("name")
	@Expose
	private String name;

	@SerializedName("_id")
	@Expose
	private String id;

	@SerializedName("is_default_selected")
	@Expose
	private boolean isDefaultSelected;




	public void setIsUserSelected(boolean isUserSelected){
		this.isUserSelected = isUserSelected;
	}

	public boolean isIsUserSelected(){
		return isUserSelected;
	}

	public void setPrice(double price){
		this.price = price;
	}

	public double getPrice(){
		return price;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setIsDefaultSelected(boolean isDefaultSelected){
		this.isDefaultSelected = isDefaultSelected;
	}

	public boolean isIsDefaultSelected(){
		return isDefaultSelected;
	}


}