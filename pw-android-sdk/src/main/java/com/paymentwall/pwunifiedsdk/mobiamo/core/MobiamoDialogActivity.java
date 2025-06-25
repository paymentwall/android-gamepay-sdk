package com.paymentwall.pwunifiedsdk.mobiamo.core;//package com.paymentwall.sdk.mobiamo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.telephony.SmsManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.paymentwall.pwunifiedsdk.R;
import com.paymentwall.pwunifiedsdk.mobiamo.utils.Const;
import com.paymentwall.pwunifiedsdk.mobiamo.utils.ResponseKey;
import com.paymentwall.pwunifiedsdk.mobiamo.views.PWScrollView;
import com.paymentwall.pwunifiedsdk.util.ResponseCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MobiamoDialogActivity extends Activity {
    public static MobiamoResponse PUBLIC_MOBIAMO_RESPONSE = null;
    public static final int MOBIAMO_REQUEST_CODE = 0x112;

    private static final String DELIVERED_SMS_ACTION = "com.paymentwall.mobianosdk.DELIVERED_SMS_ACTION";
    private BroadcastReceiver deliveryBroadcastReceiver;

    private AlertDialog.Builder dialog;
    private AlertDialog.Builder helpBuilder;
    private boolean isCheck;
    private boolean isPaymentFail;
    private MobiamoPayment request;
    private String price;
    private MobiamoResponse response;
    private MobiamoPayment.IPayment listener;

    private ProgressDialog prgDialog;
    private AlertDialog priceDialog;
    private AlertDialog supportDialog;
    private AlertDialog paymentFailDialog;
    private AlertDialog paymentNotSupportDialog;
    private static int SCREEN_DENSITY;
    private static final String TRANSACTION_STATUS_COMPLETED = "completed";
    private String strInfo;
    private PriceObject selectedObject;
    private final int[] statePressed = {android.R.attr.state_pressed};
    private final int[] stateEnabled = {android.R.attr.state_enabled};

    //layout price
    private PWScrollView psvOuterContainer;
    private RelativeLayout rlOuterLayout;
    private TextView tvConfirmation, tvDescription, tvCancel;
    private ImageView ivHelp, ivLogo;
    private Button btnBuy;
    private Spinner spnPrice;
    private TextView tvAmount;

    //Layout help
    private LinearLayout llHelp;
    private TextView tvHelpMessage;
    private TextView tvHelpBack;

    //layout payment confirmation
    private PWScrollView psvConfirmOuterContainer;
    private RelativeLayout rlConfirmOuterLayout;
    private TextView tvConfirmConfirmation, tvConfirmDescription, tvConfirmCancel;
    private ImageView ivConfirmHelp, ivConfirmLogo;
    private Button btnConfirmBuy;

    //Layout payment not supported
    private LinearLayout llPaymentNotSupported;
    private TextView tvPaymentNotSupportedOK;

    //Layout payment failed
    private ImageView ivSmile;
    private TextView tvClose, tvHelp;

    //Layout payment successful
    private ImageView ivCheck;
    private TextView tvMessage;
    private TextView tvOk;

    private LayoutInflater inflater;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Answers.getInstance().logCustom(new CustomEvent("Visit-MobiamoScreen"));

        getScreenDensity();
        MobiamoHelper.setContext(this);
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        init();
    }

    private void init() {
        if (!MobiamoHelper.hasConnectivity()) {
            /*MobiamoHelper.showToast(MobiamoHelper
                    .getString(Message.NO_INTERNET_CONNECTION));*/
            Intent intent = new Intent();
            intent.putExtra(ResponseKey.SDK_ERROR_MESSAGE, Message.NO_INTERNET_CONNECTION);
            setResult(ResponseCode.ERROR, intent);
            Toast.makeText(this, getString(R.string.err_no_internet_connection), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (!MobiamoHelper.hasSimReady(MobiamoDialogActivity.this)) {
            /*MobiamoHelper.showToast(MobiamoHelper.getString(Message.NO_SIM));*/
            Intent intent = new Intent();
            intent.putExtra(ResponseKey.SDK_ERROR_MESSAGE, Message.NO_SIM);
            setResult(ResponseCode.ERROR, intent);
            Toast.makeText(this, getString(R.string.err_no_sim), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        createDialog();
        bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(Const.KEY.REQUEST_MESSAGE)) {
            request = (MobiamoPayment) bundle.getSerializable(
                    Const.KEY.REQUEST_MESSAGE);
            request.setTestMode(false);
        }
        if (request == null) {
            Intent intent = new Intent();
            intent.putExtra(ResponseKey.SDK_ERROR_MESSAGE, Message.NO_REQUEST_FOUND);
            setResult(com.paymentwall.pwunifiedsdk.util.ResponseCode.ERROR, intent);
            finish();
            return;
        }

        MobiamoResponse response = new MobiamoResponse();
        this.response = response;
        if (isShowPaymentConfirmation(request)) {
            showDialog();
            final int option = Const.POST_OPTION.PRICEINFO;
            request.postData(this, response, Const.POST_OPTION.PRICEINFO, new MobiamoPayment.IPayment() {
                @Override
                public void onSuccess(MobiamoResponse response) {
                    dismissDialog();
                    if (option == Const.POST_OPTION.PRICEINFO) {
                        showPaymentConfirmDialog(response);
                    } else {
                        notifyPostResult(response);
                    }
                }

                @Override
                public void onError(MobiamoResponse response) {
                    dismissDialog();
                    showPaymentFailDialog(response.getMessage());
                }
            });
        } else {
            showDialog();
            request.getPricePoint(this, response, new MobiamoPayment.IGetPricepoint() {
                @Override
                public void onSuccess(MobiamoResponse response, ArrayList<?> sortedList, String[] priceArray) {
                    dismissDialog();
                    showDialogPrice(response, sortedList, priceArray);
                }

                @Override
                public void onError(MobiamoResponse response) {
                    dismissDialog();
                    showPaymentFailDialog(getString(R.string.payment_not_supported_message));
                }
            });
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unRegisterBroadcastReceiver();
    }

    void unRegisterBroadcastReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(deliveryBroadcastReceiver);
    }

    void registerBroadcastReceiver() {
        deliveryBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context _context, Intent _intent) {
                Log.i("On_receive", _intent.getAction());
                if (_intent.getAction().equalsIgnoreCase(MobiamoBroadcastReceiver.SMS_SENT_ACTION)) {
                    dismissDialog();
                    MobiamoResponse response = (MobiamoResponse) _intent.getParcelableExtra("response");
                    if (response.getSendSms()) {
                        if (TRANSACTION_STATUS_COMPLETED.equalsIgnoreCase(response
                                .getStatus()))
                            showPaymentSuccessDialog(_intent);
                        else
                            showPaymentFailDialog(response.getMessage());
                    } else {
//                        getMainActivity().backFragment(null);
                    }
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MobiamoBroadcastReceiver.SMS_SENT_ACTION);

        LocalBroadcastManager.getInstance(this).registerReceiver(deliveryBroadcastReceiver, intentFilter);
    }

    public void notifyPostResult(final MobiamoResponse response) {
        sendSMS(response);
    }

    @SuppressLint("InlinedApi")
    public void showDialogPrice(final MobiamoResponse response,
                                ArrayList<?> list, String[] pricepointsArray) {
        if (dialog != null) {
            dialog = null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog = new AlertDialog.Builder(new ContextThemeWrapper(
                    MobiamoDialogActivity.this,
                    android.R.style.Theme_Material_Light_Dialog_NoActionBar));
        } else {
            dialog = new AlertDialog.Builder(new ContextThemeWrapper(
                    MobiamoDialogActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar));
        }

        View dialogView = inflater.inflate(R.layout.layout_price, null);

        psvOuterContainer = (PWScrollView) dialogView.findViewById(R.id.svOuterContainer);
        tvConfirmation = (TextView) dialogView.findViewById(R.id.tvConfirmation);
        tvDescription = (TextView) dialogView.findViewById(R.id.tvDescription);
        spnPrice = (Spinner) dialogView.findViewById(R.id.spnPrice);
        ivLogo = (ImageView) dialogView.findViewById(R.id.ivLogo);
        ivHelp = (ImageView) dialogView.findViewById(R.id.ivHelp);
        tvCancel = (TextView) dialogView.findViewById(R.id.tvCancel);

        psvOuterContainer.setVisibility(View.VISIBLE);

        tvConfirmation.setTextAppearance(this,
                android.R.style.TextAppearance_Small);

        setConfirmationMessage(tvConfirmation, tvDescription,
                (PriceObject) list.get(0));

        PwSdkSpinnerAdapter spinnerArrayAdapter = new PwSdkSpinnerAdapter(this,
                Arrays.asList(pricepointsArray));
        spinnerArrayAdapter
                .setDropDownViewResource(R.layout.row_spn_dropdown);
        spnPrice.setAdapter(spinnerArrayAdapter);
        spnPrice.setOnItemSelectedListener(new PwSdkSpinner(
                tvConfirmation, tvDescription, list));

        spnPrice.setSelection(0);

        // Title confirmation
        tvConfirmation.setTextColor(Color.BLACK);

        tvConfirmation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        ivHelp.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                showHelpDialog();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                response.setMessageStatus(MobiamoHelper.MESSAGE_STATUS_NOT_SENT);
                response.setMessage(MobiamoHelper
                        .getString(Message.USER_DID_NOT_ACCEPT_AND_PAY));
                finish();
            }
        });

        btnBuy = (Button) dialogView.findViewById(R.id.btnBuy);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int option = Const.POST_OPTION.PINLESS;
                request.setAmount(price);
                response.setPrice(price);
                response.setProductId(request.getProductId());
                response.setProductName(request.getProductName());
                if (MobiamoHelper.hasConnectivity()) {
                    request.postData(MobiamoDialogActivity.this, response, option, new MobiamoPayment.IPayment() {
                                @Override
                                public void onSuccess(MobiamoResponse response) {
                                    if (option == Const.POST_OPTION.PRICEINFO) {
                                        showPaymentConfirmDialog(response);
                                    } else {
                                        notifyPostResult(response);
                                    }
                                }

                                @Override
                                public void onError(MobiamoResponse response) {
                                    dismissDialog();
                                    showPaymentFailDialog(getString(R.string.payment_not_supported_message));
                                }
                            }
                    );
                    showDialog();
                } else {
                    MobiamoHelper.showToast(MobiamoHelper
                            .getString(Message.NO_INTERNET_CONNECTION));
                    response.setMessage(MobiamoHelper
                            .getString(Message.NO_INTERNET_CONNECTION));
                }
            }
        });

        dialog.setView(dialogView);
        priceDialog = dialog.create();

        if (prgDialog != null && prgDialog.isShowing()) {
            prgDialog.dismiss();
        }

        priceDialog.show();
        if (isTablet()) {
            priceDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
        } else {
            priceDialog.getWindow().setLayout(convertDptoPixel(296),
                    LayoutParams.WRAP_CONTENT);
        }

        priceDialog.setCanceledOnTouchOutside(false);
        priceDialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (isCheck) {
                        isCheck = false;
                        return false;
                    } else {
                        response.setMessageStatus(MobiamoHelper.MESSAGE_STATUS_NOT_SENT);
                        response.setMessage(MobiamoHelper
                                .getString(Message.USER_DID_NOT_ACCEPT_AND_PAY));
//                        listener.onPaymentCanceled(response);
                        priceDialog.dismiss();
                    }
                }
                return true;
            }
        });
    }

    @SuppressLint("InlinedApi")
    public void showPaymentConfirmDialog(final MobiamoResponse response) {
        if (dialog != null) {
            dialog = null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog = new AlertDialog.Builder(new ContextThemeWrapper(
                    MobiamoDialogActivity.this,
                    android.R.style.Theme_Material_Light_Dialog_NoActionBar));
        } else {
            dialog = new AlertDialog.Builder(new ContextThemeWrapper(
                    MobiamoDialogActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar));
        }

        View paymentConfirmView = inflater.inflate(R.layout.layout_payment_confirmation, null);

        psvConfirmOuterContainer = (PWScrollView) paymentConfirmView.findViewById(R.id.svConfirmOuterContainer);
        tvConfirmConfirmation = (TextView) paymentConfirmView.findViewById(R.id.tvConfirmConfirmation);
        tvConfirmConfirmation.setTextAppearance(this, android.R.style.TextAppearance_Small);
        tvConfirmDescription = (TextView) paymentConfirmView.findViewById(R.id.tvConfirmDescription);
        ivConfirmLogo = (ImageView) paymentConfirmView.findViewById(R.id.ivConfirmLogo);
        ivConfirmHelp = (ImageView) paymentConfirmView.findViewById(R.id.ivConfirmHelp);
        tvConfirmCancel = (TextView) paymentConfirmView.findViewById(R.id.tvConfirmCancel);
        btnConfirmBuy = (Button) paymentConfirmView.findViewById(R.id.btnConfirmBuy);


//        strInfo = response.getRegulatoryText();
        // Parent LinearLayout

//        tvConfirmConfirmation.setText(Html
//                .fromHtml(getString(R.string.confirmation_message) + "<br><b>"
//                        + request.getProductName() + "</b> for <b>"
//                        + request.getAmount() + " " + request.getCurrency()
//                        + " ?</b>"));
        tvConfirmConfirmation.setText(Html
                .fromHtml(String.format(getString(R.string.confirmation_message),
                        request.getProductName(),
                        request.getAmount() + " " + request.getCurrency()
                )));

        tvConfirmDescription.setText(String.format(getString(R.string.description_message), request.getAmount() + " "
                + request.getCurrency()));

        ivConfirmHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelpDialog();
            }
        });

        tvConfirmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                response.setMessageStatus(MobiamoHelper.MESSAGE_STATUS_NOT_SENT);
                response.setMessage("User did not ACCEPT & PAY");
                finish();
