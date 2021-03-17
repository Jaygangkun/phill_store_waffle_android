package com.edelivery.store.models.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class OTPResponse {

	@SerializedName("success")
	@Expose
	private boolean success;

	@SerializedName("otp_for_sms")
	@Expose
	private String otpForSms;

	@SerializedName("otp_for_email")
	@Expose
	private String otpForEmail;

	@SerializedName("message")
	@Expose
	private int message;

	@SerializedName("error_code")
	@Expose
	private int errorCode;

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setOtpForSms(String otpForSms){
		this.otpForSms = otpForSms;
	}

	public String getOtpForSms(){
		return otpForSms;
	}

	public void setOtpForEmail(String otpForEmail){
		this.otpForEmail = otpForEmail;
	}

	public String getOtpForEmail(){
		return otpForEmail;
	}

	public void setMessage(int message){
		this.message = message;
	}

	public int getMessage(){
		return message;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}