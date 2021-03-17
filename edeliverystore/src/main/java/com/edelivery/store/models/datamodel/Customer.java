package com.edelivery.store.models.datamodel;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Customer {

	@SerializedName("server_token")
	private String serverToken;

	@SerializedName("_id")
	private String id;

	public void setServerToken(String serverToken){
		this.serverToken = serverToken;
	}

	public String getServerToken(){
		return serverToken;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"Customer{" +
			"server_token = '" + serverToken + '\'' + 
			",_id = '" + id + '\'' + 
			"}";
		}
}