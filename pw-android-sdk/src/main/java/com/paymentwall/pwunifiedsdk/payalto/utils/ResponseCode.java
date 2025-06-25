package com.paymentwall.pwunifiedsdk.payalto.utils;

public final class ResponseCode {
	/**
	 * The payment was confirmed
	 **/
	public static final int SUCCESSFUL = 1;

	/**
	 * Invalid input information
	 **/
	public static final int ERROR = 2;

	/**
	 * A request was sent to PW server, but the request has been rejected
	 **/
	public static final int FAILED = 3;

	/**
	 * The payment request was received but it is not yet confirmed
	 **/
	public static final int PROCESSING = 4;

	/**
	 * The payment was cancelled
	 **/
	public static final int CANCEL = 5;
}
