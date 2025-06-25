package com.paymentwall.pwunifiedsdk.payalto.utils;


import com.paymentwall.pwunifiedsdk.payalto.message.JSONConverter;
import com.paymentwall.pwunifiedsdk.payalto.message.MultiPaymentStatusException;
import com.paymentwall.pwunifiedsdk.payalto.message.NoPaymentStatusException;
import com.paymentwall.pwunifiedsdk.payalto.message.PaymentStatus;
import com.paymentwall.pwunifiedsdk.payalto.message.PaymentStatusRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

/**
 * Created by paymentwall on 20/08/15.
 */

/**
 * Http utils for Payment Status API
 **/
public class PaymentStatusUtils {
    private static final String PAYMENT_STATUS_BASE_URL = "https://api.paymentwall.com/api/rest/";
    private static final String PAYMENT_STATUS_SUB_URL = "payment";
    private static final int TIMEOUT_READ = 10000;
    private static final int TIMEOUT_CONNECT = 20000;

    /**
     * Sync method
     *
     * @throws NullPointerException        if @callback or @request is null
     * @throws MultiPaymentStatusException if there are more than 1 payment status
     * @throws NoPaymentStatusException    if there is no payment status
     **/
    public static PaymentStatus getSinglePaymentStatus(PaymentStatusRequest request) throws Exception {
        if (request == null) throw new NullPointerException("Request must not be null");
        List<PaymentStatus> paymentStatusList = getPaymentStatus(request);
        if (paymentStatusList != null && paymentStatusList.size() == 1)
            return paymentStatusList.get(0);
        else if (paymentStatusList != null && paymentStatusList.isEmpty()) {
            throw new NoPaymentStatusException("Got no payment yet");
        } else throw new MultiPaymentStatusException("Got more than 1 payment status");
    }

