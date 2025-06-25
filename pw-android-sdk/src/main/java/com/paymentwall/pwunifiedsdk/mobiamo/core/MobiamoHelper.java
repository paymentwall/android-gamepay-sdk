package com.paymentwall.pwunifiedsdk.mobiamo.core;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.paymentwall.pwunifiedsdk.mobiamo.res.values.StringResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;


enum Language {
	EN("en");

	private final String name;

	private Language(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}


public class MobiamoHelper {
	public static final int MESSAGE_STATUS_BILLED = 0;
	public static final int MESSAGE_STATUS_FAILED = 1;
	public static final int MESSAGE_STATUS_NOT_SENT = 2;
	public static final int MESSAGE_STATUS_PENDING = 3;

	private static final String MSG_NO_INTERNET_CONNECTION = "NO_INTERNET_CONNECTION";
	private static final String MSG_USER_DID_NOT_ACCEPT_AND_PAY = "USER_DID_NOT_ACCEPT_AND_PAY";
	private static final String MSG_NO_SIM = "NO_SIM";
	private static final String MSG_TRANSMISSION_SUCCESS = "TRANSMISSION_SUCCESS";
	private static final String MSG_WAITING = "WAITING";
	private static final String MSG_TRANSACTION_SUCCESS = "TRANSACTION_SUCCESS";
	private static final String MSG_TRANSACTION_FAIL = "TRANSACTION_FAIL";
	private static final String MSG_NO_REQUEST_FOUND = "NO_REQUEST_FOUND";
	private static final String MSG_PURCHASE = "PURCHASE";
	private static final String MSG_CONFIRM = "CONFIRM";
	private static final String MSG_ACCEPT_AND_PAY = "ACCEPT & BUY";
	private static final String MSG_CANCEL = "CANCEL";
	private static final String MSG_SEND_SMS_UNKNOWN = "SEND_SMS_UNKNOWN";
	private static final String MSG_SMS_NOT_SEND = "SMS_NOT_SEND";
	private static final String MSG_SEND_SMS_SUCCESS = "SEND_SMS_SUCCESS";
	private static final String MSG_SEND_SMS_FAILED = "SEND_SMS_FAILED";
	private static final String MSG_SEND_SMS_RADIO_OFF = "SEND_SMS_RADIO_OFF";
	private static final String MSG_SEND_SMS_NO_PDU = "SEND_SMS_NO_PDU";
	private static final String MSG_SEND_SMS_NO_SERVICE = "SEND_SMS_NO_SERVICE";
	private static final String MSG_ERROR_GET_SIM_ISO = "ERROR_GET_SIM_ISO";
	private static final String MSG_ERROR_GET_LOCALE = "ERROR_GET_LOCALE";
	private static final String MSG_ERROR_GET_MCC_MNC = "ERROR_GET_MCC_MNC";
	private static final String MSG_ERROR_OPERATOR_NOT_SUPPORT = "ERROR_OPERATOR_NOT_SUPPORT";
	private static final String MSG_GET_PRICE_POINT_SUCCESS = "GET_PRICE_POINT_SUCCESS";
	private static final String MSG_ERROR_GENERAL = "ERROR_GENERAL";
	private static final String MSG_POST_SUCCESS = "POST_SUCCESS";
	private static final String MSG_ERROR_POST = "ERROR_POST";
	private static final String MSG_GET_SUCCESS = "GET_SUCCESS";
	private static final String MSG_ERROR_GET_METHOD = "ERROR_GET_METHOD";
	private static final String MSG_ERROR_MSG_NOT_SEND = "ERROR_MSG_NOT_SEND";

	private static String currentLocale;
	private static boolean isStringLoaded;
	public static Context currentContext;
	private static boolean isTestMode;
	private static Map<String, String> stringResource;


	public static void setContext(Context context) {
		currentContext = context;
		loadStringFile();
	}

	public static void setLocale(String locale) {
		currentLocale = locale;
	}

