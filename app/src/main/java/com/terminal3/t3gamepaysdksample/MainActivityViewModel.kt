package com.terminal3.t3gamepaysdksample

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.terminal3.gamepaysdk.BaseResp
import com.terminal3.gamepaysdk.GPApi
import com.terminal3.gamepaysdk.IGPAPIEventHandler
import com.terminal3.gamepaysdk.brick.core.BrickHelper
import com.terminal3.gamepaysdk.core.UnifiedRequest
import com.terminal3.gamepaysdk.payalto.utils.Const
import com.terminal3.gamepaysdk.payalto.utils.PayAltoMiscUtils
import com.terminal3.gamepaysdk.util.ResponseCode
import com.terminal3.t3gamepaysdksample.config.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.ProtocolException
import java.net.URL
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.Security
import java.security.cert.CertificateEncodingException
import java.util.Arrays
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLPeerUnverifiedException

// Sealed class to represent different payment states
sealed class PaymentStatus(val message: String) {
    class Success(message: String) : PaymentStatus(message)
    class Processing(message: String) : PaymentStatus(message)
    class Failed(message: String) : PaymentStatus(message)
    class Cancelled(message: String) : PaymentStatus(message)
    class Unknown(message: String) : PaymentStatus(message)
}

class MainActivityViewModel : ViewModel(), IGPAPIEventHandler {

    var isProcessing by mutableStateOf(false)
    var paymentStatus by mutableStateOf<PaymentStatus?>(null)

    var uid by mutableStateOf<String>(Constants.USER_ID)
    var chargeAmount by mutableStateOf<Double>(0.3)
    var chargeCurrency by mutableStateOf<String>("USD")
    var country by mutableStateOf<String>("US")

    init {
        GPApi.setEventHandler(this)
    }

