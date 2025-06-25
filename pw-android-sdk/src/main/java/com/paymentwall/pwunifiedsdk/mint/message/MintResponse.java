package com.paymentwall.pwunifiedsdk.mint.message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class MintResponse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -215066240672365070L;
	
	public static final Integer SUCCESSFUL = 1;
	public static final Integer FAILED = 0;
	
	private Double changeAmount;
	private String changeCurrency;
	private Integer success;
	private String errorCode;
	private String errorMessage;

	public Double getChangeAmount() {
		return changeAmount;
	}

	public void setChangeAmount(Double changeAmount) {
		this.changeAmount = changeAmount;
	}

	public String getChangeCurrency() {
		return changeCurrency;
	}

	public void setChangeCurrency(String changeCurrency) {
		this.changeCurrency = changeCurrency;
	}

	public Integer getSuccess() {
		return success;
	}

	public void setSuccess(Integer success) {
		this.success = success;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public MintResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "PWSDKMintResponse [changeAmount=" + changeAmount
				+ ", changeCurrency=" + changeCurrency + ", success=" + success
				+ ", errorCode=" + errorCode + ", errorMessage=" + errorMessage
				+ "]";
	}

	public MintResponse(String jsonString) throws JSONException {
		super();
		JSONObject jsonObject = new JSONObject(jsonString);
		if(jsonObject.has("change_amount"))
		{
			this.changeAmount = jsonObject.getDouble("change_amount");
		}
		if(jsonObject.has("change_currency"))
		{
			this.changeCurrency = jsonObject.getString("change_currency");
		}
		if(jsonObject.has("success"))
		{
			this.success = jsonObject.getInt("success");
		}
		if(jsonObject.has("error_code"))
		{
			this.errorCode = jsonObject.getString("error_code");
		}
		if(jsonObject.has("error_message"))
		{
			this.errorMessage = jsonObject.getString("error_message");
		}
	}
	
	public static class ERROR_CODE {
		public static final int GENERAL_INTERNAL_ERROR = 1001;
		public static final int APPLICATION_NOT_LOADED = 1002;
		public static final int USER_BANNED = 1003;
		public static final int EMPTY_E_PIN = 2001;
		public static final int EMPTY_PROJECT_KEY = 2002;
		public static final int WRONG_AMOUNT = 2003;
		public static final int WRONG_CURRENCY = 2004;
		public static final int EPIN_IS_NOT_ARRAY = 2005;
		public static final int EMPTY_USER_ID = 2006;
		public static final int MINT_IS_NOT_ACTIVATED_FOR_THIS_PROJECT = 3001;
		public static final int EPIN_EXPIRED = 3002;
		public static final int EPIN_IS_NOT_ACTIVATED = 3003;
		public static final int EPIN_DUPLICATE = 3004;
		public static final int EPIN_IS_NOT_AVAILABLE_FOR_THIS_APPLICATION = 3005;
		public static final int EPIN_IS_INVALID = 3006;
		public static final int ONLY_ONE_EPIN_IS_ALLOWED = 3007;
		public static final int EPIN_TRACKING_FAILURE = 3008;
		public static final int EPIN_INSUFFICIENT_FUNDS = 3009;
		public static final int MAXIMUM_ATTEMPTS_EXCEEDED = 4000;
	}
	
	public static String errorCodeToString(int errorCode)
	{
		switch (errorCode) {
		case ERROR_CODE.GENERAL_INTERNAL_ERROR: return "General internal error";
		case ERROR_CODE.APPLICATION_NOT_LOADED: return "Application not loaded";
		case ERROR_CODE.USER_BANNED: return "User banned";
		case ERROR_CODE.EMPTY_E_PIN: return "Empty E-Pin";
		case ERROR_CODE.EMPTY_PROJECT_KEY: return "Empty project key";
		case ERROR_CODE.WRONG_AMOUNT: return "Wrong amount";
		case ERROR_CODE.WRONG_CURRENCY: return "Wrong currency";
		case ERROR_CODE.EPIN_IS_NOT_ARRAY: return "Epin is not array";
		case ERROR_CODE.EMPTY_USER_ID: return "Empty user id";
		case ERROR_CODE.MINT_IS_NOT_ACTIVATED_FOR_THIS_PROJECT: return "Mint is not activated for this project";
		case ERROR_CODE.EPIN_EXPIRED: return "Epin expired";
		case ERROR_CODE.EPIN_IS_NOT_ACTIVATED: return "Epin is not activated";
		case ERROR_CODE.EPIN_DUPLICATE: return "Epin duplicate";
		case ERROR_CODE.EPIN_IS_NOT_AVAILABLE_FOR_THIS_APPLICATION: return "Epin is not available for this application";
		case ERROR_CODE.EPIN_IS_INVALID: return "Epin is invalid";
		case ERROR_CODE.ONLY_ONE_EPIN_IS_ALLOWED: return "Only one epin is allowed";
		case ERROR_CODE.EPIN_TRACKING_FAILURE: return "Epin tracking failure";
		case ERROR_CODE.EPIN_INSUFFICIENT_FUNDS: return "Epin insufficient funds";
		case ERROR_CODE.MAXIMUM_ATTEMPTS_EXCEEDED: return "Maximum attempts exceeded";
		
		default:
			return null;
		}		
	}
}
