package com.paymentwall.pwunifiedsdk.payalto.utils;

public final class ApiType {
	/**
	* Please use ApiType.VIRTUAL_CURRENCY instead
	**/
	@Deprecated
	public static final String PS = "ps";

	public static final String PS_NAME = "ps_name";

	/**
	 * Please use ApiType.DIGITAL_GOODS instead
	 **/
	@Deprecated
	public static final String SUBSCRIPTION = "subscription";
	public static final String CART = "cart";
	public static final String VIRTUAL_CURRENCY = "ps";
	public static final String DIGITAL_GOODS = "subscription";

}
