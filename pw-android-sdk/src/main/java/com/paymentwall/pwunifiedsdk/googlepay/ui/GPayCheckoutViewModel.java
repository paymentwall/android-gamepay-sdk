package com.paymentwall.pwunifiedsdk.googlepay.ui;

import androidx.lifecycle.AndroidViewModel;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.paymentwall.pwunifiedsdk.googlepay.core.GPayCore;

import org.json.JSONObject;

public class GPayCheckoutViewModel extends AndroidViewModel {
    // A client for interacting with the Google Pay API.
    private final PaymentsClient paymentsClient;

    // LiveData with the result of whether the user can pay using Google Pay
    private final MutableLiveData<Boolean> _canUseGooglePay = new MutableLiveData<>();

    public GPayCheckoutViewModel(@NonNull Application application) {
        super(application);
        paymentsClient = GPayCore.createPaymentsClient(application);

        fetchCanUseGooglePay();
    }

    public final LiveData<Boolean> canUseGooglePay = _canUseGooglePay;

    public void setCanUseGooglePay(boolean canUseGooglePay) {
        _canUseGooglePay.setValue(canUseGooglePay);
    }

    /**
     * Determine the user's ability to pay with a payment method supported by your app and display
     * a Google Pay payment button.
     */
    private void fetchCanUseGooglePay() {
        final JSONObject isReadyToPayJson = GPayCore.getInstance().getIsReadyToPayRequest();
        if (isReadyToPayJson == null) {
            _canUseGooglePay.setValue(false);
            return;
        }

        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString());
        Task<Boolean> task = paymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(
                completedTask -> {
                    if (completedTask.isSuccessful()) {
                        _canUseGooglePay.setValue(completedTask.getResult());
                    } else {
                        Log.w("isReadyToPay failed", completedTask.getException());
                        _canUseGooglePay.setValue(false);
                    }
                });
    }

    /**
     * Creates a Task that starts the payment process with the transaction details included.
     *
     * @param priceLabel the price to show on the payment sheet.
     * @return a Task with the payment information.
     */
    public Task<PaymentData> getLoadPaymentDataTask(String priceLabel, String currencyCode) {
        JSONObject paymentDataRequestJson = GPayCore.getInstance().getPaymentDataRequest(priceLabel, currencyCode);
        if (paymentDataRequestJson == null) {
            return null;
        }
        Log.d("GPay: paymentDataRequestJson", paymentDataRequestJson.toString());

        PaymentDataRequest request =
                PaymentDataRequest.fromJson(paymentDataRequestJson.toString());
        return paymentsClient.loadPaymentData(request);
    }
}
