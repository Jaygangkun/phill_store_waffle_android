package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SurgeHours {

	@SerializedName("surge_start_hour")
	@Expose
	private String surgeStartHour;

	@SerializedName("surge_multiplier")
	@Expose
	private double surgeMultiplier;

	@SerializedName("surge_end_hour")
	@Expose
	private String surgeEndHour;

	public void setSurgeStartHour(String surgeStartHour){
		this.surgeStartHour = surgeStartHour;
	}

	public String getSurgeStartHour(){
		return surgeStartHour;
	}

	public void setSurgeMultiplier(double surgeMultiplier){
		this.surgeMultiplier = surgeMultiplier;
	}

	public double getSurgeMultiplier(){
		return surgeMultiplier;
	}

	public void setSurgeEndHour(String surgeEndHour){
		this.surgeEndHour = surgeEndHour;
	}

	public String getSurgeEndHour(){
		return surgeEndHour;
	}


}