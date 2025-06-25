package com.paymentwall.pwunifiedsdk.payalto.utils;

import com.paymentwall.pwunifiedsdk.payalto.message.PaymentStatus;

/**
 * Complex Callback interface for PaymentStatus. If there's only 1 payment status, onSuccessSingle() will be called.
 * If there is no payment status, onError() will be called.
 * If there are at least 2 payment statuses, onSuccess() will be called.
 **/
public interface PaymentStatusComplexCallback extends PaymentStatusCallback {
    /**
     * Single Payment Status callback
     **/
    void onSuccessSingle(PaymentStatus paymentStatus);
}