    /**
     * Sync method
     *
     * @return list of payment status
     **/
    public static List<PaymentStatus> getPaymentStatus(PaymentStatusRequest request) throws Exception {
        //Prepare SSL cache
        String originalDNSCacheTTL = null;
        boolean allowedToSetTTL = true;
        try {
            originalDNSCacheTTL = Security
                    .getProperty("networkaddress.cache.ttl");
            Security.setProperty("networkaddress.cache.ttl", "0");
        } catch (SecurityException se) {
            allowedToSetTTL = false;
        }
        try {
            URL url = new URL(request.getUrl(PAYMENT_STATUS_BASE_URL + PAYMENT_STATUS_SUB_URL));
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setReadTimeout(TIMEOUT_READ);
            connection.setConnectTimeout(TIMEOUT_CONNECT);
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            checkSSLCert(connection);
            int statusCode = connection.getResponseCode();
            if (statusCode < HttpURLConnection.HTTP_OK || statusCode >= HttpURLConnection.HTTP_MULT_CHOICE) {
                try {
                    String errorResponse = getResponseBody(connection.getErrorStream());
                    throw new Exception(statusCode + ":" + errorResponse);
                } catch (Exception e) {
                    throw e;
                }
            } else {
                String successfulResponse = "";
                try {
                    successfulResponse = getResponseBody(connection.getInputStream());
                    return JSONConverter.getPaymentStatus(successfulResponse);
                } catch (Exception e) {
                    throw new Exception("Error: " + successfulResponse);
                }
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if (allowedToSetTTL) {
                if (originalDNSCacheTTL == null) {
                    Security.setProperty("networkaddress.cache.ttl", "-1");
                } else {
                    Security.setProperty("networkaddress.cache.ttl",
                            originalDNSCacheTTL);
                }
            }
        }
    }

    /**
     * Async method, use default executor. A PaymentStatusCallback must be added.
     *
     * @throws NullPointerException if @callback or @request is null
     **/
    public static void getPaymentStatus(final PaymentStatusRequest request, final PaymentStatusCallback callback) throws Exception {
        getPaymentStatus(request, null, callback);
    }

    /**
     * Async method, use provided executor (if any). A PaymentStatusCallback must be added.
     *
     * @throws NullPointerException if @callback or @request is null
     **/
    public static void getPaymentStatus(final PaymentStatusRequest request, final Executor executor, final PaymentStatusCallback callback) throws Exception {
        if (callback == null) throw new NullPointerException("Callback must not be null");
        if (request == null) throw new NullPointerException("Request must not be null");

        CompatAsyncTask<Void, Void, PaymentStatusResponse> task = new CompatAsyncTask<Void, Void, PaymentStatusResponse>() {
            @Override
            protected PaymentStatusResponse doInBackground(Void... params) {
                try {
                    List<PaymentStatus> paymentStatusList = getPaymentStatus(request);
                    return new PaymentStatusResponse(null, paymentStatusList);
                } catch (Exception e) {
                    return new PaymentStatusResponse(e, null);
                }
            }

            @Override
            protected void onPostExecute(PaymentStatusResponse paymentStatusResponse) {
                if(callback instanceof PaymentStatusComplexCallback) {
                    if (paymentStatusResponse.paymentStatusList == null) {
                        callback.onError(paymentStatusResponse.error);
                    } else if (paymentStatusResponse.paymentStatusList.isEmpty()) {
                        callback.onError(new NoPaymentStatusException("Got no payment yet"));
                    } else if (paymentStatusResponse.paymentStatusList.size() == 1) {
                        ((PaymentStatusComplexCallback) callback).onSuccessSingle(paymentStatusResponse.paymentStatusList.get(0));
                    } else {
                        callback.onSuccess(paymentStatusResponse.paymentStatusList);
                    }
                } else {
                    if (paymentStatusResponse.paymentStatusList == null) {
                        callback.onError(paymentStatusResponse.error);
                    } else {
                        callback.onSuccess(paymentStatusResponse.paymentStatusList);
                    }
                }
            }
        };

        executeTokenTask(executor, task);
    }





    private static class PaymentStatusResponse {
        List<PaymentStatus> paymentStatusList;
        Exception error;

        public PaymentStatusResponse(Exception error, List<PaymentStatus> paymentStatusList) {
            this.error = error;
            this.paymentStatusList = paymentStatusList;
        }
    }

    private static void executeTokenTask(Executor executor, CompatAsyncTask<Void, Void, PaymentStatusResponse> task) {
        if (executor != null)
            task.executeOnExecutor(executor);
        else
            task.execute();
    }

    private static void checkSSLCert(HttpURLConnection connnection) throws Exception {
        if (!connnection.getURL().getHost().equals("api.paymentwall.com")) {
            return;
        }
        HttpsURLConnection tempConnection = (HttpsURLConnection) connnection;
        try {
            tempConnection.connect();
        } catch (IOException e) {
            throw e;
        }

        Certificate[] certificates;
        try {
            certificates = tempConnection.getServerCertificates();
        } catch (SSLPeerUnverifiedException e) {
            throw e;
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] der = certificates[0].getEncoded();
            md.update(der);
            byte[] digest = md.digest();
            byte[] revokedCertDigest = {5, -64, -77, 100, 54, -108, 71, 10,
                    -120, -116, 110, 127, -21, 92, -98, 36, -24, 35, -36, 83};
            if (Arrays.equals(digest, revokedCertDigest)) {
                throw new SSLPeerUnverifiedException("Revoked certificate");
            }
        } catch (NoSuchAlgorithmException e) {
            throw e;
        } catch (CertificateEncodingException e) {
            throw e;
        }
    }

    private static String getStringFromInputStream(InputStream is)
            throws IOException {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }
        return sb.toString();
    }

    private static String getResponseBody(InputStream responseStream)
            throws IOException {
        String responseBody = getStringFromInputStream(responseStream);
        try {
            responseStream.close();
        } catch (IOException e) {
            throw e;
        }
        return responseBody;
    }

    /*private static String getQuery(Map<String,String> params) throws UnsupportedEncodingException {
        StringBuilder query = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry: params.entrySet()) {
            if(first) first = false;
            else query.append('&');
            query.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            query.append('=');
            query.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
        }
        return query.toString();
    }*/
}
