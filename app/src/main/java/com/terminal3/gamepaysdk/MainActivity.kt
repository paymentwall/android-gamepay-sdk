package com.terminal3.gamepaysdk

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.paymentwall.pwunifiedsdk.brick.core.Brick
import com.paymentwall.pwunifiedsdk.core.PaymentSelectionActivity
import com.paymentwall.pwunifiedsdk.core.UnifiedRequest
import com.paymentwall.pwunifiedsdk.payalto.utils.Const
import com.paymentwall.pwunifiedsdk.payalto.utils.Key
import com.paymentwall.pwunifiedsdk.payalto.utils.ResponseCode
import com.paymentwall.pwunifiedsdk.util.SmartLog
import com.terminal3.gamepaysdk.config.Constants
import com.terminal3.gamepaysdk.ui.theme.T3GamePaySDKTheme


class MainActivity : ComponentActivity() {

    private val brickViewModel: MainActivityViewModel by viewModels { BrickViewModelFactory() }

    // State for payment status
    private var paymentStatus by mutableStateOf<PaymentStatus?>(null)
    private var chargeAmount by mutableStateOf<Double>(1.0)
    private var chargeCurrency by mutableStateOf<String>("USD")
    private var isProcessing by mutableStateOf(false)

    // BroadcastReceiver to handle Brick SDK callbacks
    private val brickReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action?.equals(
                    packageName + Brick.BROADCAST_FILTER_MERCHANT,
                    ignoreCase = true
                ) == true
            ) {
                SmartLog.i(
                    this@MainActivity::class.java.simpleName,
                    intent.getStringExtra(Brick.KEY_BRICK_TOKEN) ?: ""
                )

                val email = intent.getStringExtra(Brick.KEY_BRICK_EMAIL) ?: ""
                val token = intent.getStringExtra(Brick.KEY_BRICK_TOKEN) ?: ""

                // Process the payment with current charge details
                if (token.isNotEmpty() && email.isNotEmpty()) {
                    processPayment(token, email)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Invalid payment data received",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            T3GamePaySDKTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PaymentScreen(
                        modifier = Modifier.padding(innerPadding),
                        paymentStatus = paymentStatus,
                        amount = chargeAmount,
                        currency = chargeCurrency,
                        isProcessing = isProcessing,
                        onPayClick = { createPaymentRequest() },
                        onClearStatus = { clearPaymentStatus() }
                    )
                }
            }
        }
        registerBrickReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterBrickReceiver()
    }

    private fun registerBrickReceiver() {
        val filter = IntentFilter(packageName + Brick.BROADCAST_FILTER_MERCHANT)
        filter.addAction(packageName + Brick.BROADCAST_FILTER_MERCHANT)
        LocalBroadcastManager.getInstance(this).registerReceiver(brickReceiver, filter)
        SmartLog.d("MainActivity", "Brick receiver registered")
    }

    private fun unregisterBrickReceiver() {
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(brickReceiver)
            SmartLog.d("MainActivity", "Brick receiver unregistered")
        } catch (e: IllegalArgumentException) {
            // Receiver was not registered
            SmartLog.w("MainActivity", "Brick receiver was not registered")
        }
    }

    private fun clearPaymentStatus() {
        paymentStatus = null
    }

    private fun createPaymentRequest() {
        val request = UnifiedRequest()
        isProcessing = true
        request.pwProjectKey = Constants.PW_PROJECT_KEY
        request.pwSecretKey = Constants.PW_SECRET_KEY

        request.amount = chargeAmount
        request.currency = chargeCurrency
        request.userId = Constants.USER_ID
        request.userEmail = Constants.USER_EMAIL
        request.itemId = Constants.ITEM_GEM_ID
        request.itemName = Constants.ITEM_NAME
        request.merchantName = Constants.MERCHANT_NAME
        request.timeout = 30000
        request.signVersion = 3

        request.addBrick()
        request.enableFooter()
        request.addGooglePay()

        request.addPayAlto()
        request.addPayAltoParams(Const.P.WIDGET, "t3")
        request.addPayAltoParams(Const.P.COUNTRY_CODE, "KR")

        brickViewModel.updateAmountAndCurrency(chargeAmount, chargeCurrency)

        val intent = Intent(applicationContext, PaymentSelectionActivity::class.java)
        intent.putExtra(Key.REQUEST_MESSAGE, request)
        startActivityForResult(intent, PaymentSelectionActivity.REQUEST_CODE)
    }

    fun processPayment(token: String, email: String) {
        SmartLog.d("MainActivity", "Processing payment for $email")
        brickViewModel.processPayment(token, email)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Always stop processing indicator
        isProcessing = false

        if (resultCode == ResponseCode.SUCCESSFUL) {
            paymentStatus = PaymentStatus.Success("Payment completed successfully!")
            Toast.makeText(this, "SUCCESSFUL", Toast.LENGTH_SHORT).show()
        } else if (resultCode == ResponseCode.FAILED) {
            paymentStatus = PaymentStatus.Failed("Payment failed!")
            Toast.makeText(this, "FAILED", Toast.LENGTH_SHORT).show()
        } else if (resultCode == ResponseCode.CANCEL) {
            paymentStatus = PaymentStatus.Cancelled("Payment cancelled!")
            Toast.makeText(this, "CANCELLED", Toast.LENGTH_SHORT).show()
        } else if (resultCode == ResponseCode.ERROR) {
            val error = data?.getStringExtra("error") as? String
            paymentStatus = PaymentStatus.Unknown("An error occurred!\n" + error)
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    paymentStatus: PaymentStatus?,
    amount: Double,
    currency: String,
    isProcessing: Boolean,
    onPayClick: () -> Unit,
    onClearStatus: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Greeting section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "T3 GamePay SDK",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Payment Integration Demo",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        // Payment information
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Payment Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Amount:")
                    Text(
                        text = "${amount} ${currency}",
                        fontWeight = FontWeight.Medium
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Item:")
                    Text(text = "GEM", fontWeight = FontWeight.Medium)
                }
            }
        }

        // Payment button
        Button(
            onClick = onPayClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !isProcessing,
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isProcessing) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(text = "Processing...")
                }
            } else {
                Text(
                    text = "Start Payment",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Payment status display
        paymentStatus?.let { status ->
            PaymentStatusCard(
                status = status,
                onDismiss = onClearStatus
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun PaymentStatusCard(
    status: PaymentStatus,
    onDismiss: () -> Unit
) {
    val (backgroundColor, textColor, iconColor) = when (status) {
        is PaymentStatus.Success -> Triple(
            Color(0xFF4CAF50).copy(alpha = 0.1f),
            Color(0xFF2E7D32),
            Color(0xFF4CAF50)
        )

        is PaymentStatus.Processing -> Triple(
            Color(0xFFFF9800).copy(alpha = 0.1f),
            Color(0xFFE65100),
            Color(0xFFFF9800)
        )

        is PaymentStatus.Failed -> Triple(
            Color(0xFFF44336).copy(alpha = 0.1f),
            Color(0xFFC62828),
            Color(0xFFF44336)
        )

        is PaymentStatus.Cancelled -> Triple(
            Color(0xFF9E9E9E).copy(alpha = 0.1f),
            Color(0xFF424242),
            Color(0xFF9E9E9E)
        )

        is PaymentStatus.Unknown -> Triple(
            Color(0xFF9C27B0).copy(alpha = 0.1f),
            Color(0xFF6A1B9A),
            Color(0xFF9C27B0)
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (status) {
                        is PaymentStatus.Success -> "✅ Success"
                        is PaymentStatus.Processing -> "⏳ Processing"
                        is PaymentStatus.Failed -> "❌ Failed"
                        is PaymentStatus.Cancelled -> "⚠️ Cancelled"
                        is PaymentStatus.Unknown -> "❓ Unknown"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor,
                    fontWeight = FontWeight.SemiBold
                )

                TextButton(onClick = onDismiss) {
                    Text(text = "✕", color = textColor)
                }
            }

            Text(
                text = status.message,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    T3GamePaySDKTheme {
        Greeting("Android")
    }
}