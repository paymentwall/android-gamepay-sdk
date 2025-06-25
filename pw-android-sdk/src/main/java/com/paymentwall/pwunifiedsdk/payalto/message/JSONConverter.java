package com.paymentwall.pwunifiedsdk.payalto.message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paymentwall on 20/08/15.
 */
public class JSONConverter {

    public static List<PaymentStatus> getPaymentStatus(String json) throws Exception {
        try {
            JSONArray array = new JSONArray(json);
            List<PaymentStatus> paymentStatusList = new ArrayList<PaymentStatus>();
            if (array.length() == 0) return paymentStatusList;
            for (int i = 0; i < array.length(); i++) {
                if(!array.isNull(i)) {
                    PaymentStatus paymentStatus = getPaymentStatus(array.getJSONObject(i));
                    paymentStatusList.add(paymentStatus);
                }
            }
            return paymentStatusList;
        } catch (Exception e) {
            throw e;
        }
    }

    private static PaymentStatus getPaymentStatus(JSONObject jsonObject) throws Exception {
        PaymentStatus status = new PaymentStatus();

        if (jsonObject.has(PaymentStatus.K_OBJECT)) {
            status.setObject(jsonObject.getString(PaymentStatus.K_OBJECT));
        }

        if (jsonObject.has(PaymentStatus.K_AMOUNT)) {
            status.setAmount(jsonObject.getDouble(PaymentStatus.K_AMOUNT));
        }

        if (jsonObject.has(PaymentStatus.K_CREATED)) {
            status.setDateCreated(jsonObject.getLong(PaymentStatus.K_CREATED));
        }

        if (jsonObject.has(PaymentStatus.K_CURRENCY)) {
            status.setCurrency(jsonObject.getString(PaymentStatus.K_CURRENCY));
        }

        if (jsonObject.has(PaymentStatus.K_ID)) {
            status.setId(jsonObject.getString(PaymentStatus.K_ID));
        }

        if (jsonObject.has(PaymentStatus.K_PRODUCT_ID)) {
            status.setProductId(jsonObject.getString(PaymentStatus.K_PRODUCT_ID));
        }

        if (jsonObject.has(PaymentStatus.K_REFUNDED)) {
            status.setRefunded(jsonObject.getBoolean(PaymentStatus.K_REFUNDED));
        }

        if (jsonObject.has(PaymentStatus.K_RISK)) {
            status.setRiskStatus(jsonObject.getString(PaymentStatus.K_RISK));
        }

        if (jsonObject.has(PaymentStatus.K_UID)) {
            status.setUid(jsonObject.getString(PaymentStatus.K_UID));
        }

        if (jsonObject.has(PaymentStatus.K_SUBSCRIPTION)) {
            status.setSubscription(getSubscription(jsonObject.getJSONObject(PaymentStatus.K_SUBSCRIPTION)));
        }

        return status;
    }

    public static PaymentStatus getSinglePaymentStatus(String json) throws Exception {
        try {
            JSONArray array = new JSONArray(json);
            if (array.length() == 0) return null;
            if (array.length() > 1) throw new Exception("Got more than 1 payment status");
            PaymentStatus paymentStatus = getPaymentStatus(array.getJSONObject(0));
            return paymentStatus;
        } catch (JSONException e) {
            return null;
        }
    }

    private static Subscription getSubscription(JSONObject jsonObject) throws Exception {
        Subscription subscription = new Subscription();

        if (jsonObject.has(Subscription.K_ACTIVE)) {
            subscription.setActiveVal(jsonObject.getInt(Subscription.K_ACTIVE));
        }

        if (jsonObject.has(Subscription.K_DATE_NEXT)) {
            subscription.setDateNext(jsonObject.getLong(Subscription.K_DATE_NEXT));
        }

        if (jsonObject.has(Subscription.K_EXPIRED)) {
            subscription.setExpiredVal(jsonObject.getInt(Subscription.K_EXPIRED));
        }

        if (jsonObject.has(Subscription.K_ID)) {
            subscription.setId(jsonObject.getString(Subscription.K_ID));
        }

        if (jsonObject.has(Subscription.K_IS_TRIAL)) {
            subscription.setTrialVal(jsonObject.getInt(Subscription.K_IS_TRIAL));
        }

        if (jsonObject.has(Subscription.K_DATE_STARTED)) {
            subscription.setDateStarted(jsonObject.getLong(Subscription.K_DATE_STARTED));
        }

        if (jsonObject.has(Subscription.K_OBJECT)) {
            subscription.setObject(jsonObject.getString(Subscription.K_OBJECT));
        }

        if (jsonObject.has(Subscription.K_PAYMENTS_LIMIT)) {
            subscription.setPaymentLimit(jsonObject.getLong(Subscription.K_PAYMENTS_LIMIT));
        }

        if (jsonObject.has(Subscription.K_PERIOD)) {
            subscription.setPeriod(jsonObject.getString(Subscription.K_PERIOD));
        }

        if (jsonObject.has(Subscription.K_PERIOD_DURATION)) {
            subscription.setPeriodDuration(jsonObject.getInt(Subscription.K_PERIOD_DURATION));
        }

        if (jsonObject.has(Subscription.K_STARTED)) {
            subscription.setStartedVal(jsonObject.getInt(Subscription.K_STARTED));
        }

        return subscription;
    }
}
