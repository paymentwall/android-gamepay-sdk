package com.terminal3.t3gamepaysdksample.config

object Constants {
    const val CLIENT_RETURN_URL = "gpdemo://gamepay-redirect"

    // Endpoint on the merchant's server that make a charge requests to Brick API.
//    const val MERCHANT_CHARGE_ENDPOINT = "https://merchant-server.com/api/charge"
    const val MERCHANT_CHARGE_ENDPOINT = "http://develop.game-pay-demo-merchant-server.terminal3.stuffio.com/charge"

    // Secure URL to which the 3DS widget will redirect after successful authentication.
    // After redirection, the merchant should call MERCHANT_CHARGE_ENDPOINT again with the `token` to finalize the charge.
//    const val THREE_DS_RETURN_URL = "https://merchant-server.com/return-url"
    const val  THREE_DS_RETURN_URL = "http://develop.game-pay-demo-merchant-server.terminal3.stuffio.com"

    // production test
    const val PW_PROJECT_KEY = "";
    const val PW_SECRET_KEY = ""; // Should be get from merchant's server

    const val TERMS_OF_SERVICE_URL: String = "https://starbustergames.com/terms-of-service"
    const val PRIVACY_POLICY_URL: String = "https://starbustergames.com/privacy-policy"

    const val USER_ID: String = "12345"
    const val ITEM_GEM_ID: String = "gem0001"
    const val USER_EMAIL: String = "user_123@gmail.com"
    const val ITEM_NAME: String = "GEM"
    const val MERCHANT_NAME: String = "StarBuster"

    const val SERVER_ENV: String = "production"
}