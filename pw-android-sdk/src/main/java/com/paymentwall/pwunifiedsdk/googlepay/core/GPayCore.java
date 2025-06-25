package com.paymentwall.pwunifiedsdk.googlepay.core;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.paymentwall.pwunifiedsdk.brick.core.BrickHelper;
import com.paymentwall.pwunifiedsdk.core.PWEnv;
import com.paymentwall.pwunifiedsdk.googlepay.utils.Const;
import com.paymentwall.pwunifiedsdk.mobiamo.payment.PWSDKRequest;
import com.paymentwall.pwunifiedsdk.util.PwUtils;
import com.paymentwall.pwunifiedsdk.util.SmartLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class GPayCore {
    private static final int CONNECTION_TIMEOUT = 30000;
    private static final int READ_TIMEOUT = 30000;
    private static final String INSTRUCTION_URL = PWEnv.GOOGLE_PAY_INSTRUCTION_URL;
    private static final String PAYMENT_URL = PWEnv.GOOGLE_PAY_PAYMENT_URL;

    private static GPayCore instance;
    private Context context;

    // region Request Objects
    // Request object for Click ID generation
    public static class GPayClickRequest {
        public String uid;
        public String email;
        public double amount;
        public String currency;
//        public String merchantOrderId;
        public String key;
        public String skey;

        public GPayClickRequest(String uid, String email, double amount, String currency,
                                String key, String skey) {
            this.uid = uid;
            this.email = email;
            this.amount = amount;
            this.currency = currency;
//            this.merchantOrderId = merchantOrderId;
            this.key = key;
            this.skey = skey;
        }
    }

    // Request object for payment processing
    public static class GPayPaymentRequest {
        public String key;
        public String uid;
        public String skey;
        public String googlePayToken;
        public String email;

        public GPayPaymentRequest(String key, String skey, String uid,
                                  String googlePayToken, String email) {
            this.key = key;
            this.uid = uid;
            this.skey = skey;
            this.googlePayToken = googlePayToken;
            this.email = email;
        }
    }
    // endregion

    public static GPayCore getInstance() {
        if (instance == null)
            instance = new GPayCore();
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static PaymentsClient createPaymentsClient(Context context) {
        Wallet.WalletOptions walletOptions =
                new Wallet.WalletOptions.Builder().setEnvironment(PWEnv.GPAY_ENVIRONMENT).build();
        return Wallet.getPaymentsClient(context, walletOptions);
    }

    public JSONObject getIsReadyToPayRequest() {
        try {
            return getBaseRequest()
                    .put("allowedPaymentMethods", new JSONArray().put(getBaseCardPaymentMethod()));
        } catch (JSONException e) {
            return null;
        }
    }


    private String _refId = "";
    private String _sessionKey = "";
    private String _gatewayMerchantId = "";
    private JSONObject _merchantInfo = new JSONObject();

    // region Generate ClickID
    public void getGooglePayInstruction(final GPayClickRequest request, final ClickIdCallback callback) {
        final Handler handler = new Handler(context.getMainLooper());
        getGooglePayInstruction(handler, request.uid, request.email, request.amount, request.currency, request.key,request.skey, callback);
    }

    // Async method to generate ClickID
    private void getGooglePayInstruction(final Handler handler, final String uid, final String email,
                                         final double amount, final String currency, final String key, String skey,
                                         final ClickIdCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String clickId = getGooglePayInstruction(uid, email, amount, currency, key, skey);
                    _refId = clickId;
                    postClickIdSuccess(callback, handler, clickId);
                } catch (GooglePayError error) {
                    postGooglePayError(GPayRequestType.INSTRUCTION, callback, handler, error);
                }
            }
        }).start();
    }

    // Sync method to generate ClickID
    private String getGooglePayInstruction(String uid, String email, double amount, String currency,
                                           String key, String skey) throws GooglePayError {
        try {
            Map<String, String> parameters = new TreeMap<>();
            parameters.put("uid", uid);
            parameters.put("email", email);
            parameters.put("amount", String.valueOf(amount));
            parameters.put("currency", currency);
            parameters.put("ps", "gateway");
//            parameters.put("merchant_order_id", merchantOrderId);

            String queryUrl = BrickHelper.urlEncodeUTF8(parameters);
            URL url = new URL(INSTRUCTION_URL);

            String signature = PWSDKRequest.signatureCalculateValueOnly(parameters, skey, 3);

            HttpURLConnection conn = createPostRequest(url, queryUrl,
                    new HashMap<String, String>() {{
                        put("pw-sign", signature);
                        put("pw-key", key);
                    }});

            String response = getResponseBody(conn.getInputStream());
            JSONObject jsonResponse = new JSONObject(response);

//            JSONObject responseObject = new JSONObject(jsonResponse);
//            JSONObject dataObject = responseObject.getJSONObject("data");
//            String clickID = dataObject.getString("id");
//            if (jsonResponse.has("click_id")) {
//                return jsonResponse.getString("click_id");
//            } else {
//                throw new GooglePayError("Failed to generate click ID", GooglePayError.Kind.API_ERROR);
//            }

            try {
                JSONObject dataObject = jsonResponse.getJSONObject("data");
                String clickID = dataObject.getString("id");
                _refId = clickID;
                _sessionKey = dataObject.getString("session");

                // Parse instructions to get GooglePay configuration
                JSONArray instructions = dataObject.getJSONArray("instructions");
                for (int i = 0; i < instructions.length(); i++) {
                    JSONObject instruction = instructions.getJSONObject(i);
                    if ("google_pay".equals(instruction.getString("name"))) {
                        JSONObject googlePayValue = instruction.getJSONObject("value");

                        // Extract tokenization specification for gateway merchant ID
                        JSONObject tokenizationSpec = googlePayValue.getJSONObject("tokenization_specification");
                        JSONObject tokenizationParams = tokenizationSpec.getJSONObject("parameters");
                        _gatewayMerchantId = tokenizationParams.getString("gatewayMerchantId");

                        // Extract merchant info
                        _merchantInfo = googlePayValue.getJSONObject("merchant_info");

                        SmartLog.d("GooglePay", "ClickID: " + clickID);
                        SmartLog.d("GooglePay", "Session: " + _sessionKey);
                        SmartLog.d("GooglePay", "Gateway Merchant ID: " + _gatewayMerchantId);
                        SmartLog.d("GooglePay", "Merchant Info: " + _merchantInfo.toString());

                        break;
                    }
                }

                return clickID;
            } catch (JSONException e) {
                throw new GooglePayError("Failed to generate click ID", GooglePayError.Kind.API_ERROR);
            }

        } catch (Exception e) {
            throw new GooglePayError(e.getMessage(), GooglePayError.Kind.NETWORK);
        }
    }

    // endregion

    // region Process GooglePay payment
    public void processGPayPayment(final GPayPaymentRequest request, final PaymentCallback callback) {
        final Handler handler = new Handler(context.getMainLooper());
        processGPayPayment(handler, request.key, request.uid, _sessionKey, _refId, request.googlePayToken, request.email, callback);
    }

    // Async method to process Google Pay payment
    private void processGPayPayment(final Handler handler, final String key, final String uid,
                                   final String skey, final String ref, final String googlePayToken,
                                   final String email, final PaymentCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String paymentResult = processGPayPaymentSync(key, uid, skey, ref, googlePayToken, email);
                    postPaymentSuccess(callback, handler, paymentResult);
                } catch (GooglePayError error) {
                    postGooglePayError(GPayRequestType.PAYMENT, callback, handler, error);
                }
            }
        }).start();
    }

    // Sync method to process Google Pay payment
    private String processGPayPaymentSync(String key, String uid, String skey, String ref,
                                         String googlePayToken, String email) throws GooglePayError {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("google_pay_token", googlePayToken);
            parameters.put("email", email);

            String queryUrl = BrickHelper.urlEncodeUTF8(parameters);
            String urlString = String.format("%s?key=%s&uid=%s&skey=%s&ref=%s",
                    PAYMENT_URL, key, uid, skey, ref);
            URL url = new URL(urlString);
            Log.d("GPAY: Request URL", url.toString());
            HttpURLConnection conn = createPostRequest(url, queryUrl, new HashMap<>());
//            conn.setRequestProperty("Accept", "*/*");
//            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            String response = getResponseBody(conn.getInputStream());
            JSONObject jsonResponse = new JSONObject(response);

            if (jsonResponse.has("success") && jsonResponse.getInt("success") == 1) {
                return jsonResponse.toString();
            } else {
                Log.d("GPAY: Response", jsonResponse.toString());
                String errMsg = "Payment processing failed";
                if (jsonResponse.has("error")) {
                    errMsg = jsonResponse.getJSONArray("error").getString(0);
                }
                throw new GooglePayError(errMsg, GooglePayError.Kind.API_ERROR);
            }
        } catch (Exception e) {
            throw new GooglePayError(e.getMessage(), GooglePayError.Kind.NETWORK);
        }
    }

    // endregion

    // Helper methods from Brick class
    private HttpURLConnection createPostRequest(URL url, String queryUrl, Map<String, String> extraHeaders) throws GooglePayError {
        return createPostRequest(url, queryUrl, extraHeaders, CONNECTION_TIMEOUT, READ_TIMEOUT);
    }

    private HttpURLConnection createPostRequest(URL url, String queryUrl, Map<String, String> extraHeaders,  int connectionTimeout, int readTimeout) throws GooglePayError {
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new GooglePayError("Cannot open connection", GooglePayError.Kind.NETWORK);
        }
        Log.d("GPAY: Request raw-data", queryUrl);

        conn.setConnectTimeout(connectionTimeout);
        conn.setReadTimeout(readTimeout);
        conn.setUseCaches(false);
        conn.setDoOutput(true);

        try {
            conn.setRequestMethod("POST");
        } catch (ProtocolException e) {
            throw new GooglePayError("Cannot send data", GooglePayError.Kind.UNEXPECTED);
        }

