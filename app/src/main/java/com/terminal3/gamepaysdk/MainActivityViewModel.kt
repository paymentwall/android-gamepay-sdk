package com.terminal3.gamepaysdk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.paymentwall.pwunifiedsdk.brick.core.Brick
import com.paymentwall.pwunifiedsdk.brick.core.BrickHelper
import com.paymentwall.pwunifiedsdk.util.ResponseCode
import com.terminal3.gamepaysdk.config.Constants
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.ProtocolException
import java.net.URL
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.Security
import java.security.cert.Certificate
import java.security.cert.CertificateEncodingException
import java.util.*
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLPeerUnverifiedException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


// Sealed class to represent different payment states
sealed class PaymentStatus(val message: String) {
    class Success(message: String) : PaymentStatus(message)
    class Processing(message: String) : PaymentStatus(message)
    class Failed(message: String) : PaymentStatus(message)
    class Cancelled(message: String) : PaymentStatus(message)
    class Unknown(message: String) : PaymentStatus(message)
}

class MainActivityViewModel : ViewModel() {

    public var _amount: Double = 0.0
        private set
    public var _currency: String = "USD"
        private set

    fun updateAmountAndCurrency(amount: Double, currency: String) {
        _amount = amount
        _currency = currency
    }

    fun processPayment(token: String, email: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                charge(token, email, _amount, _currency)
            }
        }
    }

    private suspend fun charge(token: String, email: String, amount: Double, currency: String) {
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
            }

            val secureRedirectUrl = "${Constants.THREE_DS_RETURN_URL}/index?token=$token&email=$email&amount=$amountStr&currency=$currency"
            parameters["secure_redirect_url"] = secureRedirectUrl

            val queryUrl = BrickHelper.urlEncodeUTF8(parameters)

            // Connect
            val conn = createPostRequest(URL(Constants.MERCHANT_CHARGE_ENDPOINT), queryUrl, 30000, 30000)
            Log.i("START CONNECT", queryUrl)

            // Get message
            val response = getResponseBody(conn.inputStream)
            Log.i("RESPONSE", response)
            val jsonObject = JSONObject(response)

            when {
                jsonObject.has("error") -> {
                    Brick.getInstance().setError(jsonObject.getString("error"))
                }
                jsonObject.has("card") -> {
                    val objCard = jsonObject.getJSONObject("card")
                    if (objCard.has("token")) {
                        Brick.getInstance().setResult(1, objCard.getString("token"))
                    } else {
                        Brick.getInstance().setError("Invalid card response")
                    }
                }
                jsonObject.has("secure") -> {
                    val objSecure = jsonObject.getJSONObject("secure")
                    val redirect = objSecure.getString("redirect")
                    Brick.getInstance().setResult(redirect)
                }
                else -> {
                    Brick.getInstance().setError("Unknown response format")
                }
            }

        } catch (e: Exception) {
            Brick.getInstance().setError(e.message ?: "Payment processing failed")
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