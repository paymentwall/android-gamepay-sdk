package com.paymentwall.pwunifiedsdk.payalto.message;

/**
 * Created by paymentwall on 20/08/15.
 */
public class MultiPaymentStatusException extends Exception {
    public MultiPaymentStatusException(String detailMessage) {
        super(detailMessage);
    }
}
