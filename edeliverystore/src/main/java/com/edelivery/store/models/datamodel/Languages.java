package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Languages implements Parcelable {

	@SerializedName("code")
	private String code;

	@SerializedName("name")
	private String name;

	@SerializedName("string_file_path")
	private String stringFilePath;
	@SerializedName("is_visible")
	@Expose
	private boolean isVisible;

	protected Languages(Parcel in) {
		code = in.readString();
		name = in.readString();
		stringFilePath = in.readString();
		isVisible = in.readByte() != 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(code);
		dest.writeString(name);
		dest.writeString(stringFilePath);
		dest.writeByte((byte) (isVisible ? 1 : 0));
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Languages> CREATOR = new Creator<Languages>() {
		@Override
		public Languages createFromParcel(Parcel in) {
			return new Languages(in);
		}

		@Override
		public Languages[] newArray(int size) {
			return new Languages[size];
		}
	};

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setStringFilePath(String stringFilePath){
		this.stringFilePath = stringFilePath;
	}

	public String getStringFilePath(){
		return stringFilePath;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean visible) {
		isVisible = visible;
	}

	@Override
 	public String toString(){
		return 
			"Languages{" +
			"code = '" + code + '\'' + 
			",name = '" + name + '\'' + 
			",string_file_path = '" + stringFilePath + '\'' + 
			"}";
		}
}