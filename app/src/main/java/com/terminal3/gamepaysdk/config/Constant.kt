package com.terminal3.gamepaysdk.config

object Constants {
    // Endpoint on the merchant's server that make a charge requests to Paymentwall's Brick API.
    const val MERCHANT_CHARGE_ENDPOINT = "https://merchant-server.com/api/charge"

    // Secure URL to which the 3DS widget will redirect after successful authentication.
    // After redirection, the merchant should call MERCHANT_CHARGE_ENDPOINT again with the `token` to finalize the charge.
    const val THREE_DS_RETURN_URL = "https://merchant-server.com/return-url"


    // production test
    const val PW_PROJECT_KEY = "t_7bd5aa9b2b446d22b72d8eab962936"; // Sandbox test
    const val PW_SECRET_KEY = "t_711a250b792f6fe9f44c485e4f1bee";

    const val USER_ID: String = "12311111001233123"
    const val ITEM_GEM_ID: String = "gem0001"
    const val USER_EMAIL: String = "user_123@gmail.com"
    const val ITEM_NAME: String = "GEM"
    const val MERCHANT_NAME: String = "Aetherborne"
}