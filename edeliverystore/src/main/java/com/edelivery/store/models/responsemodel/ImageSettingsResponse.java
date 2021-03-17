package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.ImageSetting;
import com.google.gson.annotations.SerializedName;

public class ImageSettingsResponse{

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private int message;

	@SerializedName("image_setting")
	private ImageSetting imageSetting;

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setMessage(int message){
		this.message = message;
	}

	public int getMessage(){
		return message;
	}

	public void setImageSetting(ImageSetting imageSetting){
		this.imageSetting = imageSetting;
	}

	public ImageSetting getImageSetting(){
		return imageSetting;
	}

	@Override
 	public String toString(){
		return 
			"ImageSettingsResponse{" + 
			"success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			",image_setting = '" + imageSetting + '\'' + 
			"}";
		}
}