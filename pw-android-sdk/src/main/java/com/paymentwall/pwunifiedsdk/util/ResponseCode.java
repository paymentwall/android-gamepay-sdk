package com.paymentwall.pwunifiedsdk.util;

/**
 * Created by nguyen.anh on 4/27/2016.
 */
public class ResponseCode {

    public static final int SUCCESSFUL = 1;

    // Invalid input information
    public static final int ERROR = 2;

    // A request was sent to PW server,
    // but the request has been rejected
    public static final int FAILED = 3;

    // The payment request was received
    // but it was not confirmed
    public static final int PROCESSING = 4;

    // The payment was cancelled
    public static final int CANCEL = 5;

    //Payment is continued processing by the merchant
    public static final int MERCHANT_PROCESSING = 6;
}