//        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//        conn.setRequestProperty("Accept", "*/*");
//        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
        conn = PwUtils.addExtraHeaders(context, conn);
        for (String key : extraHeaders.keySet()) {
            conn.setRequestProperty(key, extraHeaders.get(key));
        }

        OutputStream output = null;
        try {
            output = conn.getOutputStream();
            output.write(queryUrl.getBytes("UTF-8"));
            int statusCode = conn.getResponseCode();

            if (statusCode < 200 || statusCode >= 300) {
                String errorResponse = getResponseBody(conn.getErrorStream());
                throw new GooglePayError(errorResponse, GooglePayError.Kind.HTTP);
            }
        } catch (IOException e) {
            throw new GooglePayError("Network error", GooglePayError.Kind.NETWORK);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
        return conn;
    }

    private String getResponseBody(InputStream responseStream) throws GooglePayError {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            br = new BufferedReader(new InputStreamReader(responseStream));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            throw new GooglePayError("Cannot get response", GooglePayError.Kind.NETWORK);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
        return sb.toString();
    }

    private static void postClickIdSuccess(final ClickIdCallback callback, final Handler handler, final String clickId) {
        if (callback != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onClickIdSuccess(clickId);
                }
            });
        }
    }

    private static void postPaymentSuccess(final PaymentCallback callback, final Handler handler, final String result) {
        if (callback != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onPaymentSuccess(result);
                }
            });
        }
    }

    private static void postGooglePayError(GPayRequestType type, final Callback callback, final Handler handler, final GooglePayError error) {
        if (callback != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onGooglePayError(type, error);
                }
            });
        }
    }

    // region Specs
    public JSONArray getAllowedPaymentMethods() throws JSONException {
        return new JSONArray().put(getCardPaymentMethod());
    }

    public JSONObject getPaymentDataRequest(String priceLabel, String currencyCode) {
        try {
            return getBaseRequest()
                    .put("allowedPaymentMethods", getAllowedPaymentMethods())
                    .put("transactionInfo", getTransactionInfo(priceLabel, currencyCode))
                    .put("merchantInfo", getMerchantInfo())
                    .put("emailRequired", true);
//                    .put("shippingAddressRequired", true)
//                    .put("shippingAddressParameters", new JSONObject()
//                            .put("phoneNumberRequired", false)
//                            .put("allowedCountryCodes", new JSONArray(Const.SHIPPING_SUPPORTED_COUNTRIES))
//                    );
        } catch (JSONException e) {
            return null;
        }
    }

    private JSONObject getBaseRequest() throws JSONException {
        return new JSONObject()
                .put("apiVersion", 2)
                .put("apiVersionMinor", 0);
    }

    private JSONObject getTransactionInfo(String price, String currencyCode) throws JSONException {
        return new JSONObject()
                .put("totalPrice", price)
                .put("totalPriceStatus", "FINAL")
//                .put("countryCode", countryCode)
                .put("currencyCode", currencyCode)
                .put("checkoutOption", "COMPLETE_IMMEDIATE_PURCHASE");
    }

    private JSONObject getMerchantInfo() throws JSONException {
        return _merchantInfo;
//        return new JSONObject().put("merchantName", merchantName)
//                .put("merchantOrigin", merchantOrigin);
    }

    private JSONObject getCardPaymentMethod() throws JSONException {
        return getBaseCardPaymentMethod()
                .put("tokenizationSpecification", getGatewayTokenizationSpecification());
    }

    private JSONObject getBaseCardPaymentMethod() throws JSONException {
        return new JSONObject()
                .put("type", "CARD")
                .put("parameters", new JSONObject()
                                .put("allowedAuthMethods", getAllowedCardAuthMethods())
                                .put("allowedCardNetworks", getAllowedCardNetworks())
//                        .put("billingAddressRequired", true)
//                        .put("billingAddressParameters", new JSONObject()
//                                .put("format", "FULL")
//                        )
                );
    }

    private JSONArray getAllowedCardAuthMethods() {
        return new JSONArray(Const.SUPPORTED_METHODS);
    }

    private JSONArray getAllowedCardNetworks() {
        return new JSONArray(Const.SUPPORTED_NETWORKS);
    }

    private JSONObject getGatewayTokenizationSpecification() throws JSONException {
        return new JSONObject()
                .put("type", "PAYMENT_GATEWAY")
                .put("parameters", new JSONObject()
//                        .put("gateway", "example")
//                        .put("gatewayMerchantId", "exampleGatewayMerchantId")
                                .put("gateway", "paymentwall")
                                .put("gatewayMerchantId", _gatewayMerchantId)
                );
    }

    // endregion

    public interface ClickIdCallback extends Callback {
        void onClickIdSuccess(String clickId);
    }

    public interface PaymentCallback extends Callback {
        void onPaymentSuccess(String result);
    }

    public interface Callback {
        void onGooglePayError(GPayRequestType type, GooglePayError error);
    }

    public enum GPayRequestType {
        INSTRUCTION, PAYMENT
    }

    public static class GooglePayError extends Exception {
        public enum Kind {
            NETWORK, API_ERROR, HTTP, UNEXPECTED
        }

        private final Kind kind;

        public GooglePayError(String message, Kind kind) {
            super(message);
            this.kind = kind;
        }

        public Kind getKind() {
            return kind;
        }
    }
}
