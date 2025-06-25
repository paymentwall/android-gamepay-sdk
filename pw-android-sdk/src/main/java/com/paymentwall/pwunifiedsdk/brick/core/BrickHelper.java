package com.paymentwall.pwunifiedsdk.brick.core;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by paymentwall on 28/05/15.
 */
public class BrickHelper {
    public static boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }
    public static boolean luhnCheck(String cardNumber) {
        int s1 = 0, s2 = 0;
        String reverse = new StringBuffer(cardNumber).reverse().toString();
        for (int i = 0; i < reverse.length(); i++) {
            int digit = Character.digit(reverse.charAt(i), 10);
            if (i % 2 == 0) {// this is for odd digits, they are 1-indexed in
                // the algorithm
                s1 += digit;
            } else {// add 2 * digit for 0-4, add 2 * digit - 9 for 5-9
                s2 += 2 * digit;
                if (digit >= 5) {
                    s2 -= 9;
                }
            }
        }
        return (s1 + s2) % 10 == 0;
    }

    public static boolean isDateExpired(int expMonth, int expYear, int length) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int scale = currentYear / 100;

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, expMonth);
        if (length == 4) {
            if (expYear < currentYear) {
                return true;
            }
            calendar.set(Calendar.YEAR, expYear);
        } else {
            calendar.set(Calendar.YEAR, scale * 100 + expYear);
        }
        Date date = calendar.getTime();
        try {
            Date today = new Date();
            return date.before(subtractDay(today));
        } catch (Exception e) {
            e.printStackTrace();
            return true;

        }
    }

    public static Date subtractDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    public static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static String urlEncodeUTF8(Map<?,?> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?,?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    urlEncodeUTF8(entry.getKey().toString()),
                    urlEncodeUTF8(entry.getValue().toString())
            ));
        }
        return sb.toString();
    }

    public static BrickToken createTokenFromJson(String json)
            throws BrickError {
        try {
            JSONObject jToken = new JSONObject(json);
            String type = jToken.getString(JsonField.J_TYPE);
            String token = jToken.getString(JsonField.J_TOKEN);
            int expiresIn = jToken.getInt(JsonField.J_EXPIRES_IN);
            int active = jToken.getInt(JsonField.J_ACTIVE);
            JSONObject jCard = jToken.getJSONObject(JsonField.J_CARD);
            String cardType = jCard.getString(JsonField.J_CARD_TYPE);
            String cardLast4 = jCard.getString(JsonField.J_CARD_LAST_4);
            String cardBin = jCard.getString(JsonField.J_CARD_BIN);
            String cardExpMonth = jCard.getString(JsonField.J_CARD_EXP_MONTH);
            String cardExpYear = jCard.getString(JsonField.J_CARD_EXP_YEAR);
            return new BrickToken(type, token, expiresIn, active,
                    new BrickCard(cardType, cardLast4, cardBin, cardExpMonth,
                            cardExpYear));
        } catch (JSONException e) {
            try {
                throw createErrorFromJson(json);
            } catch (BrickError brickError) {
                throw brickError;
            }
        }
    }

    public static BrickError createErrorFromJson(String json)
			throws BrickError {
		try {
			JSONObject jError;
			jError = new JSONObject(json);
			String type = jError.getString(JsonField.J_TYPE);
			String error = jError.getString(JsonField.J_ERROR);
			String code = jError.getString(JsonField.J_CODE);
            BrickResponse response = new BrickResponse();
            response.setCode(code);
            response.setError(error);
            response.setType(type);
//			BrickError brickError = new BrickError("Rejected", BrickError.Kind.REJECTED);
            BrickError brickError = new BrickError(error, BrickError.Kind.REJECTED);
            brickError.setResponse(response);
            return brickError;
		} catch (JSONException e) {
			throw new BrickError("Unknown response error", BrickError.Kind.SERVER);
		}
	}

    private static final class JsonField {
        public static final String J_TYPE = "type";
        public static final String J_TOKEN = "token";
        public static final String J_EXPIRES_IN = "expires_in";
        public static final String J_ACTIVE = "active";
        public static final String J_CARD = "card";
        public static final String J_CARD_TYPE = "type";
        public static final String J_CARD_LAST_4 = "last4";
        public static final String J_CARD_BIN = "bin";
        public static final String J_CARD_EXP_MONTH = "exp_month";
        public static final String J_CARD_EXP_YEAR = "exp_year";
        public static final String J_ERROR = "error";
        public static final String J_CODE = "code";
    }
}
