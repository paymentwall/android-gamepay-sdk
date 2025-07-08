package com.terminal3.t3gamepaysdksample

import android.os.Bundle
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.terminal3.gamepaysdk.GPApi
import com.terminal3.t3gamepaysdksample.ui.theme.T3GamePaySDKSampleTheme

class MainActivity : ComponentActivity() {

    private val vm: MainActivityViewModel by viewModels { BrickViewModelFactory() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            T3GamePaySDKSampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PaymentScreen(
                        modifier = Modifier.padding(innerPadding),
                        paymentStatus = vm.paymentStatus,
                        amount = vm.chargeAmount,
                        currency = vm.chargeCurrency,
                        isProcessing = vm.isProcessing,
                        onPayClick = { createPaymentRequest() },
                        onClearStatus = { clearPaymentStatus() }
                    )
                }
            }
        }
    }


    private fun clearPaymentStatus() {
        vm.paymentStatus = null
    }

    private fun createPaymentRequest() {
        val request = vm.createPaymentRequest()
        GPApi.sendReq(this, request)
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
    T3GamePaySDKSampleTheme {
        Greeting("Android")
    }
}