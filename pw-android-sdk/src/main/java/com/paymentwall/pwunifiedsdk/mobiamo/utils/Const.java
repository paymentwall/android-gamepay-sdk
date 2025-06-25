package com.paymentwall.pwunifiedsdk.mobiamo.utils;

public class Const {
    public static final float RATIO = 0.95f;

    public static final class KEY {
        public static final String REQUEST_TYPE = "request_type";
        public static final String PAYMENT_TYPE = "payment_type";
        public static final String REQUEST_MESSAGE = "request_message";
        public static final String GOOGLEPAY_TOKEN = "google_pay_token";
        public static final String RESPONSE_MESSAGE = "response_message";
        public static final String SDK_ERROR_MESSAGE = "sdk_error_message";
    }

    public static final class PAYMENT_METHOD {
        public static final int NULL = 0;
        public static final int PW_LOCAL_DEFAULT = 0x6278b88e;
        public static final int PW_LOCAL_FLEXIBLE = 0x692afe62;
        public static final int BRICK = 0x20402d6c;
        public static final int PW_PRO = BRICK;
        public static final int MOBIAMO = 0x74e6cbf2;
        public static final int MINT = 0x413548f7;
    }

    public static final class P {
        public static final String KEY = "key";
        public static final String SIGN = "sign";
        public static final String SIGN_VERSION = "sign_version";
        public static final String TS = "ts";
        public static final String UID = "uid";
    }

    public static final class RESPONSE_CODE {
        public static final int SUCCESSFUL = 1; // Payment was confirmed
        public static final int ERROR = 2; // User app has some problem with input information
        public static final int FAILED = 3; // Request was sent to PW server, but the request has been rejected
        public static final int PROCESSING = 4; // Payment request was received but it was not confirmed
        public static final int CANCEL = 5; // user cancel payment
    }

    public static final class POST_OPTION {
        public static final int PINLESS = 1;
        public static final int PRICEINFO = 2;
    }

    public static final class REGEX {
        public static final String HEXADECIMAL32 = "^[0-9a-fA-F]{32}$";
        public static final String MINT_REGEX = "^[0-9]{16}$";

    }

    public static final class VIEW_ID {
        public static final int PAY_ID = 10000;
        public static final int CANCEL_ID = 10001;
        public static final int HINT_ID = 10002;
        public static final int SPINER_ID = 10003;
        public static final int CONTENT_ID = 10004;
        public static final int NOTE_ID = 10005;
    }
}