//                listener.onPaymentCanceled(response);
            }
        });


        btnConfirmBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strShortCode = response.getShortcode();
                String strText = response.getKeyword() + " "
                        + response.getTransactionId();
                if (strShortCode.trim().length() > 0
                        && strText.trim().length() > 0) {
                    response.setPrice(request.getAmount());

                    createDialog();
                    showDialog();
                    sendSMS(response);

                } else {
                    final int option = Const.POST_OPTION.PINLESS;
                    response.setPrice(request.getAmount());
                    if (MobiamoHelper.hasConnectivity()) {
                        request.postData(MobiamoDialogActivity.this, response, option, new MobiamoPayment.IPayment() {
                                    @Override
                                    public void onSuccess(MobiamoResponse response) {
                                        if (option == Const.POST_OPTION.PRICEINFO) {
                                            showPaymentConfirmDialog(response);
                                        } else {
                                            notifyPostResult(response);
                                        }
                                    }

                                    @Override
                                    public void onError(MobiamoResponse response) {
                                        dismissDialog();
                                        showPaymentFailDialog(response.getMessage());
                                    }
                                }
                        );
                        response.setProductId(request.getProductId());
                        response.setProductName(request.getProductName());

                        psvOuterContainer.setVisibility(View.VISIBLE);
                        createDialog();
                        showDialog();
                    } else {
                        MobiamoHelper.showToast(MobiamoHelper
                                .getString(Message.NO_INTERNET_CONNECTION));
                        response.setMessage(MobiamoHelper
                                .getString(Message.NO_INTERNET_CONNECTION));
                    }
                }
            }
        });

        dialog.setView(paymentConfirmView);
        priceDialog = dialog.create();
        priceDialog.show();
        if (prgDialog != null && prgDialog.isShowing()) {
            prgDialog.dismiss();
        }
        if (!isTablet()) {
            priceDialog.getWindow().setLayout(convertDptoPixel(296),
                    LayoutParams.WRAP_CONTENT);
        }
        priceDialog.setCanceledOnTouchOutside(false);
        priceDialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (isCheck) {
                        isCheck = false;
                        return false;
                    } else {
                        response.setMessageStatus(MobiamoHelper.MESSAGE_STATUS_NOT_SENT);
//                        listener.onPaymentCanceled(response);
                        priceDialog.dismiss();
                    }
                }
                return true;
            }
        });
    }

    @SuppressLint("InlinedApi")
    public void showNotSupportDialog(final MobiamoResponse paymentResponse) {

        if (dialog != null) {
            dialog = null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog = new AlertDialog.Builder(new ContextThemeWrapper(
                    MobiamoDialogActivity.this,
                    android.R.style.Theme_Material_Light_Dialog_NoActionBar));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            dialog = new AlertDialog.Builder(new ContextThemeWrapper(
                    MobiamoDialogActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar));
        } else {
            dialog = new AlertDialog.Builder(MobiamoDialogActivity.this);
        }

        View notSupportView = inflater.inflate(R.layout.layout_payment_not_support, null);
        llPaymentNotSupported = (LinearLayout) notSupportView.findViewById(R.id.llPaymentNotSupported);
        tvPaymentNotSupportedOK = (TextView) notSupportView.findViewById(R.id.tvNotSupportedOk);

        tvPaymentNotSupportedOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                paymentListener.onPaymentFailed(paymentResponse);
            }
        });
        dialog.setView(notSupportView);
        paymentNotSupportDialog = dialog.create();
        paymentNotSupportDialog.show();

        if (prgDialog != null && prgDialog.isShowing()) {
            prgDialog.dismiss();
        }

        if (!isTablet()) {
            paymentNotSupportDialog.getWindow().setLayout(
                    convertDptoPixel(296), LayoutParams.WRAP_CONTENT);
        }

        paymentNotSupportDialog.setCanceledOnTouchOutside(false);
        paymentNotSupportDialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    paymentListener.onPaymentFailed(paymentResponse);
                }
                return true;
            }
        });

    }

    public void setCallbackMethod(Intent intent, int result) {
        if (prgDialog != null && prgDialog.isShowing()) {
            prgDialog.dismiss();
        }
        setResult(result, intent);
        if (intent != null) {
            MobiamoResponse response = (MobiamoResponse) intent
                    .getSerializableExtra(Const.KEY.RESPONSE_MESSAGE);
            if (response.getSendSms()) {
                if (TRANSACTION_STATUS_COMPLETED.equalsIgnoreCase(response
                        .getStatus()))
                    showPaymentSuccessDialog(intent);
                else
                    showPaymentFailDialog("");
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

//    public void setCallbackMethod(Intent intent, int result){
//        if(prgDialog != null && prgDialog.isShowing()){
//            prgDialog.dismiss();
//        }
//    }

    private class PwSdkSpinner implements Spinner.OnItemSelectedListener {

        private final TextView txtConfirmationMessage;
        private final TextView txtDescription;
        private ArrayList<?> mList;

        public PwSdkSpinner(TextView confirm, TextView remind, ArrayList<?> list) {
            txtConfirmationMessage = confirm;
            txtDescription = remind;
            mList = list;
        }


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            PriceObject obj = (PriceObject) mList.get(position);
            setConfirmationMessage(txtConfirmationMessage, txtDescription, obj);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public void setConfirmationMessage(TextView txtConfirmationMessage,
                                       TextView txtDescription, PriceObject obj) {
        selectedObject = obj;
        price = obj.getKeyValue();

        txtConfirmationMessage.setText(Html
                .fromHtml(String.format(getString(R.string.confirmation_message),
                        request.getProductName(),
                        request.getAmount() + " " + request.getCurrency()
                )));

        txtDescription.setText(String.format(getString(R.string.description_message), obj.getKeyValue() + " " + obj.getCurrencyValue()));

        if (MobiamoHelper.isRoamingState(MobiamoHelper.currentContext)) {
            txtDescription
                    .setText(Html.fromHtml(txtDescription.getText()
                            + " <b>" + getString(R.string.description_roaming_state) + "</b>"));
        }
    }

    private void sendSMS(final MobiamoResponse response) {
        Log.i("Mobiamo", "sendSMS 001 response = "+response);
        Intent sentIntent = new Intent(MobiamoBroadcastReceiver.SEND_SMS_ACTION);
        sentIntent.setClass(this, MobiamoBroadcastReceiver.class);
        // sentIntent.putExtra("request", request);

        PUBLIC_MOBIAMO_RESPONSE = response;
//        sentIntent.putExtra("listener", listener);

        PendingIntent sentPI = PendingIntent.getBroadcast(
                getApplicationContext(),
                0,
                sentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Create the deliveryIntent parameter
        Intent deliveryIntent = new Intent(DELIVERED_SMS_ACTION);
        deliveryIntent.setClass(this, MobiamoDialogActivity.class);
        PendingIntent deliverPI = PendingIntent.getBroadcast(this, 0,
                deliveryIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String address = response.getShortcode();
        String text = response.getKeyword() + " " + response.getTransactionId();

        try {
            // Send the message
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(address, null, text, sentPI, deliverPI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        request = null;
        releaseDialog();
        MobiamoHelper.release();
        MobiamoPayment.release();
        super.onDestroy();
    }

    private void releaseDialog() {
        if (priceDialog != null && priceDialog.isShowing()) {
            priceDialog.dismiss();
        }

        if (paymentFailDialog != null && paymentFailDialog.isShowing()) {
            paymentFailDialog.dismiss();
        }

        if (supportDialog != null && supportDialog.isShowing()) {
            supportDialog.dismiss();
        }

        if (prgDialog != null && prgDialog.isShowing()) {
            prgDialog.dismiss();
        }
    }


    int convertDptoPixel(int dp) {
        Resources r = this.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, r.getDisplayMetrics());
        return px;
    }

    void getScreenDensity() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        SCREEN_DENSITY = metrics.densityDpi;
    }

    private static class PwSdkSpinnerAdapter extends ArrayAdapter<String> {

        private PwSdkSpinnerAdapter(Context context, List<String> items) {
            super(context, R.layout.row_spn_dropdown, items);
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            if (view != null && view instanceof CheckedTextView) {
                ((CheckedTextView) view).setCheckMarkDrawable(null);
            }
            return view;
        }

        @SuppressLint("InlinedApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            if (view != null && view instanceof TextView) {
                ((TextView) view).setTextAppearance(getContext(),
                        android.R.style.TextAppearance_DeviceDefault_Small);
                ((TextView) view).setTypeface(null, Typeface.BOLD);
                ((TextView) view).setTextColor(Color.BLACK);
                ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                ((TextView) view).setPadding(0, 0, 0, 0);
            }
            return view;
        }
    }
    void createDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            prgDialog = new ProgressDialog(MobiamoDialogActivity.this,
                    android.R.style.Theme_Material_Light_Dialog);
        }  else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            prgDialog = new ProgressDialog(MobiamoDialogActivity.this,
                    android.R.style.Theme_Material_Light_Dialog);
        } else {
            prgDialog = new ProgressDialog(MobiamoDialogActivity.this);
        }
        prgDialog.setMessage(getString(R.string.initiate_purchase_message));
        prgDialog.setCancelable(false);
    }

    void showDialog() {
        prgDialog.show();
        WindowManager.LayoutParams params = prgDialog.getWindow()
                .getAttributes();
        params.gravity = Gravity.CENTER;
        prgDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

    }

    public void dismissDialog() {
        if (prgDialog != null && prgDialog.isShowing()) {
            prgDialog.dismiss();
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    void showHelpDialog() {
        if (prgDialog != null && prgDialog.isShowing()) {
            prgDialog.dismiss();
        }

        if (helpBuilder != null) {
            helpBuilder = null;
        }

        helpBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar));

        View helpView = inflater.inflate(R.layout.layout_mobiamo_help, null);

//        llHelp = (LinearLayout) helpView.findViewById(R.id.llHelp);
        tvHelpMessage = (TextView) helpView.findViewById(R.id.tvMessage);
        tvHelpBack = (TextView) helpView.findViewById(R.id.tvBack);


        String strPriceInfo = "";
        if (selectedObject != null) {
            strPriceInfo = selectedObject.getPriceInfo().replaceAll("#sep#",
                    "\r\n");

        } else {
            if (strInfo != null) {
                strPriceInfo = strInfo.replaceAll("#sep#", "\r\n");
                tvHelpMessage.setText(strPriceInfo);
            }
        }
        tvHelpMessage.setText(strPriceInfo);

        tvHelpMessage.setMovementMethod(LinkMovementMethod.getInstance());
        Linkify.addLinks(tvHelpMessage, Linkify.PHONE_NUMBERS
                | Linkify.EMAIL_ADDRESSES);

        tvHelpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPaymentFail) {
                    supportDialog.dismiss();
                    paymentFailDialog.show
                            ();
                    isPaymentFail = false;
                } else {
                    supportDialog.dismiss();
                    priceDialog.show();
                }
            }
        });

        helpBuilder.setView(helpView);
        supportDialog = helpBuilder.create();
        supportDialog.show();
        if (!isTablet()) {
            supportDialog.getWindow().setLayout(convertDptoPixel(296),
                    LayoutParams.WRAP_CONTENT);
        }
        supportDialog.setCanceledOnTouchOutside(false);
        supportDialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    isCheck = true;
                    if (isPaymentFail) {
                        supportDialog.dismiss();
                        paymentFailDialog.show();
                        isPaymentFail = false;
                    } else {
                        supportDialog.dismiss();
                        priceDialog.show();
                    }
                }
                return false;
            }
        });
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    void showPaymentFailDialog(String message) {

        if (prgDialog != null && prgDialog.isShowing()) {
            prgDialog.dismiss();
        }

        if (dialog != null) {
            dialog = null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog = new AlertDialog.Builder(new ContextThemeWrapper(
                    MobiamoDialogActivity.this,
                    android.R.style.Theme_Material_Light_Dialog_NoActionBar));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            dialog = new AlertDialog.Builder(new ContextThemeWrapper(
                    MobiamoDialogActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar));
        } else {
            dialog = new AlertDialog.Builder(new ContextThemeWrapper(
                    MobiamoDialogActivity.this,
                    android.R.style.Theme_Dialog));
        }

        View paymentFailedView = inflater.inflate(R.layout.layout_payment_fail, null);

        ivSmile = (ImageView) paymentFailedView.findViewById(R.id.ivSmile);
        tvClose = (TextView) paymentFailedView.findViewById(R.id.tvClose);
        tvHelp = (TextView) paymentFailedView.findViewById(R.id.tvHelp);

//        Bitmap bmpSmile = MobiamoImageLoader
//                .loadBitmap(SCREEN_DENSITY, MobiamoImageLoader.IMG_SMILE);
//        Drawable drawableSmile = new BitmapDrawable(getResources(), bmpSmile);
//        ivSmile.setImageDrawable(drawableSmile);

        tvClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setResult(ResponseCode.FAILED);
                finish();
            }
        });

        tvHelp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isPaymentFail = true;
                if (paymentFailDialog != null) {
                    paymentFailDialog.dismiss();
                }
                showHelpDialog();
            }
        });

        dialog.setView(paymentFailedView);
        paymentFailDialog = dialog.create();
        paymentFailDialog.show();
        if (!isTablet()) {
            paymentFailDialog.getWindow().setLayout(convertDptoPixel(244),
                    LayoutParams.WRAP_CONTENT);
        } else {
            paymentFailDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
        }
        paymentFailDialog.setCanceledOnTouchOutside(false);
        paymentFailDialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (isCheck) {
                        isCheck = false;
                        return false;
                    } else {
                        paymentFailDialog.dismiss();
                        setResult(ResponseCode.FAILED);
                        finish();
                    }
                }
                return true;
            }
        });
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    void showPaymentSuccessDialog(Intent intent) {

        Toast.makeText(this, "Payment successful", Toast.LENGTH_SHORT).show();

        setResult(ResponseCode.SUCCESSFUL);
        finish();
        return;

//        if (prgDialog != null && prgDialog.isShowing()) {
//            prgDialog.dismiss();
//        }
//
//        if (dialog != null) {
//            dialog = null;
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            dialog = new AlertDialog.Builder(new ContextThemeWrapper(
//                    MobiamoDialogActivity.this,
//                    android.R.style.Theme_Material_Light_Dialog_NoActionBar));
//        } else {
//
//            dialog = new AlertDialog.Builder(new ContextThemeWrapper(
//                    MobiamoDialogActivity.this,
//                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar));
//        }
//
//        View paymentSuccessfulView = inflater.inflate(R.layout.layout_payment_successful, null);

//        ivCheck = (ImageView) paymentSuccessfulView.findViewById(R.id.ivCheck);
//        tvMessage = (TextView) paymentSuccessfulView.findViewById(R.id.tvMessage);
//        tvOk = (TextView) paymentSuccessfulView.findViewById(R.id.tvOk);


//        Bitmap bmpSmile = MobiamoImageLoader
//                .loadBitmap(SCREEN_DENSITY, MobiamoImageLoader.IMG_CHECK);
//        Drawable drawableSmile = new BitmapDrawable(getResources(), bmpSmile);
//        ivCheck.setImageDrawable(drawableSmile);

//        String strPrice = null;
//        if (intent != null) {
//            MobiamoResponse response = (MobiamoResponse) intent
//                    .getSerializableExtra(Const.KEY.RESPONSE_MESSAGE);
//            strPrice = response.getPrice() + " " + response.getCurrencyCode();
//        }
//
//        String strMsg = getString(R.string.youve_just_paid) + " <b>" + strPrice + "</b>";

//        tvMessage.setText(Html.fromHtml(strMsg));

//        tvOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                setResult(ResponseCode.SUCCESSFUL);
//
//            }
//        });

//        dialog.setView(paymentSuccessfulView);
//
//        priceDialog = dialog.create();
//        priceDialog.show();
//        if (!isTablet()) {
//            priceDialog.getWindow().setLayout(convertDptoPixel(296),
//                    LayoutParams.WRAP_CONTENT);
//        }
//        priceDialog.setCanceledOnTouchOutside(false);
//        priceDialog.setOnKeyListener(new Dialog.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface arg0, int keyCode,
//                                 KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    priceDialog.dismiss();
//                }
//                return true;
//            }
//        });
//
//        priceDialog = dialog.create();
//        priceDialog.show();
//        if (!isTablet()) {
//            priceDialog.getWindow().setLayout(convertDptoPixel(296), LayoutParams.WRAP_CONTENT);
//        }
    }

    boolean isTablet() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        float scaleFactor = metrics.density;
        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;

        float smallestWidth = Math.min(widthDp, heightDp);

        return smallestWidth > 600;
    }

    private boolean isShowPaymentConfirmation(MobiamoPayment request) {
        Log.i("NULL CHECK", request == null ? "NULL" : "NOT NULL");
        Log.i("REQUEST", request.getAmount() + " " + request.getCurrency() + " " + request.getKey());
        return (!"".equals(request.getAmount())
                && !" ".equals(request.getAmount())
                && request.getAmount() != null
                && !"".equals(request.getCurrency())
                && !" ".equals(request.getCurrency()) && request.getCurrency() != null);
    }
}
