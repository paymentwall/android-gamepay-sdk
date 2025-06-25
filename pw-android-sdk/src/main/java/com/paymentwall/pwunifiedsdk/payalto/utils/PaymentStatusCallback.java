package com.paymentwall.pwunifiedsdk.payalto.utils;

import com.paymentwall.pwunifiedsdk.payalto.message.PaymentStatus;

import java.util.List;

/**
 * Callback interface for PaymentStatus
 **/
public interface PaymentStatusCallback {
    /**
     * Error callback
     **/
    void onError(Exception error);

    /**
     * Multiple Payment Statuses callback
     **/
    void onSuccess(List<PaymentStatus> paymentStatusList);
}
