package com.paymentwall.pwunifiedsdk.payalto.core;

import android.content.Context;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.paymentwall.pwunifiedsdk.core.PWEnv;
import com.paymentwall.pwunifiedsdk.object.ExternalPs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PayAltoCore {

    private static final String TAG = "PayAltoCore";

    // Production
    private static final String PAYMENT_SYSTEMS_BASE_URL = PWEnv.PAY_ALTO_PAYMENT_SYSTEMS;
    public static final String PAY_ALTO_PS = PWEnv.PAY_ALTO_PS;
    public static final String PAY_ALTO_SUBSCRIPTION = PWEnv.PAY_ALTO_SUBSCRIPTION;
    public static final String PAY_ALTO_CART = PWEnv.PAY_ALTO_CART;


    // Staging
//    private static final String PAYMENT_SYSTEMS_BASE_URL = "https://develop.wallapi.bamboo.stuffio.com/api/payment-systems";
//    public static final String PAY_ALTO_PS = "https://develop.wallapi.bamboo.stuffio.com/api/ps/";
//    public static final String PAY_ALTO_SUBSCRIPTION = "https://develop.wallapi.bamboo.stuffio.com/api/subscription/";
//    public static final String PAY_ALTO_CART = "https://develop.wallapi.bamboo.stuffio.com/api/cart/";

    private static final int CONNECTION_TIMEOUT_MS = 10000; // 10 seconds
    private static final int READ_TIMEOUT_MS = 15000; // 15 seconds

    private static PayAltoCore instance;
    private Context context;

    // region Data Models
    public static class PayAltoMethod {
        public String id;
        public String imgClass;
        public String imgUrl;
        public String name;
        public boolean newWindow;
        public int psTypeId;

        public PayAltoMethod(String id, String imgClass, String imgUrl, String name, boolean newWindow, int psTypeId) {
            this.id = id;
            this.imgClass = imgClass;
            this.imgUrl = imgUrl;
            this.name = name;
            this.newWindow = newWindow;
            this.psTypeId = psTypeId;
        }

        public PayAltoMethod(JSONObject json) throws JSONException {
            this.id = json.getString("id");
            this.imgClass = json.getString("img_class");
            this.imgUrl = json.getString("img_url");
            this.name = json.getString("name");
            this.newWindow = json.getBoolean("new_window");
            this.psTypeId = json.getInt("ps_type_id");
        }

        public JSONObject toJSON() throws JSONException {
            JSONObject json = new JSONObject();
            json.put("id", id);
            json.put("img_class", imgClass);
            json.put("img_url", imgUrl);
            json.put("name", name);
            json.put("new_window", newWindow);
            json.put("ps_type_id", psTypeId);
            return json;
        }
    }

    public static class PaymentSystemsRequest {
        public String key;
        public String countryCode;

        public PaymentSystemsRequest(String key, String country_code) {
            this.key = key;
            this.countryCode = country_code;
        }
    }
    // endregion

    public static PayAltoCore getInstance() {
        if (instance == null)
            instance = new PayAltoCore();
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void getPaymentSystems(final PaymentSystemsRequest request, final PaymentSystemsCallback callback) {
        final Handler handler = new Handler(context.getMainLooper());
        getPaymentSystems(handler, request.key, request.countryCode, callback);
    }

    // Async method to get payment systems
    private void getPaymentSystems(final Handler handler, final String key, final String countryCode, final PaymentSystemsCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<PayAltoMethod> payAltoMethods = getPaymentSystemsSync(key, countryCode);
                    postPaymentSystemsSuccess(callback, handler, payAltoMethods);
                } catch (PayAltoError error) {
                    postPayAltoError(callback, handler, error);
                }
            }
        }).start();
    }

    // Sync method to get payment systems from real API
    public List<PayAltoMethod> getPaymentSystemsSync(String key, String countryCode) throws PayAltoError {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            // Build URL with key parameter
            String urlString = PAYMENT_SYSTEMS_BASE_URL + "?key=" + key;
            if (countryCode!= null && !countryCode.isBlank()) {
                urlString += "&country_code=" + countryCode;
            }
            URL url = new URL(urlString);

            Log.d(TAG, "Making API request to: " + urlString);

            // Create connection
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CONNECTION_TIMEOUT_MS);
            connection.setReadTimeout(READ_TIMEOUT_MS);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("User-Agent", "PWLocalCore-Android");

            // Check response code
            int responseCode = connection.getResponseCode();
            Log.d(TAG, "API response code: " + responseCode);

            if (responseCode != HttpURLConnection.HTTP_OK) {
                String errorMessage = "HTTP error: " + responseCode;
                if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    errorMessage = "Invalid API key";
                } else if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                    errorMessage = "Access forbidden";
                } else if (responseCode >= 500) {
                    errorMessage = "Server error";
                }
                throw new PayAltoError(errorMessage, PayAltoError.Kind.HTTP);
            }

            // Read response
            InputStream inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            String jsonResponse = response.toString();
            Log.d(TAG, "API response received, length: " + jsonResponse.length());

            // Parse JSON response
            return parsePaymentMethods(jsonResponse);

        } catch (IOException e) {
            Log.e(TAG, "Network error: " + e.getMessage());
            throw new PayAltoError("Network error: " + e.getMessage(), PayAltoError.Kind.NETWORK);
        } catch (JSONException e) {
            Log.e(TAG, "JSON parsing error: " + e.getMessage());
            throw new PayAltoError("Failed to parse payment systems data", PayAltoError.Kind.API_ERROR);
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error: " + e.getMessage());
            throw new PayAltoError(e.getMessage(), PayAltoError.Kind.UNEXPECTED);
        } finally {
            // Clean up resources
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.w(TAG, "Error closing reader: " + e.getMessage());
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private List<PayAltoMethod> parsePaymentMethods(String jsonResponse) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonResponse);
        List<PayAltoMethod> payAltoMethods = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            PayAltoMethod payAltoMethod = new PayAltoMethod(jsonObject);
            payAltoMethods.add(payAltoMethod);
        }

        Log.d(TAG, "Successfully parsed " + payAltoMethods.size() + " payment methods");
        return payAltoMethods;
    }

    // Helper methods for posting results to main thread
    private static void postPaymentSystemsSuccess(final PaymentSystemsCallback callback, final Handler handler, final List<PayAltoMethod> payAltoMethods) {
        if (callback != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
//                    List<PaymentMethod> paymentMethodList = transformToExternalPs(paymentMethods);
                    callback.onPaymentSystemsSuccess(payAltoMethods);
                }
            });
        }
    }

    private static void postPayAltoError(final Callback callback, final Handler handler, final PayAltoError error) {
        if (callback != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onPayAltoError(error);
                }
            });
        }
    }

    // Transforming
    private static List<ExternalPs> transformToExternalPs(List<PayAltoMethod> payAltoMethods) {
        List<ExternalPs> externalPsList = new ArrayList<>();

        for (PayAltoMethod payAltoMethod : payAltoMethods) {
            // Create PayAltoParams with additional data
            PayAltoParams params = new PayAltoParams(
                    payAltoMethod.imgUrl,
                    payAltoMethod.imgClass,
                    payAltoMethod.newWindow,
                    payAltoMethod.psTypeId
            );

            // Create ExternalPs with default icon resource ID (0 since we're using image URLs)
            ExternalPs externalPs = new ExternalPs(
                    payAltoMethod.id,
                    payAltoMethod.name,
                    0, // iconResId - using 0 since we have imgUrl in params
                    params
            );

            externalPsList.add(externalPs);
        }

        Log.d(TAG, "Transformed " + payAltoMethods.size() + " PaymentMethods to ExternalPs objects");
        return externalPsList;
    }

    // Parameters class for ExternalPs
    public static class PayAltoParams implements Parcelable {
        public String imgUrl;
        public String imgClass;
        public boolean newWindow;
        public int psTypeId;

        public PayAltoParams(String imgUrl, String imgClass, boolean newWindow, int psTypeId) {
            this.imgUrl = imgUrl;
            this.imgClass = imgClass;
            this.newWindow = newWindow;
            this.psTypeId = psTypeId;
        }

        protected PayAltoParams(Parcel in) {
            imgUrl = in.readString();
            imgClass = in.readString();
            newWindow = in.readByte() != 0;
            psTypeId = in.readInt();
        }

        public static final Creator<PayAltoParams> CREATOR = new Creator<PayAltoParams>() {
            @Override
            public PayAltoParams createFromParcel(Parcel source) {
                return new PayAltoParams(source);
            }

            @Override
            public PayAltoParams[] newArray(int size) {
                return new PayAltoParams[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(imgUrl);
            dest.writeString(imgClass);
            dest.writeByte((byte) (newWindow ? 1 : 0));
            dest.writeInt(psTypeId);
        }
    }

    // Callback interfaces
    public interface PaymentSystemsCallback extends Callback {
        void onPaymentSystemsSuccess(List<PayAltoMethod> payAltoMethods);
    }

    public interface Callback {
        void onPayAltoError(PayAltoError error);
    }

    // Error class
    public static class PayAltoError extends Exception {
        public enum Kind {
            NETWORK, API_ERROR, HTTP, UNEXPECTED
        }

        private final Kind kind;

        public PayAltoError(String message, Kind kind) {
            super(message);
            this.kind = kind;
        }

        public Kind getKind() {
            return kind;
        }
    }
}
