package com.paymentwall.pwunifiedsdk.mobiamo.core;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.paymentwall.pwunifiedsdk.util.SmartLog;

public class MobiamoBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "MobiamoReceiver";
    public static final String SEND_SMS_ACTION = "com.paymentwall.mobiamosdk.SEND_SMS_ACTION";
    public static final String SMS_SENT_ACTION = "com.paymentwall.mobiamosdk.SMS_SENT_ACTION";
    public static final String TRANSACTION_COMPLETED = "completed";
	private Handler handler = new Handler();

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();

        SmartLog.e(TAG, "Action: " + action);

        if (action.equals(SEND_SMS_ACTION)) {

			final MobiamoPayment request = new MobiamoPayment();
			SmartLog.d(TAG, "onReceive: " + getResultCode());

            final MobiamoResponse response = MobiamoDialogActivity.PUBLIC_MOBIAMO_RESPONSE;

			if(response == null) {
                SmartLog.e(TAG, "MobiamoResponse null");
                return;
            }

			String transactionId = response.getTransactionId();
			if ("".equals(transactionId) || " ".equals(transactionId)
					|| transactionId == null) {
				Toast.makeText(context,  "Can not get transaction ID",
						Toast.LENGTH_SHORT).show();
				SmartLog.e(TAG, "Can not get transaction ID");
				return;
			}

			String resultText = MobiamoHelper.getString(Message.SMS_NOT_SEND);
			boolean isOk = false;

			Intent i = new Intent(SMS_SENT_ACTION);

			switch (getResultCode()) {
			case Activity.RESULT_OK:
				resultText = MobiamoHelper.getString(Message.SEND_SMS_SUCCESS);
//				response.setStatus(TRANSACTION_COMPLETED);
				response.setSendSms(true);
				response.setMessage(resultText);
				isOk = true;
				break;
			case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				resultText = MobiamoHelper.getString(Message.SEND_SMS_FAILED);
				break;
			case SmsManager.RESULT_ERROR_RADIO_OFF:
				resultText = MobiamoHelper
						.getString(Message.SEND_SMS_RADIO_OFF);
				break;
			case SmsManager.RESULT_ERROR_NULL_PDU:
				resultText = MobiamoHelper.getString(Message.SEND_SMS_NO_PDU);
				break;
			case SmsManager.RESULT_ERROR_NO_SERVICE:
				resultText = MobiamoHelper
						.getString(Message.SEND_SMS_NO_SERVICE);
				break;
			}

			if (!isOk) {
				if (!resultText.equals(MobiamoHelper
						.getString(Message.SMS_NOT_SEND))) {
					MobiamoHelper.showToast(resultText);
					response.setMessageStatus(MobiamoHelper.MESSAGE_STATUS_FAILED);
					response.setMessage(resultText);
//					listener.onPaymentFailed(response);
				} else {
					response.setMessageStatus(MobiamoHelper.MESSAGE_STATUS_NOT_SENT);
					response.setMessage(MobiamoHelper
							.getString(Message.ERROR_MSG_NOT_SEND));

				}
				i.putExtra("response", (Parcelable) response);
				LocalBroadcastManager.getInstance(context).sendBroadcast(i);
			}else{
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						request.getPaymentStatus(context, response, new MobiamoPayment.IPayment() {
							@Override
							public void onSuccess(MobiamoResponse response1) {
							    SmartLog.i("Mobiamo", "MobiamoBroadcastReceiver getPaymentStatus onSuccess");
								Intent i1 = new Intent(SMS_SENT_ACTION);
								i1.putExtra("response", (Parcelable) response1);
								LocalBroadcastManager.getInstance(context).sendBroadcast(i1);
							}

							@Override
							public void onError(MobiamoResponse response1) {
                                SmartLog.i("Mobiamo", "MobiamoBroadcastReceiver getPaymentStatus onError");
								Intent i2 = new Intent(SMS_SENT_ACTION);
								i2.putExtra("response", (Parcelable) response1);
								LocalBroadcastManager.getInstance(context).sendBroadcast(i2);
							}
						});
					}
				}, 3000);
			}
        }
    }

    public static SmsMessage[] getMessagesFromIntent(Intent intent) {
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        byte[][] pduObjs = new byte[messages.length][];

        for (int i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }
        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;

        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }
        return msgs;
    }

}