    fun createPaymentRequest(): UnifiedRequest {
        isProcessing = true
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

                    Log.i("CHARGE_AMOUNT", amount.toString())
                    Log.i("CHARGE_CURRENCY", currency)
                    Log.i("CUSTOM_EMAIL", email ?: "")
                    Log.i("CUSTOM_TOKEN", token ?: "")
                    Log.i("CUSTOM_FIRST_NAME", firstName ?: "")
                    Log.i("CUSTOM_LAST_NAME", lastName ?: "")
                    Log.i("CUSTOM_REF_ID", refId ?: "")
                    Log.i("CUSTOM_CHARGE_ID", chargeId ?: "")
                    Log.i("CUSTOM_SECURE_TOKEN", brickSercureToken ?: "")
                    Log.i("CUSTOM_REFERRAL_CODE", referralCode ?: "")
                    Log.i("CUSTOM_HISTORY_ID", historyId ?: "")

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

    fun processPayment(token: String, amount: Double, currency: String, firstName: String, lastName: String,
                       email: String, refId: String = "", chargeId: String = "", secureToken: String = "",
                       customParams: Map<String, String>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                charge(token, firstName, lastName, email, amount, currency, refId, chargeId, secureToken, customParams)
            }
        }
    }

    private fun charge(token: String, firstName: String, lastName: String,
                       email: String, amount: Double, currency: String,
                       refId: String, chargeId: String, secureToken: String,
                       customParams: Map<String, String>) {
        // Prepare SSL
        var originalDNSCacheTTL: String? = null
        var allowedToSetTTL = true

        try {
            originalDNSCacheTTL = Security.getProperty("networkaddress.cache.ttl")
            Security.setProperty("networkaddress.cache.ttl", "0")
        } catch (se: SecurityException) {
            allowedToSetTTL = false
        }

        try {
            val amountStr = String.format("%.2f", amount).replace(",", ".")

            // Create HTTP request
            val parameters = HashMap<String, String>().apply {
                put("token", token)
                put("email", email)
                put("amount", amountStr)
                put("currency", currency)
                put("secure_return_method", "url")
                put("secretKey", Constants.PW_SECRET_KEY)
                if (firstName.isNotBlank()) {
                    put("firstname", firstName)
                }
                if (lastName.isNotBlank()) {
                    put("lastname", lastName)
                }
                if (refId.isNotBlank()) {
                    put("reference_id", refId)
                }
                if (chargeId.isNotBlank()) {
                    put("brick_charge_id", chargeId)
                }
                if (secureToken.isNotBlank()) {
                    put("brick_secure_token", secureToken)
                }
                customParams.forEach { (key, value) ->
                    put(key, value)
                }
//                put("env", "staging")
                put("env", Constants.SERVER_ENV)
            }

            val secureRedirectUrl = "${Constants.THREE_DS_RETURN_URL}/index?token=$token&email=$email&amount=$amountStr&currency=$currency"
            parameters["secure_redirect_url"] = secureRedirectUrl

            val queryUrl = BrickHelper.urlEncodeUTF8(parameters)

            // Connect
            val conn = createPostRequest(URL(Constants.MERCHANT_CHARGE_ENDPOINT), queryUrl, 30000, 30000)
            Log.i("START CONNECT", queryUrl)

            // Get charge response
            val response = getResponseBody(conn.inputStream)
            Log.i("RESPONSE", response)
            GPApi.setBrickResponse(response)

        } catch (e: Exception) {
            GPApi.setBrickError(e.message ?: "Payment processing failed")
            Log.e("BrickViewModel", "Payment error", e)
        } finally {
            if (allowedToSetTTL) {
                if (originalDNSCacheTTL == null) {
                    Security.setProperty("networkaddress.cache.ttl", "-1")
                } else {
                    Security.setProperty("networkaddress.cache.ttl", originalDNSCacheTTL)
                }
            }
        }
    }


    private fun createPostRequest(url: URL, queryUrl: String, connectionTimeout: Int, readTimeout: Int): HttpURLConnection {
        val conn = try {
            url.openConnection() as HttpURLConnection
        } catch (e: IOException) {
            throw e
        }

        conn.apply {
            connectTimeout = connectionTimeout
//            readTimeout = readTimeout
            useCaches = false
            doOutput = true
            try {
                requestMethod = "POST"
            } catch (e: ProtocolException) {
                throw e
            }
            setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
            setRequestProperty("x-apikey", Constants.PW_SECRET_KEY)
        }

        checkSSLCert(conn)

        var output: OutputStream? = null
        try {
            output = conn.outputStream
            output.write(queryUrl.toByteArray(Charsets.UTF_8))

            val statusCode = conn.responseCode
            if (statusCode < 200 || statusCode >= 300) {
                val errorResponse = try {
                    getResponseBody(conn.errorStream)
                } catch (e: Exception) {
                    throw e
                }
                throw Exception(errorResponse)
            }
        } catch (e: Exception) {
            throw e
        } finally {
            output?.close()
        }

        return conn
    }

    private fun getResponseBody(responseStream: InputStream): String {
        val rBody = getStringFromInputStream(responseStream)
        try {
            responseStream.close()
        } catch (e: IOException) {
            // Ignore
        }
        return rBody
    }

    private fun getStringFromInputStream(inputStream: InputStream): String {
        var br: BufferedReader? = null
        val sb = StringBuilder()

        try {
            br = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }
        } catch (e: IOException) {
            // Ignore
        } finally {
            br?.close()
        }

        return sb.toString()
    }

    private fun checkSSLCert(con: HttpURLConnection) {
        if (con.url.host != "pwgateway.com") {
            return
        }

        val conn = con as HttpsURLConnection
        try {
            conn.connect()
        } catch (e: IOException) {
            // Ignore
        }

        val certs = try {
            conn.serverCertificates
        } catch (e: SSLPeerUnverifiedException) {
            return
        }

        try {
            val md = MessageDigest.getInstance("SHA-1")
            val der = certs[0].encoded
            md.update(der)
            val digest = md.digest()
            val revokedCertDigest = byteArrayOf(
                5, -64, -77, 100, 54, -108, 71, 10,
                -120, -116, 110, 127, -21, 92, -98, 36, -24, 35, -36, 83
            )

            if (Arrays.equals(digest, revokedCertDigest)) {
                // Handle revoked certificate
            }
        } catch (e: NoSuchAlgorithmException) {
            // Ignore
        } catch (e: CertificateEncodingException) {
            // Ignore
        }
    }
}

class BrickViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainActivityViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}