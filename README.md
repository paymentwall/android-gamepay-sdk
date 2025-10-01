# Android GamePay SDK

## Table of Contents
* [Introduction](#introduction)
* [Features](#features)
* [Requirements](#requirements)
* [Installation](#installation)
* [How to run Demo app](#how-to-run-demo-app)
* [Latest update](#latest-update)
* [Credentials](#credentials)

## INTRODUCTION

Do you want to accept payments from mobile users in different countries, different payment system by writing just few of code lines? GamePay SDK is a global mobile payment gateway that accepts payments from more than 200 countries with 100+ alternative payment options. We now provide SDK for Android which will become a native part of your application, it eliminates the necessity to open a web browser for payments. Less steps, faster process, there's no doubt your conversion rate will get boost! All you have to do is to integrate the framework into your project to start accepting in-app payment. It is quick and easy! We'll guide you through the process here.

## FEATURES

* Easy integration of payment gateway
* Secure payment processing
* Fully compatible with Android applications
* Example project provided for easy setup and usage

## REQUIREMENTS

Android Studio 4.0+, Android API Level 24+ (Android 5.0+)

## INSTALLATION

You can integrate the SDK into your Android app in three simple ways:

### Gradle (Recommended):
Add the following dependency to your `app/build.gradle` file:

```gradle
dependencies {
    implementation 'com.terminal3:gamepaysdk:1.2.0'
}
```



## HOW TO RUN DEMO APP

1. **Setup Project Credentials**: To run Demo app, you need to setup your server and project keys. Obtain these Paymentwall API credentials in the application settings of your Merchant Account at Paymentwall.com
```kotlin
object Constants {
    // Deep link URL used to redirect the user back to the app 
    // after completing the payment in an external browser or app.
    // This must match the <data android:scheme="..." android:host="..."/> 
    // you declare in AndroidManifest.xml for PaymentSelectionActivity.
    const val CLIENT_RETURN_URL = "gpdemo://gamepay-redirect"

    // Endpoint on the merchant's server that make a charge requests to Brick API.
    const val MERCHANT_CHARGE_ENDPOINT = "https://merchant-server.com/api/charge"

    // Secure URL to which the 3DS widget will redirect after successful authentication.
    // After redirection, the merchant should call MERCHANT_CHARGE_ENDPOINT again with the `token` to finalize the charge.
    const val THREE_DS_RETURN_URL = "https://merchant-server.com/return-url"

    // production test
    const val PW_PROJECT_KEY = "";
    const val PW_SECRET_KEY = ""; // Should be get from merchant's server
...
}
```

2. **Run the Demo**:
   * Select your device/emulator
   * Click Run button

## LATEST UPDATE

Please check the demo app and the docs to see how to update your current code.
**Version 1.2.0 Features:**
* Support tax calculation for all countries
* Handle 3DS 2.0 authentication for Brick transactions
* Process native payment instructions for PayAlto

**Version 1.0.1 Features:**
* Initial release with core payment functionality
* Brick payment method support
* Automatic 3D Secure handling
* Comprehensive error handling

## CREDENTIALS

SDK integration requires a project keys. Obtain these Paymentwall API credentials in the application settings of your Merchant Account at Paymentwall.com

### Required Credentials:
- **Project Key**: Your public project identifier
- **Secret Key**: Your private project secret (for server-side operations)

### How to Get Credentials:
1. Log in to your Paymentwall Merchant Account
2. Go to Application Settings
3. Find your project credentials
4. Copy the Project Key and Secret Key

## CORE SDK INTEGRATION

For detailed integration instructions, please refer to our comprehensive documentation:

### Quick Start Guide:

1. **Implement Event Handler**:
```kotlin
class MainActivityViewModel : ViewModel(), IGPAPIEventHandler {
    init {
        GPApi.setEventHandler(this)
    }

    override fun onResp(resp: BaseResp?) {
        when (resp?.resultCode) {
            ResponseCode.SUCCESSFUL -> {
                isProcessing = false
                paymentStatus = PaymentStatus.Success("Payment completed successfully!")
            }

            ResponseCode.FAILED -> {
                isProcessing = false
                paymentStatus = PaymentStatus.Failed("Payment failed!")
            }

            ResponseCode.CANCEL -> {
                isProcessing = false
                paymentStatus = PaymentStatus.Cancelled("Payment cancelled!")
            }

            ResponseCode.ERROR -> {
                isProcessing = false
                val errMessage = resp.data.getStringExtra("error_message") ?: "An error occurred!"
                paymentStatus = PaymentStatus.Unknown(errMessage)
            }

            ResponseCode.MERCHANT_PROCESSING -> {
                val serviceType = resp.data.getStringExtra(GPApi.KEY_SERVICE_TYPE) ?: ""

                if (GPApi.SERVICE_TYPE_BRICK == serviceType) {
                    val token = resp.data.getStringExtra(GPApi.KEY_BRICK_TOKEN) ?: ""
                    val amount = resp.data.getDoubleExtra(GPApi.KEY_BRICK_CHARGE_AMOUNT, chargeAmount);
                    val currency = resp.data.getStringExtra(GPApi.KEY_BRICK_CHARGE_CURRENCY) ?: chargeCurrency

                    val email = resp.data.getStringExtra(GPApi.KEY_BRICK_EMAIL) ?: ""
                    val firstName = resp.data.getStringExtra(GPApi.KEY_BRICK_CARD_HOLDER_FIRST_NAME) ?: "";
                    val lastName = resp.data.getStringExtra(GPApi.KEY_BRICK_CARD_HOLDER_LAST_NAME) ?: "";

                    val refId = resp.data.getStringExtra(GPApi.KEY_BRICK_REF_ID) ?: "";
                    val chargeId = resp.data.getStringExtra(GPApi.KEY_BRICK_CHARGE_ID) ?: "";
                    val brickSercureToken = resp.data.getStringExtra(GPApi.KEY_BRICK_SECURE_TOKEN) ?: "";

                    val customParamsBundle = resp.data.getBundleExtra(GPApi.KEY_CUSTOM_PARAMS) ?: Bundle()
                    val referralCode = customParamsBundle.getString("referral_code") ?: ""
                    val historyId = customParamsBundle.getString("history_id") ?: ""
                    val customParams = mapOf(
                        "referral_code" to referralCode,
                        "history_id" to historyId
                    )

                    // Process the payment with current charge details
                    if (token.isNotEmpty() && email.isNotEmpty()) {
                        processPayment(token, amount, currency, firstName, lastName,
                            email, refId, chargeId, brickSercureToken,
                            customParams)
                    } else {
                        paymentStatus = PaymentStatus.Unknown("Invalid payment data received")
                    }
                }
            }
        }
    }
}

```

2. **Create a payment request**:
```kotlin
class MainActivityViewModel : AppCompatActivity(), IGPAPIEventHandler {
    fun createPaymentRequest(): UnifiedRequest {
        val request = UnifiedRequest()
        // Configure project keys
        request.pwProjectKey = Constants.PW_PROJECT_KEY
        request.pwSecretKey = Constants.PW_SECRET_KEY

        // Configure merchant details
        request.merchantName = Constants.MERCHANT_NAME
        request.merchantTermsOfServiceURL = Constants.TERMS_OF_SERVICE_URL
        request.merchantPrivacyPolicyURL = Constants.PRIVACY_POLICY_URL
        request.returnUrl = Constants.CLIENT_RETURN_URL

        // Configure payment request
        request.amount = chargeAmount
        request.currency = chargeCurrency
        request.userId = uid
        request.userEmail = Constants.USER_EMAIL
        request.itemId = Constants.ITEM_GEM_ID
        request.itemName = Constants.ITEM_NAME
        request.countryCode = country
        request.timeout = 30000
        request.signVersion = 3

        // Add custom parameters
        request.addCustomParam("referral_code", "AUB8712364")
        request.addCustomParam("history_id", "87782627")

        return request
    }
}
```

3. **Start Payment**:
```kotlin
class MainActivity : ComponentActivity() {
    private fun createPaymentRequest() {
        val request = vm.createPaymentRequest()
        GPApi.sendReq(this, request)
    }
}
```

---

## Support

For technical support and questions:
- 📧 Email: integration@terminal3.com
- 📖 Documentation: [Integration Guide](https://docs.terminal3.com/integration/sdks/game-pay-android)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
