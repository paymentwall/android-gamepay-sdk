package com.paymentwall.pwunifiedsdk.payalto.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.paymentwall.pwunifiedsdk.util.SmartLog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Currency;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PayAltoMiscUtils {
    public static String md5(String inputString) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(inputString.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static String sha256(String inputString) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] array = md.digest(inputString.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

	/*public static String mapToString(Map<String, String> parameters)
	{
		for(Entry<String, String> entry: parameters.entrySet())
		{
			
		}
		return null;
	}*/

    public static String urlStringEncode(String input) {
        SmartLog.i("urlStringEncode input=" + input);
        try {
            return URLEncoder.encode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    public static Map<String, String> sortMap(Map<String, String> aMap) {
        Map<String, String> sortedMap = new TreeMap<String, String>(aMap);
        return sortedMap;
    }

    public static String printMap(Map<String, String> params) {
        StringBuilder out = new StringBuilder();
        final Set<Entry<String, String>> entrySet = params.entrySet();
        for (Entry<String, String> entry : entrySet) {
            out.append('|');
            out.append(entry.getKey());
            out.append('=');
            out.append(entry.getValue());

        }
        return out.toString();
    }

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /**
     * Generate a value suitable for use in View.setId(int).
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    public static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public static boolean isValidCurrency(String currency) {
        try {
            Currency.getInstance(currency);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getQuery(Map<String, String> parameters) throws Exception {
        try {
            TreeMap<String, String> params = new TreeMap<String, String>(parameters);
            StringBuilder query = new StringBuilder();
            query.append('?');
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first) first = false;
                else query.append('&');
                query.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                query.append('=');
                query.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            return query.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static Bundle stringMapToBundle(Map<String, String> map) {
        if (map == null || map.isEmpty()) return new Bundle();
        else {
            Bundle bundle = new Bundle();
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry) it.next();
                bundle.putString(entry.getKey(), entry.getValue());
            }
            return bundle;
        }
    }

    public static Bundle intMapToBundle(Map<Integer, String> map) {
        if (map == null || map.isEmpty()) return new Bundle();
        else {
            Bundle bundle = new Bundle();
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, String> entry = (Map.Entry<Integer, String>) it.next();
                bundle.putString(String.valueOf(entry.getKey()), entry.getValue());
            }
            return bundle;
        }
    }

    public static Bundle pricesToBundle(Map<Integer, Float> map) {
        if (map == null || map.isEmpty()) return new Bundle();
        else {
            Bundle bundle = new Bundle();
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, Float> entry = (Map.Entry<Integer, Float>) it.next();
                bundle.putFloat(String.valueOf(entry.getKey()), entry.getValue());
            }
            return bundle;
        }
    }

    public static TreeMap bundleToPrices(Bundle bundle) {
        if (bundle == null || bundle.isEmpty()) return null;
        else {
            TreeMap<Integer, Float> map = new TreeMap<Integer, Float>();
            final Set<String> keySet = bundle.keySet();
            for (String key : keySet) {
                map.put(Integer.valueOf(key), bundle.getFloat(key));
            }
            return map;
        }
    }

    public static TreeMap bundleToStringTreeMap(Bundle bundle) {
        if (bundle == null || bundle.isEmpty()) return null;
        else {
            TreeMap<String, String> map = new TreeMap<String, String>();
            final Set<String> keySet = bundle.keySet();
            for (String key : keySet) {
                map.put(key, bundle.getString(key));
            }
            return map;
        }
    }

    public static TreeMap bundleToIntegerTreeMap(Bundle bundle) {
        if (bundle == null || bundle.isEmpty()) return null;
        else {
            TreeMap<Integer, String> map = new TreeMap<Integer, String>();
            final Set<String> keySet = bundle.keySet();
            for (String key : keySet) {
                map.put(Integer.valueOf(key), bundle.getString(key));
            }
            return map;
        }
    }

    public static String val(Object object) {
        if (object == null) throw new NullPointerException("Value must be non null");
        if (object instanceof String) return (String) object;
        else if (object instanceof Integer
                || object instanceof Float
                || object instanceof Double
                || object instanceof Long) {
            return String.valueOf(object);
        } else if (object instanceof Boolean) {
            if ((Boolean) object) {
                return "1";
            } else {
                return "0";
            }
        } else {
            throw new IllegalArgumentException("Data type is not accepted");
        }
    }

    public static boolean urlEqual(String url, String successfulUrl) {
        if (url == null || successfulUrl == null) throw new NullPointerException("Invalid URL");
        Uri uri1 = Uri.parse(url);
        Uri uri2 = Uri.parse(successfulUrl);
        if (uri1 == null || uri2 == null) return false;
        return (uri1.getScheme().equalsIgnoreCase(uri2.getScheme()) && uri2.getHost().equalsIgnoreCase(uri2.getHost()));


        /*if (successfulUrl.toLowerCase().equals(url.toLowerCase())) return true;
        Uri successfulUri = Uri.parse(successfulUrl.toLowerCase());
        Uri uri = Uri.parse(url.toLowerCase());
        String uriP = uri.getPath();
        String sP = successfulUri.getPath();
        if (uriP != null && uriP.endsWith("/")) uriP = uriP.substring(0, uriP.length() - 1);
        if (sP != null && sP.endsWith("/")) sP = sP.substring(0, sP.length() - 1);
        return (
                uri.getScheme().equals(successfulUri.getScheme())
                        && uri.getAuthority().equals(successfulUri.getAuthority())
                        && (
                        (uri.getQuery() == null && successfulUri.getQuery() == null)
                                || uri.getQuery().equals(successfulUri.getQuery())
                )
                        && ((uriP == null && sP == null) || uriP.equals(sP))
        );*/
    }

    public static float get1Px(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi / 160f;
    }

    public static float dpToPx(Context context, int dp) {
        return dp * get1Px(context);
    }
}
