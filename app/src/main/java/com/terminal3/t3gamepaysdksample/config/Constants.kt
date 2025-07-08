package com.terminal3.t3gamepaysdksample.config

object Constants {
    // Endpoint on the merchant's server that make a charge requests to Brick API.
//    const val MERCHANT_CHARGE_ENDPOINT = "https://merchant-server.com/api/charge"
    const val MERCHANT_CHARGE_ENDPOINT = "https://api.paymentwall.com/api/brick/charge"

    // Secure URL to which the 3DS widget will redirect after successful authentication.
    // After redirection, the merchant should call MERCHANT_CHARGE_ENDPOINT again with the `token` to finalize the charge.
    const val THREE_DS_RETURN_URL = "http://192.168.12.219:3000"

    // production test
    const val PW_PROJECT_KEY = "c741ef19877dd2047956cade07c9c7f6"; // GPay + Brick test
    const val PW_SECRET_KEY = "28b8049b4d7d8292a2cc4d37d7a0ea81";
//    const val PW_PROJECT_KEY = "";
//    const val PW_SECRET_KEY = ""; // Should be get from merchant's server

    const val USER_ID: String = "12311111001233123"
    const val ITEM_GEM_ID: String = "gem0001"
    const val USER_EMAIL: String = "user_123@gmail.com"
    const val ITEM_NAME: String = "GEM"
    const val MERCHANT_NAME: String = "Aetherborne"
}