	public static String getString(Message msg) {
		if (!isStringLoaded)
			return "Language unknown";

		switch (msg) {
		case NO_INTERNET_CONNECTION:
			return stringResource.get(MSG_NO_INTERNET_CONNECTION);
		case USER_DID_NOT_ACCEPT_AND_PAY:
			return stringResource.get(MSG_USER_DID_NOT_ACCEPT_AND_PAY);
		case NO_SIM:
			return stringResource.get(MSG_NO_SIM);
		case TRANSMISSION_SUCCESS:
			return stringResource.get(MSG_TRANSMISSION_SUCCESS);
		case WAITING:
			return stringResource.get(MSG_WAITING);
		case TRANSACTION_FAIL:
			return stringResource.get(MSG_TRANSACTION_FAIL);
		case TRANSACTION_SUCCESS:
			return stringResource.get(MSG_TRANSACTION_SUCCESS);
		case NO_REQUEST_FOUND:
			return stringResource.get(MSG_NO_REQUEST_FOUND);
		case PURCHASE:
			return stringResource.get(MSG_PURCHASE);
		case CONFIRM:
			return stringResource.get(MSG_CONFIRM);
		case ACCEPT_AND_PAY:
			return stringResource.get(MSG_ACCEPT_AND_PAY);
		case CANCEL:
			return stringResource.get(MSG_CANCEL);
		case SEND_SMS_UNKNOWN:
			return stringResource.get(MSG_SEND_SMS_UNKNOWN);
		case SMS_NOT_SEND:
			return stringResource.get(MSG_SMS_NOT_SEND);
		case SEND_SMS_SUCCESS:
			return stringResource.get(MSG_SEND_SMS_SUCCESS);
		case SEND_SMS_FAILED:
			return stringResource.get(MSG_SEND_SMS_FAILED);
		case SEND_SMS_RADIO_OFF:
			return stringResource.get(MSG_SEND_SMS_RADIO_OFF);
		case SEND_SMS_NO_PDU:
			return stringResource.get(MSG_SEND_SMS_NO_PDU);
		case SEND_SMS_NO_SERVICE:
			return stringResource.get(MSG_SEND_SMS_NO_SERVICE);
		case ERROR_GET_SIM_ISO:
			return stringResource.get(MSG_ERROR_GET_SIM_ISO);
		case ERROR_GET_LOCALE:
			return stringResource.get(MSG_ERROR_GET_LOCALE);
		case ERROR_GET_MCC_MNC:
			return stringResource.get(MSG_ERROR_GET_MCC_MNC);
		case ERROR_OPERATOR_NOT_SUPPORT:
			return stringResource.get(MSG_ERROR_OPERATOR_NOT_SUPPORT);
		case GET_PRICE_POINT_SUCCESS:
			return stringResource.get(MSG_GET_PRICE_POINT_SUCCESS);
		case ERROR_GENERAL:
			return stringResource.get(MSG_ERROR_GENERAL);
		case POST_SUCCESS:
			return stringResource.get(MSG_POST_SUCCESS);
		case ERROR_POST:
			return stringResource.get(MSG_ERROR_POST);
		case GET_SUCCESS:
			return stringResource.get(MSG_GET_SUCCESS);
		case ERROR_GET_METHOD:
			return stringResource.get(MSG_ERROR_GET_METHOD);
		case ERROR_MSG_NOT_SEND:
			return stringResource.get(MSG_ERROR_MSG_NOT_SEND);
		default:
			return "Message unknown";
		}
	}

	public static void setTestModeEnabled(boolean enabled) {
		isTestMode = enabled;
	}

	public static boolean IsTestMode() {
		return isTestMode;
	}

	public static void showToast(String msg) {
		if (currentContext != null)
			Toast.makeText(currentContext, msg, Toast.LENGTH_LONG).show();
	}

	static void release() {
		currentContext = null;
		stringResource = null;
		isStringLoaded = false;
		currentLocale = "";
		isTestMode = false;
	}

	public static boolean hasConnectivity() {
		if (currentContext == null)
			return false;

		ConnectivityManager cm = (ConnectivityManager) currentContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null)
			return false;

		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnected();
	}

	public static boolean hasSimReady(Activity activity) {
		TelephonyManager telMgr = (TelephonyManager) activity
				.getSystemService(Context.TELEPHONY_SERVICE);
		if(telMgr!=null) {
			int simState = telMgr.getSimState();
			boolean isReady = false;
			switch (simState) {
				case TelephonyManager.SIM_STATE_ABSENT:
					break;
				case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
					break;
				case TelephonyManager.SIM_STATE_PIN_REQUIRED:
					break;
				case TelephonyManager.SIM_STATE_PUK_REQUIRED:
					break;
				case TelephonyManager.SIM_STATE_READY:
					isReady = true;
					break;
				case TelephonyManager.SIM_STATE_UNKNOWN:
					break;
			}
			return isReady;
		} else return false;
	}

	private static void loadStringFile() {
		if (isStringLoaded)
			return;
		stringResource = StringResources.STRINGS;
		isStringLoaded = true;
	}

	public static boolean isRoamingState(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String simOperator = tm.getSimOperator();
		String networkOperator = tm.getNetworkOperator();
		if ((networkOperator.trim().length() == 0) || (networkOperator == null)) {
			return false;
		}

		return !simOperator.equalsIgnoreCase(networkOperator);
	}

	public static ArrayList<?> sortPrices(ArrayList<?> list) {
		float[] pricesArr = new float[list.size()];
		for (int i = 0; i < list.size(); i++) {
			PriceObject object = (PriceObject) list.get(i);
			String value = object.getKeyValue();

			String strFloat = value.replace(",", "");
			pricesArr[i] = Float.parseFloat(strFloat);
		}

		for (int i = 0; i < pricesArr.length - 1; i++) {
			for (int j = i + 1; j < pricesArr.length; j++) {
				if (pricesArr[i] > pricesArr[j]) {
					Collections.swap(list, i, j);
				}
			}
		}
		return list;
	}

	public static String[] createPricePointList(ArrayList<?> list) {
		String[] str = new String[list.size()];

		for (int i = 0; i < list.size(); i++) {
			PriceObject object = (PriceObject) list.get(i);
			String value = object.getKeyValue();
			str[i] = value + " " + object.getCurrencyValue();
		}
		return str;
	}

	public static String buildRequestUrl(Map<String, String> params, String url) {
		boolean isFirst = true;
		StringBuilder builder = new StringBuilder(url);
		for (Iterator<Map.Entry<String, String>> iterator = params.entrySet()
				.iterator(); iterator.hasNext();) {
			Map.Entry<String, String> entry = iterator.next();
			if (isFirst) {
				builder.append("?");
				isFirst = false;
			}
			String keyValuePair = String.format("%s=%s", entry.getKey(),
					entry.getValue());
			builder.append(keyValuePair);
			if (iterator.hasNext()) {
				builder.append("&");
			}
		}

		Log.i("REQUEST_URL", builder.toString());

		return builder.toString();
	}
}