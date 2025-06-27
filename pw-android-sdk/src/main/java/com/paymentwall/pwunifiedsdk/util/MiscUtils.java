package com.paymentwall.pwunifiedsdk.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Currency;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MiscUtils {
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

    public static String calculateHmacSha256(String secretKey, String message) {
        try {
//            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
//            Mac mac = Mac.getInstance("HmacSHA256");
//            mac.init(secretKeySpec);
//            byte[] hmacBytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
//            String hash = Base64.encodeToString(hmacBytes, Base64.NO_WRAP);
//            Log.i("SIGN: hash", hash);
//            return hash;
            // Step 2: Create HMAC-SHA256 signature
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            byte[] hashBytes = sha256_HMAC.doFinal(message.getBytes(StandardCharsets.UTF_8));

            // Convert byte array to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
//            SmartLog.i("SIGN: hash", hexString.toString());
            return hexString.toString();
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String rsa(Context context, String inputString) {
        try {
            StringBuilder buf = new StringBuilder();
            InputStream json = context.getAssets().open("rsa_private_key_pkcs8.pem");

            BufferedReader in =
                    new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;

            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            in.close();

            String bufferStr = buf.toString();
            bufferStr = bufferStr.replace("-----BEGIN PRIVATE KEY-----", "");
            bufferStr = bufferStr.replace("-----END PRIVATE KEY-----", "");

            byte[] keyBytes = bufferStr.getBytes();

//            X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.decode(bufferStr.getBytes("utf-8"), Base64.DEFAULT));
//            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(spec);

            PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(Base64.decode(bufferStr.getBytes("utf-8"), Base64.DEFAULT));
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(ks);


            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] encryptedBytes = cipher.doFinal(inputString.getBytes());

            return bytesToString(encryptedBytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String signRsa(Context context, String content, String privateKey) {
        try {

            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                    Base64.decode(privateKey, Base64.DEFAULT));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature
                    .getInstance("SHA1WithRSA");

            signature.initSign(priKey);
            signature.update(content.getBytes("UTF-8"));

            byte[] signed = signature.sign();

            return Base64.encodeToString(signed, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String bytesToString(byte[] b) {
        byte[] b2 = new byte[b.length + 1];
        b2[0] = 1;
        System.arraycopy(b, 0, b2, 1, b.length);
        return new BigInteger(b2).toString(36);
    }

    /*public static String mapToString(Map<String, String> parameters)
    {
        for(Entry<String, String> entry: parameters.entrySet())
        {

        }
        return null;
    }*/

    public static String urlStringEncode(String input) {
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


    public static String printPwMap(Map<String, String> params){
        StringBuilder out = new StringBuilder();
        final Set<Map.Entry<String, String>> entrySet = params.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
//            out.append('|');
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
            for (Entry<String, String> entry : params.entrySet()) {
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
                Entry<String, String> entry = (Entry) it.next();
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
                Entry<Integer, String> entry = (Entry<Integer, String>) it.next();
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
                Entry<Integer, Float> entry = (Entry<Integer, Float>) it.next();
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

    public static String getRealPathFromURI(Uri contentURI, Activity context) {
        Log.i("URI", contentURI.toString());
        String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentURI, projection, null,
                null, null);
        if (cursor == null) {
            Log.i("CURSOR","CURSOR_NULL");
            return null;
        }
        Log.i("CURSOR",cursor.getCount()+"");
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            // cursor.close();
            return s;
        }
        // cursor.close();
        return null;
    }

    public static void removeJsInterface(WebView webView) {
        try {
            Method method = webView.getClass().getMethod(
                    "removeJavascriptInterface", String.class);
            if (method != null) {
                method.invoke(webView, "searchBoxJavaBridge_");
                method.invoke(webView, "accessibility");
            }
        } catch (Exception ex) {
//            Log.i(LOGTAG, ex.toString());
        }
    }

    public static boolean isSuccessfulUrl(String url, String successfulUrl) {
//        Log.i("pwsdk","url = "+url);
        if (successfulUrl != null) {
            Uri successUri = Uri.parse(successfulUrl);
            Uri uri = Uri.parse(url);
            if (successUri == null || uri == null) return false;

            return (
                    uri.getHost().equals(successUri.getHost()) &&
                            uri.getScheme().equals(successUri.getScheme())
            );
        } else {
            Uri uri = Uri.parse(url);
            return (uri != null && "pwlocal".equals(uri.getScheme()) && "paymentsuccessful".equals(uri.getHost()));
        }
    }

    public static boolean isFasterpayLink(String url) {
        if (TextUtils.isEmpty(url)) return false;
        Uri uri = Uri.parse(url);
        return (uri != null && "fasterpay".equals(uri.getScheme()) && "pay".equals(uri.getHost()));
    }

    public static boolean isFasterPayLink(Uri uri){
        return (uri != null && "fasterpay".equals(uri.getScheme()) && "pay".equals(uri.getHost()));
    }


}
