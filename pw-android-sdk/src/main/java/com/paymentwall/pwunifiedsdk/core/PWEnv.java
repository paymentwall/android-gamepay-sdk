package com.paymentwall.pwunifiedsdk.core;

import com.google.android.gms.wallet.WalletConstants;

public class PWEnv {
    // Environment enum to clearly define possible environments
    public enum Environment {
        PRODUCTION
    }

    // Current environment (default to production)
    private static final Environment currentEnvironment = Environment.PRODUCTION;
    public static final int GPAY_ENVIRONMENT = WalletConstants.ENVIRONMENT_PRODUCTION;

    // Base URLs
    private static final String PRODUCTION_BASE_URL = "https://api.paymentwall.com";
    private static final String BRICK_PRODUCTION_URL = "https://pwgateway.com";
//    private static final String BRICK_STAGING_URL = "https://api.paymentwall.com";

    // Get the appropriate base URL based on current environment
    private static String getBaseUrl() {
        return PRODUCTION_BASE_URL;
    }

    private static String getBrickBaseUrl() {
        return BRICK_PRODUCTION_URL;
    }


    // region Brick endpoints
    public static final String BRICK_POST_TOKEN_URL = getBrickBaseUrl() + "/api/token";
    public static final String BRICK_POST_TOKEN_TEST_URL = getBaseUrl() + "/api/pro/v2/token";
    public static final String PW_GATEWAY_URL = "pwgateway.com"; // Domain
    public static final String BRICK_GENERATE_TOKEN_URL = getBaseUrl() + "/api/brick/token";
    public static final String BRICK_JS_URL = getBaseUrl() + "/api/brick-init/save?key=%s";
    // endregion

    // region Google Pay endpoints
    public static final String GOOGLE_PAY_INSTRUCTION_URL = getBaseUrl() + "/api/payments";
    public static final String GOOGLE_PAY_PAYMENT_URL = getBaseUrl() + "/api/gateway/payment";
    // endregion

    // region PayAlto endpoints
    public static final String PAY_ALTO_PAYMENT_SYSTEMS = getBaseUrl() + "/api/payment-systems";
    public static final String PAY_ALTO_PS = getBaseUrl() + "/api/ps/";
    public static final String PAY_ALTO_SUBSCRIPTION = getBaseUrl() + "/api/subscription/";
    public static final String PAY_ALTO_CART = getBaseUrl() + "/api/cart/";
    // endregion

}
