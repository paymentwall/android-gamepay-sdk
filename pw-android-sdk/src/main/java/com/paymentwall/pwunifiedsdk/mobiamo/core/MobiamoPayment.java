package com.paymentwall.pwunifiedsdk.mobiamo.core;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.paymentwall.pwunifiedsdk.mobiamo.payment.PWSDKRequest;
import com.paymentwall.pwunifiedsdk.util.PwUtils;
import com.paymentwall.pwunifiedsdk.util.SmartLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MobiamoPayment extends PWSDKRequest {
    private static final long serialVersionUID = -6164544224875116912L;
    private static final String GET_API = "https://api.paymentwall.com/api/mobiamo/status";
    private static final String POST_API = "https://api.paymentwall.com/api/mobiamo/payment";
    private static final String GET_PRICE_POINT_API = "https://api.paymentwall.com/api/price-points";
    private static final String REQUEST_UID = "uid";
    private static final String REQUEST_KEY = "key";
    private static final String REQUEST_PRODUCT_NAME = "product_name";
    private static final String REQUEST_PRODUCT_ID = "product_id";
    private static final String REQUEST_AMOUNT = "amount";
    private static final String REQUEST_CURRENCY = "currency";
    private static final String REQUEST_COUNTRY = "country";
    private static final String REQUEST_MNC = "mnc";
    private static final String REQUEST_MCC = "mcc";
    private static final String REQUEST_SIGN_VERSION = "sign_version";
    private static final String REQUEST_TS = "ts";
    private static final String MOBIAMO_CALL_TYPE = "mobiamo_call_type";
    private static final String TYPE = "inapp";
    private static final String PS = "ps";
    private static final String MOBIAMO = "mobiamo";

    private static final String RESPONSE_TRANSACTION_ID = "transaction_id";
    private static final String RESPONSE_SHORT_CODE = "shortcode";
    private static final String RESPONSE_KEY_WORD = "keyword";
    private static final String RESPONSE_REGULATORY_TEXT = "regulatory_text";
    private static final String RESPONSE_SUCCESS = "success";
    private static final String RESPONSE_STATUS = "status";
    private final static String RESPONSE_AMOUNT = "amount";
    private static final String RESPONSE_CURRENCY_CODE = "currencyCode";

    private static final int RESULT_OK = 0;
    private static final int RESULT_FAILED = 1;

    public static final int ACTIVITY_MOBIAMO = 1111;
    private static final String TAG = "MobiamoPayment";

    private static final int RETRIES_TIME = 3;
    private static final int RETRY_DELAY_MS = 1 * 1000;

    /***
     * Request parameters
     */
    // ID of the end-user who's making the payment in Merchant's system
    private static String mUid;

    // Project Key provided by Paymentwall
    private static String mKey;

    // name of the product purchased by the user
    private static String mProductName;

    // ID of the product purchased by the user
    private static String mProductId;

    // pricepoint amount, e.g. 9.99
    private static String mAmount;

    // pricepoint currency code, e.g. EUR
    private static String mCurrency;

    private String itemUrl;
    private File itemFile;
    private int itemResID;
    private String itemContentProvider;

    // Mobile Network Code, needed along with mcc to identify user's carrier and
    // provide relevant instructions
    private static String mMNC;

    // Mobile Country Code
    private static String mMCC;

    private static boolean mTestMode;

    private static final String TRANSACTION_STATUS_COMPLETED = "completed";

    public MobiamoPayment() {
        signVersion = 2;
    }

    public void setProductId(String productId) {
        mProductId = productId;
    }

    public String getProductId() {
        return mProductId;
    }

    public void setProductName(String productName) {
        mProductName = productName;
    }

    public void setAmount(String amount) {
        mAmount = amount;
    }

    public void setApplicationKey(String key) {
        mKey = key;
    }

    public String getAmount() {
        return mAmount;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public String getProductName() {
        return mProductName;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public void setCurrency(String currency) {
        mCurrency = currency;
    }

    public void setTestMode(boolean value) {
        this.mTestMode = value;
    }


    public String getItemContentProvider() {
        return itemContentProvider;
    }

    public void setItemContentProvider(String itemContentProvider) {
        this.itemContentProvider = itemContentProvider;
    }

    public int getItemResID() {
        return itemResID;
    }

    public void setItemResID(int itemResID) {
        this.itemResID = itemResID;
    }

    public File getItemFile() {
        return itemFile;
    }

    public void setItemFile(File itemFile) {
        this.itemFile = itemFile;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public void getPricePoint(Context context, MobiamoResponse response, IGetPricepoint listener) {
        new GetPricePointAsyncTask().execute(context, response, listener);
    }

    public void postData(Context context, MobiamoResponse response, int option, IPayment listener
    ) {
        new PostAsyncTask().execute(context, response, listener, option);
    }

    public void getPaymentStatus(Context context, MobiamoResponse response, IPayment listener) {
        new GetPaymentStatusTask().execute(context, response, listener);
    }


    private static class PostAsyncTask extends
            AsyncTask<Object, Integer, HashMap<Integer, String>> {
        private IPayment listener;
        private MobiamoResponse response;
        private int option;
        private final HashMap<Integer, String> mResult = new HashMap<>();
        private String responseText;
        private Context context;


        @Override
        protected HashMap<Integer, String> doInBackground(Object... params) {
            context = (Context) params[0];
            response = (MobiamoResponse) params[1];
            listener = (IPayment) params[2];
            option = (Integer) params[3];

            if (context != null) {
                // find country following sim
                TelephonyManager tm = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                String mCountry = tm.getSimCountryIso().toUpperCase(Locale.US);

                // find currency code following country
                Locale[] locales = Locale.getAvailableLocales();
                Locale locale = null;
                for (Locale locale1 : locales) {
                    if (locale1.getCountry().equals(mCountry)) {
                        locale = locale1;
                        break;
                    }
                }
                if (locale != null) {
                    mCurrency = java.util.Currency.getInstance(locale)
                            .getCurrencyCode().toUpperCase(Locale.US);
                    SmartLog.d(TAG, "Currency: " + mCurrency);

                    if (mCurrency.equalsIgnoreCase("LTL")) {
                        mCurrency = "EUR";
                    }
                }

                // find mcc and mnc
                String simOperator = tm.getSimOperator();
                if (simOperator != null && simOperator.length() > 0) {
                    mMCC = simOperator.substring(0, 3);
                    mMNC = simOperator.substring(3);
                }
//                mCurrency = "PHP";
//                mCountry = "PH";
//
//                mMCC = "515";
//                mMNC = "01";


                // calculate unix timestamp in seconds
                long time = System.currentTimeMillis() / 1000;
                String ts = String.valueOf(time);

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put(REQUEST_AMOUNT,
                        String.valueOf(mAmount.replace(",", "")));
                parameters.put(REQUEST_COUNTRY, mCountry);
                parameters.put(REQUEST_CURRENCY, mCurrency);
                parameters.put(REQUEST_KEY, mKey);
                parameters.put(REQUEST_MCC, mMCC);
                parameters.put(REQUEST_MNC, mMNC);
                parameters.put(REQUEST_PRODUCT_ID, String.valueOf(mProductId));
                parameters.put(REQUEST_PRODUCT_NAME, mProductName);
                parameters.put(REQUEST_SIGN_VERSION,
                        String.valueOf(signVersion));
                parameters.put(REQUEST_TS, ts);
                parameters.put(REQUEST_UID, mUid);

                String requestUrl = MobiamoHelper.buildRequestUrl(parameters,
                        POST_API);

                SmartLog.d(TAG, "requestUrl = " + requestUrl);

                try {
                    responseText = createPostByHttpURLConnection(context, requestUrl);

                    SmartLog.d(TAG, "POST:response = " + responseText);

                    // Parse json string returned
                    JSONObject jo = new JSONObject(responseText);
                    String transId = jo.getString(RESPONSE_TRANSACTION_ID);
                    String shortcode = jo.getString(RESPONSE_SHORT_CODE);
                    String keyword = jo.getString(RESPONSE_KEY_WORD);
                    String regulatoryText = jo.getJSONArray(
                            RESPONSE_REGULATORY_TEXT).getString(0);

                    // Set response object
                    response.setTransactionId(transId);
                    response.setShortcode(shortcode);
                    response.setKeyword(keyword);
                    response.setRegulatoryText(regulatoryText);
                    mResult.put(RESULT_OK,
                            MobiamoHelper.getString(Message.POST_SUCCESS));
                    response.setMessage(MobiamoHelper
                            .getString(Message.POST_SUCCESS));
                    return mResult;
                } catch (JSONException e) {
                    try {
                        JSONObject object = new JSONObject(responseText);
                        String strError = object.getString("error");
                        mResult.put(RESULT_FAILED, strError);
                        response.setMessage(strError);
                        return mResult;
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                        e.printStackTrace();
                    }
                }
            }

            mResult.put(RESULT_FAILED,
                    MobiamoHelper.getString(Message.ERROR_POST));
            response.setMessage(MobiamoHelper.getString(Message.ERROR_POST));
            return mResult;
        }

        @Override
        protected void onPostExecute(HashMap<Integer, String> result) {
            Integer keyResult = result.keySet().iterator().next();
            switch (keyResult) {
                case RESULT_OK:
                    listener.onSuccess(response);
                    break;
                case RESULT_FAILED:
                    listener.onError(response);
                    break;
            }
        }
    }

    private static class GetPricePointAsyncTask extends
            AsyncTask<Object, Integer, HashMap<Integer, String>> {

        private IGetPricepoint listener;
        private MobiamoResponse response;
        private Context context;

        private final ArrayList<PriceObject> list = new ArrayList<>();
        private ArrayList<?> sortedList = new ArrayList<>();
        private String[] pricesArray;

        private final HashMap<Integer, String> mResult = new HashMap<>();
        private String result = "";

        @Override
        protected HashMap<Integer, String> doInBackground(Object... params) {
            context = (Context) params[0];
            response = (MobiamoResponse) params[1];
            listener = (IGetPricepoint) params[2];

            TelephonyManager tm = (TelephonyManager) context.
                    getSystemService(Context.TELEPHONY_SERVICE);
            // find mcc and mnc
            String simOperator = tm.getSimOperator();
            if (simOperator != null && simOperator.length() > 0) {
                mMCC = simOperator.substring(0, 3);
                mMNC = simOperator.substring(3);

                SmartLog.i("SIM_MCC", mMCC);
                SmartLog.i("SIM_MNC", mMNC);

            }
//				else {
//					mResult.put(RESULT_FAILED,
//							MobiamoHelper.getString(Message.ERROR_GET_MCC_MNC));
//					response.setMessage(MobiamoHelper
//							.getString(Message.ERROR_GET_MCC_MNC));
//					return mResult;
//				}

            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put(REQUEST_KEY, mKey);

            if (mTestMode) {
                mMCC = "452";
                mMNC = "04";
            }

            parameters.put(REQUEST_MCC, mMCC);
            parameters.put(REQUEST_MNC, mMNC);
            parameters.put(MOBIAMO_CALL_TYPE, TYPE);
            parameters.put(PS, MOBIAMO);

            String requestUrl = MobiamoHelper.buildRequestUrl(parameters,
                    GET_PRICE_POINT_API);

            try {
                result = createGetByHttpURLConnection(context, requestUrl);
                if (result == null) {
                    mResult.put(RESULT_FAILED, MobiamoHelper
                            .getString(Message.ERROR_OPERATOR_NOT_SUPPORT));
                    response.setMessage(MobiamoHelper
                            .getString(Message.ERROR_OPERATOR_NOT_SUPPORT));
                    return mResult;
                }

                if (isInvalidResponse(result)) {
                    mResult.put(RESULT_FAILED, MobiamoHelper
                            .getString(Message.ERROR_OPERATOR_NOT_SUPPORT));
                    response.setMessage(MobiamoHelper
                            .getString(Message.ERROR_OPERATOR_NOT_SUPPORT));
                    return mResult;
                }

                SmartLog.d(TAG, "GET:response = " + result);
                JSONArray array = new JSONArray(result);
                for (int n = 0; n < array.length(); n++) {
                    JSONObject object = array.getJSONObject(n);
                    PriceObject price = new PriceObject();
                    String value = object.getString("amount");
                    String currency = object.getString("currency");
                    String priceInfo = object.getString("price_info");
                    price.setKeyValue(value);
                    price.setCurrencyValue(currency);
                    price.setPriceInfo(priceInfo);
                    list.add(price);
                }

                sortedList = MobiamoHelper.sortPrices(list);
                pricesArray = MobiamoHelper.createPricePointList(list);
                mResult.put(RESULT_OK, MobiamoHelper
                        .getString(Message.GET_PRICE_POINT_SUCCESS));
                response.setMessage(MobiamoHelper
                        .getString(Message.GET_PRICE_POINT_SUCCESS));
                return mResult;
            } catch (JSONException e) {
                try {
                    JSONObject object = new JSONObject(result);
                    String strError = object.getString("error");
                    mResult.put(RESULT_FAILED, strError);
                    response.setMessage(strError);
                    return mResult;
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    e.printStackTrace();
                }
            }

            mResult.put(RESULT_FAILED,
                    MobiamoHelper.getString(Message.ERROR_GENERAL));
            response.setMessage(MobiamoHelper.getString(Message.ERROR_GENERAL));
            return mResult;
        }

        @Override
        protected void onPostExecute(HashMap<Integer, String> result) {
            Integer keyResult = result.keySet().iterator().next();
            switch (keyResult) {

                case RESULT_OK:
                    listener.onSuccess(response, sortedList, pricesArray);
//                        ma.showDialogPrice(response, sortedList, pricesArray);
                    break;

                case RESULT_FAILED:
//					Toast.makeText(ma, result.get(keyResult),
//							Toast.LENGTH_SHORT).show();
//                        ma.showNotSupportDialog(listener, response);
                    listener.onError(response);
//				listener.onPaymentFailed(response);
                    break;
            }
        }
    }

    private static String createPostByHttpURLConnection(final Context context, String requestUrl) {
        URL url;
        HttpsURLConnection urlConnection = null;
        String response = null;
        boolean isConnected = false;
        for (int retry = 0; retry <= RETRIES_TIME && !isConnected; retry++) {
            try {
                if (retry > 0) {
                    SmartLog.w(TAG, "Retry connect: " + retry + "/" + RETRIES_TIME);
                    Thread.sleep(RETRY_DELAY_MS);
                }
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
                    System.setProperty("http.keepAlive", "false");
                }

                url = new URL(requestUrl);
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
                urlConnection = PwUtils.addExtraHeaders(context, urlConnection);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                // writer.write(getQuery(params));
                writer.write(requestUrl);
                writer.flush();
                writer.close();
                os.close();
                urlConnection.connect();

                SmartLog.d(TAG, "Response Code: " + urlConnection.getResponseCode());
                if (urlConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    isConnected = true;
                    response = convertInputStreamToString(urlConnection
                            .getInputStream());
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
        return response;
    }

    private static String createGetByHttpURLConnection(final Context context, String requestUrl) {
        URL url;
        HttpsURLConnection urlConnection = null;
        String response = null;
        boolean isConnected = false;

        for (int retry = 0; retry <= RETRIES_TIME && !isConnected; retry++) {
            try {
                if (retry > 0) {
                    SmartLog.w(TAG, "retry connect: " + retry + "/" + RETRIES_TIME);
                    Thread.sleep(RETRY_DELAY_MS);

                }

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
                    System.setProperty("http.keepAlive", "false");
                }

                url = new URL(requestUrl);
                SmartLog.d(TAG, "GET request: " + requestUrl);
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(15 * 1000);
                urlConnection.setConnectTimeout(10 * 1000);
                urlConnection = PwUtils.addExtraHeaders(context, urlConnection);
                urlConnection.connect();


                SmartLog.d(TAG, "Response Code: " + urlConnection.getResponseCode());
                if (urlConnection.getResponseCode() > 0) {
                    isConnected = true;
                    response = convertInputStreamToString(urlConnection
                            .getInputStream());
                }

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {

                    urlConnection.disconnect();
                }
            }
        }
        return response;
    }

    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }

    private static String convertInputStreaToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }
        inputStream.close();
        return result;
    }

    private static class GetPaymentStatusTask extends
            AsyncTask<Object, Integer, Integer> {
        private IPayment listener;
        private MobiamoResponse response;
        private Context context;

        @Override
        protected Integer doInBackground(Object... params) {
            context = (Context)params[0];
            response = (MobiamoResponse) params[1];
            listener = (IPayment) params[2];

            // Build GET request
            long time = System.currentTimeMillis() / 1000;
            String ts = String.valueOf(time);

            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put(REQUEST_KEY, mKey);
            parameters.put(REQUEST_SIGN_VERSION, String.valueOf(signVersion));
            parameters.put(RESPONSE_TRANSACTION_ID,
                    String.valueOf(response.getTransactionId()));
            parameters.put(REQUEST_TS, ts);
            parameters.put(REQUEST_UID, mUid);

            String requestUrl = MobiamoHelper.buildRequestUrl(parameters,
                    GET_API);

            long startTime = System.currentTimeMillis() / 1000;
            long endTime = startTime;
            String responseText = null;

//            int TIME_OUT = 60;
//            while (endTime - startTime <= TIME_OUT) {
            try {

                responseText = createGetByHttpURLConnection(context, requestUrl);
                SmartLog.d(TAG, "Get response: " + responseText);

                JSONObject jo = new JSONObject(responseText);
                String success = jo.getString(RESPONSE_SUCCESS);
                String status = jo.getString(RESPONSE_STATUS);
                String amount = jo.getString(RESPONSE_AMOUNT);
                String currencyCode = jo.getString(RESPONSE_CURRENCY_CODE);

                // Set response object
                response.setSuccess(Integer.valueOf(success));
                response.setStatus(status);
                response.setAmount(Float.valueOf(amount.replace(",", "")));
                response.setCurrencyCode(currencyCode);
                response.setMessage(MobiamoHelper.getString(Message.TRANSACTION_SUCCESS));

                if (status.equalsIgnoreCase(TRANSACTION_STATUS_COMPLETED)) {
                    response.setMessage(MobiamoHelper
                            .getString(Message.TRANSACTION_SUCCESS));
                    return RESULT_OK;
                }else{
                    response.setMessage(MobiamoHelper.getString(Message.TRANSACTION_FAIL));
                    return RESULT_FAILED;
                }

            } catch (JSONException e) {
                e.printStackTrace();
                try {
                    JSONObject object = new JSONObject(responseText);
                    String strError = object.getString("error");
                    response.setMessage(strError);
                    return RESULT_FAILED;
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    e.printStackTrace();
                }
            }
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                endTime = System.currentTimeMillis() / 1000;
//            }

            response.setMessage(MobiamoHelper
                    .getString(Message.ERROR_GET_METHOD));
            return RESULT_FAILED;
        }

        @Override
        protected void onPostExecute(Integer result) {
            switch (result) {
                case RESULT_OK:
                    listener.onSuccess(response);
                    break;
                case RESULT_FAILED:
                    listener.onError(response);
                    break;
            }
        }
    }

    static boolean isInvalidResponse(String response) {
        return (response == null || response.equals("") || response.equals(" ") || response
                .equals("[]"));
    }

    @Override
    public String generateParameterString(int signatureVersion) {
        return null;
    }

    @Override
    public String signatureCalculate() {
        return null;
    }

    @Override
    public String signatureCalculate(int version) {
        return null;
    }

    @Override
    public boolean validateData() {
        return false;
    }

//    public static Intent createIntent(MobiamoPayment request, Context context) {
//        Intent intent = new Intent(context, MobiamoDialogActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        intent.putExtra(Const.KEY.REQUEST_MESSAGE, request);
//        return intent;
//    }

    public static void release() {
        mUid = "";
        mKey = "";
        mProductName = "";
        mProductId = "";
        mAmount = "";
        mCurrency = "";
        mMNC = "";
        mMCC = "";
    }

    public interface IGetPricepoint {
        void onSuccess(MobiamoResponse response, ArrayList<?> sortedList, String[] priceArray);

        void onError(MobiamoResponse response);
    }

    public interface IPayment {
        void onSuccess(MobiamoResponse response);

        void onError(MobiamoResponse response);
    }
}
