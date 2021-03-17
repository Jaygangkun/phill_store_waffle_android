package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.OrderData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class HistoryResponse{

	@SerializedName("success")
	@Expose
	private boolean success;

	@SerializedName("message")
	@Expose
	private int message;

	@SerializedName("order_list")
	@Expose
	private ArrayList<OrderData> orderList;

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

	public void setOrderList(ArrayList<OrderData> orderList){
		this.orderList = orderList;
	}

	public ArrayList<OrderData> getOrderList(){
		return orderList;
	}

	@Override
 	public String toString(){
		return 
			"HistoryResponse{" + 
			"success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			",order_list = '" + orderList + '\'' + 
			"}";
		}

	@SerializedName("error_code")
	@Expose
	private int errorCode;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode= errorCode;
	}
}