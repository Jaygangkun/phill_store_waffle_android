package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.WalletRequestDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalletTransactionResponse {

	@SerializedName("success")
	private boolean success;

	@SerializedName("wallet_request_detail")
	private List<WalletRequestDetail> walletRequestDetail;

	@SerializedName("message")
	private int message;

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setWalletRequestDetail(List<WalletRequestDetail> walletRequestDetail){
		this.walletRequestDetail = walletRequestDetail;
	}

	public List<WalletRequestDetail> getWalletRequestDetail(){
		return walletRequestDetail;
	}

	public void setMessage(int message){
		this.message = message;
	}

	public int getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"WalletTransactionResponse{" +
			"success = '" + success + '\'' + 
			",wallet_request_detail = '" + walletRequestDetail + '\'' + 
			",message = '" + message + '\'' + 
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