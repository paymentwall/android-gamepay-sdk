package com.paymentwall.pwunifiedsdk.core;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.paymentwall.pwunifiedsdk.R;
import com.paymentwall.pwunifiedsdk.brick.core.Brick;
import com.paymentwall.pwunifiedsdk.mint.message.MintRequest;
import com.paymentwall.pwunifiedsdk.mint.message.MintResponse;
import com.paymentwall.pwunifiedsdk.mint.ui.views.AutoFitEditText;
import com.paymentwall.pwunifiedsdk.mint.utils.Const;
import com.paymentwall.pwunifiedsdk.mint.utils.PWHttpClient;
import com.paymentwall.pwunifiedsdk.mint.utils.PaymentMethod;
import com.paymentwall.pwunifiedsdk.mint.utils.SntpClient;
import com.paymentwall.pwunifiedsdk.util.Key;
import com.paymentwall.pwunifiedsdk.util.MiscUtils;
import com.paymentwall.pwunifiedsdk.util.PwUtils;
import com.paymentwall.pwunifiedsdk.util.ResponseCode;
import com.paymentwall.pwunifiedsdk.util.SmartLog;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLException;

/**
 * Created by nguyen.anh on 7/20/2016.
 */

public class MintFragment extends BaseFragment implements PWHttpClient.MintCallback {

    public static final String NON_DIGIT = "[^0-9]";
    protected MintRequest request;
    protected MintResponse response;
    private double amount;
    private String currency;
    private static float dpFactor;
    protected static String mintCode;
    protected static boolean isTermChecked = true;
    protected boolean preventLoop = false, isLoading, isMintError, shouldDisplayError;
    private int beforeLength, afterLength, errorToken;
    final Handler mHandler = new Handler();

    private static final String TAG = "Mint";


    private AlertDialog.Builder dialog;
    private AlertDialog successDialog;
    private static Double changeAmount;
    private static String changeCurrencyCode;
    private AutoFitEditText[] edtArray;
    private MintTextWatcher[] watchers;
    private boolean antiloop;
    private static TextSpreader spreader;

    private TextView tvProduct, tvPrice;
    private ImageView ivProduct;
    private AutoFitEditText edtMint1, edtMint2, edtMint3, edtMint4;
    private CheckBox cbAccept;
    private Button btnConfirm;
    private TextView tvAgreement, tvCopyright;

    private int statusCode;

    Linkify.TransformFilter tosFilter = new Linkify.TransformFilter() {
        @Override
        public String transformUrl(Matcher match, String url) {
            return "";
        }
    };


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(self.getPackageName() + Brick.FILTER_BACK_PRESS_FRAGMENT)) {

                if (getMainActivity().isUnsuccessfulShowing) {
                    hideErrorLayout();
                    return;
                } else if (isWaitLayoutShowing() || getMainActivity().isSuccessfulShowing) {
                    return;
                } else {
                    Intent i = new Intent();
                    i.setAction(self.getPackageName() + Brick.FILTER_BACK_PRESS_ACTIVITY);
                    LocalBroadcastManager.getInstance(self).sendBroadcast(i);
                    return;
                }
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PwUtils.logFabricCustom("Visit-MintScreen");

        isTermChecked = true;
        mintCode = null;
        LocalBroadcastManager.getInstance(self).registerReceiver(receiver, new IntentFilter(self.getPackageName() + Brick.FILTER_BACK_PRESS_FRAGMENT));

        getMainActivity().isWaitLayoutShowing = false;
        getMainActivity().isUnsuccessfulShowing = false;
        getMainActivity().isSuccessfulShowing = false;
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(self).unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SmartLog.i("ONSAVEINSTANCESTATE", "afdafs");
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(PwUtils.getLayoutId(self, "frag_mint"), container, false);
        bindView(v);
        return v;
    }

    private void bindView(View v) {
        tvProduct = (TextView) v.findViewById(R.id.tvProduct);
        tvPrice = (TextView) v.findViewById(R.id.tvPrice);
        ivProduct = (ImageView) v.findViewById(R.id.ivProduct);
        edtMint1 = (AutoFitEditText) v.findViewById(R.id.etCode1);
        edtMint2 = (AutoFitEditText) v.findViewById(R.id.etCode2);
        edtMint3 = (AutoFitEditText) v.findViewById(R.id.etCode3);
        edtMint4 = (AutoFitEditText) v.findViewById(R.id.etCode4);
        btnConfirm = (Button) v.findViewById(R.id.btnConfirm);
        cbAccept = (CheckBox) v.findViewById(R.id.cbAccept);
        tvAgreement = (TextView) v.findViewById(R.id.tvAgreement);

        tvCopyright = (TextView) v.findViewById(R.id.tvCopyRight);
        tvCopyright.setText(String.format(getString(R.string.secured_by_pw), new SimpleDateFormat("yyyy").format(new Date())));

        if (mintCode != null) {
            fillEpin(mintCode);
            SmartLog.i("MINT_CODE", mintCode);
        } else {
            SmartLog.i("MINT_CODE", "NULL");
        }
        cbAccept.setChecked(isTermChecked);


        edtArray = new AutoFitEditText[]{edtMint1, edtMint2, edtMint3, edtMint4};
        spreader = new TextSpreader();
        watchers = new MintTextWatcher[edtArray.length];

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateMintCode(true)) {
                    sendPin();
                } else {
                    showErrorLayout(getString(R.string.err_mint_pin_16));
                }
            }
        });


        cbAccept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isTermChecked = isChecked;
            }
        });

        //Set link for terms and privacy policy
        Pattern patternTOS = Pattern.compile("" + getString(R.string.terms) + "");
        Pattern patternPolicy = Pattern.compile("" + getString(R.string.privacy_policy) + "");
        Linkify.addLinks(tvAgreement, patternTOS, Const.MINT.PW_URL.MINT_TERM, null,
                tosFilter);
        Linkify.addLinks(tvAgreement, patternPolicy, Const.MINT.PW_URL.PRIVACY_POLICY,
                null, tosFilter);
        tvAgreement.setLinkTextColor(getResources().getColor(R.color.agreement_text));

        PwUtils.setFontBold(self, new TextView[]{btnConfirm});

        init();

        PwUtils.setCustomAttributes(self, v);
    }

    private void init() {
        acquireMessage();
        setupEditText();
        validateMintCode(false);
    }

    private void acquireMessage() {
        statusCode = 0;
        Bundle extras = getArguments();
        if (extras == null) {
            Intent result = new Intent();
            result.putExtra(Key.SDK_ERROR_MESSAGE, "NULL REQUEST");
            statusCode = ResponseCode.ERROR;
            self.setResult(statusCode, result);
            getMainActivity().backFragment(null);
            return;
        }
        int requestType = extras.getInt(Key.PAYMENT_TYPE,
                PaymentMethod.NULL);
        if (requestType != PaymentMethod.MINT) {
            Intent result = new Intent();
            result.putExtra(Key.SDK_ERROR_MESSAGE, "NULL REQUEST_TYPE");
            statusCode = ResponseCode.ERROR;
            self.setResult(statusCode, result);
            getMainActivity().backFragment(null);
            return;
        }
        try {
            request = (MintRequest) extras
                    .getParcelable(Key.REQUEST_MESSAGE);
        } catch (Exception e) {
            Intent result = new Intent();
            result.putExtra(Key.SDK_ERROR_MESSAGE, "NULL REQUEST OBJECT");
            statusCode = ResponseCode.ERROR;
            self.setResult(statusCode, result);
            getMainActivity().backFragment(null);
        }

        if (request == null) {
            Intent result = new Intent();
            result.putExtra(Key.SDK_ERROR_MESSAGE, "NULL REQUEST OBJECT");
            statusCode = ResponseCode.ERROR;
            self.setResult(statusCode, result);
            getMainActivity().backFragment(null);
            return;
        }

        if (!request.validate()) {
            Intent result = new Intent();
            result.putExtra(Key.SDK_ERROR_MESSAGE, "MORE PARAMETER(S) NEEDED");
            statusCode = ResponseCode.ERROR;
            self.setResult(statusCode, result);
            getMainActivity().backFragment(null);
            return;
        }
        statusCode = 0;
        if (request.getAmount() == null || request.getAmount() < 0 || request.getCurrency() == null) {
            Intent result = new Intent();
            result.putExtra(Key.SDK_ERROR_MESSAGE, "MORE PARAMETER(S) NEEDED");
            statusCode = ResponseCode.ERROR;
            self.setResult(statusCode, result);
            getMainActivity().backFragment(null);
            return;
        } else {
            amount = request.getAmount();
            currency = request.getCurrency();
        }
        String amount = request.getAmount() + " " + currency;
        tvPrice.setText(PwUtils.getCurrencySymbol(request.getCurrency()) + request.getAmount());

        ivProduct.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    ivProduct.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    ivProduct.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                if (request.getItemUrl() != null) {
                    Glide.with(self).load(request.getItemUrl()).into(ivProduct);
                } else if (request.getItemFile() != null) {
                    Glide.with(self).load(request.getItemFile()).into(ivProduct);
                } else if (request.getItemResID() != 0) {
                    Glide.with(self).load(request.getItemResID()).into(ivProduct);
                } else if (request.getItemContentProvider() != null) {
                    String realPath = MiscUtils.getRealPathFromURI(Uri.parse(request.getItemContentProvider()), self);
                    SmartLog.i("REAL_PATH", realPath);
                    Glide.with(self).load(new File(realPath)).into(ivProduct);
                } else {
                    ((View) (ivProduct.getParent())).setVisibility(View.GONE);

                }
            }
        });

        tvProduct.setText(request.getName());

//        if (request.getePin() == null || request.getePin().size() == 0) {
//            fillEpin("");
//        } else {
//            if (request.getePin().size() == 1) {
//                request.getePin().set(0, request.getePin().get(0).replaceAll(NON_DIGIT, ""));
//                if (request.getePin().get(0).matches(Const.MINT.REGEX.PIN)) {
//                    preventLoop = true;
//                    fillEpin(request.getePin().get(0));
//                    preventLoop = false;
//                } else {
//                    try {
//                        request.setePin(new ArrayList<String>());
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
    }

    public void setupEditText() {
        for (int i = 0; i < edtArray.length; i++) {
            final int index = i;
            watchers[i] = new MintTextWatcher(index);
            edtArray[i].addTextChangedListener(watchers[i]);

            if (index > 0) {
                edtArray[i].setOnKeyListener(new View.OnKeyListener() {

                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_DEL && edtArray[index].getSelectionStart() == 0) {
                            // Case 2. Cursor jump backward (left box is full, current box is empty)
                            // Case 3. Cursor jump backward (left box is not full)
                            SmartLog.d(TAG, "OnKeyListener keyCode=" + keyCode + " index=" + index + " jumpCursorToLast");
                            jumpCursorToLast(index - 1);
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
            }
        }
    }

    protected void fillEpin() {
        this.preventLoop = true;
        mintCode = edtMint1.getText().toString()
                + edtMint2.getText().toString() + edtMint3.getText().toString()
                + edtMint4.getText().toString();
        mintCodeSplit();
        this.preventLoop = false;
    }

    protected void fillEpin(String mintPin) {
        this.preventLoop = true;
        mintCode = mintPin;
        mintCodeSplit();
        this.preventLoop = false;
    }

    public void mintCodeSplit() {
        antiloop = true;
        if (mintCode.length() < 4) {
            edtMint2.setText("");
            edtMint3.setText("");
            edtMint4.setText("");
            edtMint1.setText(mintCode);
            edtMint1.requestFocus();
            edtMint1.setSelection(mintCode.length());
        } else if (mintCode.length() >= 4 && mintCode.length() < 8) {
            edtMint1.setText(mintCode.substring(0, 4));
            edtMint3.setText("");
            edtMint4.setText("");
            edtMint2.setText(mintCode.substring(4, mintCode.length()));
            if (afterLength == 4 && beforeLength > afterLength) {
                edtMint1.requestFocus();
                edtMint1.setSelection(4);
            } else if (afterLength == 4 && beforeLength < afterLength) {
                edtMint2.requestFocus();
                edtMint2.setSelection(mintCode.length() - 4);
            }
        } else if (mintCode.length() >= 8 && mintCode.length() < 12) {
            edtMint1.setText(mintCode.substring(0, 4));
            edtMint2.setText(mintCode.substring(4, 8));
            edtMint4.setText("");
            edtMint3.setText(mintCode.substring(8, mintCode.length()));
            if (afterLength == 8 && beforeLength > afterLength) {
                edtMint2.requestFocus();
                edtMint2.setSelection(4);
            } else if (afterLength == 8 && beforeLength < afterLength) {
                edtMint3.requestFocus();
                edtMint3.setSelection(mintCode.length() - 8);
            }
        } else if (mintCode.length() >= 12 && mintCode.length() < 16) {
            edtMint1.setText(mintCode.substring(0, 4));
            edtMint2.setText(mintCode.substring(4, 8));
            edtMint3.setText(mintCode.substring(8, 12));
            edtMint4.setText(mintCode.substring(12, mintCode.length()));
            if (afterLength == 12 && beforeLength > afterLength) {
                edtMint3.requestFocus();
                edtMint3.setSelection(4);
            } else if (afterLength == 12 && beforeLength > afterLength) {
                edtMint4.requestFocus();
                edtMint4.setSelection(mintCode.length() - 12);
            }
        } else if (mintCode.length() >= 16) {
            mintCode = mintCode.substring(0, 16);
            edtMint1.setText(mintCode.substring(0, 4));
            edtMint2.setText(mintCode.substring(4, 8));
            edtMint3.setText(mintCode.substring(8, 12));
            edtMint4.setText(mintCode.substring(12, 16));
            edtMint4.requestFocus();
            edtMint4.setSelection(4);
        }
        antiloop = false;
    }

    @Override
    public void onMintError(int statusCode, String body, Throwable error) {

        isMintError = true;

        if (shouldDisplayError) {
            if (statusCode > 0) {
                if (body != null) {
                    try {
                        MintResponse rp = new MintResponse(body);
                        getMainActivity().paymentError = ("Failed. " + rp.getErrorMessage());
                        statusCode = ResponseCode.FAILED;
                        if (rp != null)
                            response = rp;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        getMainActivity().paymentError = (getString(R.string.unknown_error) + " ("
                                + Integer
                                .toHexString(Const.MINT.INTERNAL_ERROR_CODE.ERROR_006)
                                + ")");
                    }
                } else {
                    getMainActivity().paymentError = (getString(R.string.status) + "=" + statusCode
                            + " | " + getString(R.string.error) + "=" + error.getClass().getName() + "|"
                            + error.toString());
                }
                showErrorLayout(getMainActivity().paymentError);
            } else {
                response = null;
                if (error instanceof SSLException) {
                    statusCode = ResponseCode.ERROR;
                    getNtpTime();
                } else if (error instanceof SocketTimeoutException) {
                    statusCode = ResponseCode.ERROR;
                    getMainActivity().paymentError = (getString(R.string.service_unavailable) + " ("
                            + Integer
                            .toHexString(Const.MINT.INTERNAL_ERROR_CODE.ERROR_013)
                            + ")");
                } else if (error instanceof IOException) {
                    statusCode = ResponseCode.ERROR;
                    getMainActivity().paymentError = (getString(R.string.check_internet_connection) + " ("
                            + Integer
                            .toHexString(Const.MINT.INTERNAL_ERROR_CODE.ERROR_014)
                            + ")");
                } else {

                    getMainActivity().paymentError = (getString(R.string.unknown_error) + " ("
                            + Integer
                            .toHexString(Const.MINT.INTERNAL_ERROR_CODE.ERROR_019)
                            + ")");
                }
            }
        }
    }

    @Override
    public void onMintSuccess(int statusCode, String body) {
        isMintError = false;

        SmartLog.i("ON_MINT_SUCCESS", "SUCCESS");

        if (body != null) {
            try {
                MintResponse rp = new MintResponse(body);
                if (rp.getSuccess() == MintResponse.SUCCESSFUL) {
                    statusCode = ResponseCode.SUCCESSFUL;
                    hideErrorLayout();
//						paymentConfirmed(rp.getChangeAmount(), rp.getChangeCurrency()).show();
//						displaySuccessfulPayment(rp.getChangeAmount(), rp.getChangeCurrency());
                    displayPaymentSucceeded();
                    changeAmount = rp.getChangeAmount();
                    changeCurrencyCode = rp.getChangeCurrency();
                    response = null;
                    // Display dialog which confirms the payment
                } else {
                    statusCode = ResponseCode.FAILED;
                    if (rp != null)
                        response = rp;
                    getMainActivity().paymentError = (getString(R.string.service_unavailable) + " ("
                            + Integer
                            .toHexString(Const.MINT.INTERNAL_ERROR_CODE.ERROR_015)
                            + ")");
                    isMintError = true;
                }
            } catch (JSONException e) {
                getMainActivity().paymentError = (getString(R.string.service_unavailable) + " ("
                        + Integer
                        .toHexString(Const.MINT.INTERNAL_ERROR_CODE.ERROR_005)
                        + ")");
                isMintError = true;
            }
        } else {
            getMainActivity().paymentError = (getString(R.string.unknown_error) + " ("
                    + Integer.toHexString(Const.MINT.INTERNAL_ERROR_CODE.ERROR_003)
                    + ")");
            isMintError = true;
        }
    }

    @Override
    public void onMintStart() {
        shouldDisplayError = true;
    }

    @Override
    public void onMintStop() {
        try {
//            hideWaitLayout();
        } catch (Exception e) {

        }
    }

    private boolean validateMintCode(boolean confirmed) {
        mintCode = edtMint1.getText().toString()
                + edtMint2.getText().toString() + edtMint3.getText().toString()
                + edtMint4.getText().toString();

        boolean validated = mintCode.matches(Const.MINT.REGEX.PIN);
        if (validated) btnConfirm.setEnabled(true);
        else btnConfirm.setEnabled(false);
        return validated;
    }

    protected void sendPin() {
        // Alert user
        if (!cbAccept.isChecked()) {
            showErrorLayout(getString(R.string.term_alert));
            return;
        }
        List<String> ePins = new ArrayList<String>();
        ePins.add(mintCode);
        try {
            request.setePin(ePins);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//            hideError(errorToken);
        try {
                /*mintResponseHandler = new MintResponseHandler(
                        MintActivity.this);
                PWHttpClient.mintRedeem(getApplicationContext(),
                        request, mintResponseHandler);*/
            PWHttpClient.mintRedeem(self, request, this);
            isLoading = true;
            showWaitLayout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void getNtpTime() {
        Thread t = new Thread() {
            public void run() {
                SntpClient sntpClient = new SntpClient();
                boolean getNtpTime = sntpClient.requestTime(Const.NTP_SERVER,
                        Const.NTP_TIMEOUT);
                long currentTime = 0;
                if (getNtpTime) {
                    currentTime = sntpClient.getNtpTime();
                }
                GetTime getTime = new GetTime(getNtpTime, currentTime);
                mHandler.post(getTime);
            }
        };
        t.start();
    }

    public class GetTime implements Runnable {
        boolean successful;
        long timeDifference;

        public GetTime(boolean successful, long currentUtcTime) {
            this.successful = successful;
            if (successful) {
                this.timeDifference = System.currentTimeMillis()
                        - currentUtcTime;
            } else {
                this.timeDifference = 0;
            }
        }

        public void run() {
            if (successful
                    && Math.abs(timeDifference) > 1 * 24 * 60 * 60 * 1000) {
                // Compare current time with ntp server time
                showErrorLayout(getString(R.string.err_system_time_incorrect));
            } else {
                showErrorLayout(getString(R.string.service_unavailable));
            }
        }
    }

    public class MintTextChange {
        public int index;
        public String beforeText;
        public String afterText;
        public int startPosition;
        public int beforeCount;
        public int afterCount;
        public int cursorPositionStart;
        public int cursorPositionEnd;

        public MintTextChange(int index) {
            super();
            this.index = index;
        }

        @Override
        public String toString() {
            return "MintTextChange [index=" + index + ", beforeText="
                    + beforeText + ", afterText=" + afterText
                    + ", startPosition=" + startPosition + ", beforeCount="
                    + beforeCount + ", afterCount=" + afterCount
                    + ", cursorPositionStart=" + cursorPositionStart
                    + ", cursorPositionEnd=" + cursorPositionEnd + "]";
        }
    }

    public class MintTextWatcher implements TextWatcher {
        // index of textwatcher
        int index;
        MintTextChange textChange;

        public MintTextWatcher(int index) {
            super();
            this.index = index;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
            if (!antiloop) {
                textChange = new MintTextChange(index);
                textChange.beforeText = s.toString();
                textChange.startPosition = start;
                textChange.beforeCount = count;
                textChange.afterCount = after;
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            hideErrorLayout();
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if (textChange != null && !antiloop) {
                textChange.afterText = s.toString();
                textChange.cursorPositionStart = edtArray[index].getSelectionStart();
                textChange.cursorPositionEnd = edtArray[index].getSelectionEnd();
                spreader.onTextChanged(textChange);
                mintCode = edtMint1.getText().toString()
                        + edtMint2.getText().toString() + edtMint3.getText().toString()
                        + edtMint4.getText().toString();
                SmartLog.i("MINT_CODE_AFTER_CHANGE", mintCode);
                validateMintCode(false);
            }
        }

        public MintTextChange getTextChange() {
            return textChange;
        }
    }

    public class TextSpreader {
        public TextSpreader() {

        }

        public void onTextChanged(MintTextChange change) {
            antiloop = true;
            SmartLog.i("ON_TEXT_CHANGED", change.beforeText + "---" + change.afterText + "---" + change.index);
            if (change.beforeText.length() < change.afterText.length() && change.afterText.length() > 4 && change.index == 3) {
                edtArray[3].setText(change.afterText.substring(0, 4));
                if (change.cursorPositionEnd >= 4) {
                    edtArray[3].setSelection(4);
                } else {
                    edtArray[3].setSelection(change.cursorPositionEnd);
                }
            } else if (change.beforeText.length() < change.afterText.length() && change.afterText.length() > 4 && change.cursorPositionEnd == change.afterText.length()) {
                // Case 1. Cursor jump forward (right box is empty, input to last position)
                // Case 4. Cursor jump forward (right box is NOT empty, input to last position)
//    			fillNextEmptyEditText(change.index, change.afterText.substring(4, change.afterText.length()));
                fillNextEditText(change.index, change.afterText.substring(4, change.afterText.length()), change.cursorPositionEnd, change);

            } else if (change.beforeText.length() > 0 && change.afterText.length() == 0 && change.index > 0) {
                // Case 2. Cursor jump backward (left box is full, current box is empty)
                // Case 3. Cursor jump backward (left box is not full)
                jumpCursorToLast(change.index - 1);
            } else if (change.afterText.length() > 4 && change.index < 3 && change.cursorPositionEnd < change.afterText.length()) {
//    			fillNextEmptyEditText(change.index, change.afterText.substring(4, change.afterText.length()), change.afterText.substring(change.cursorPositionEnd));
                fillNextEditText(change.index, change.afterText.substring(4, change.afterText.length()), change.afterText.substring(change.cursorPositionEnd), change);
            }
            if (change.beforeText.length() < change.afterText.length() && change.afterText.length() >= 4 && change.afterText.length() % 4 == 0 && change.index < 3) {
                SmartLog.i("MOVE_CURSOR_TO_FIRST", "MOVING");
                moveCursorTo(change.index + 1, 0);
            }
            antiloop = false;
        }
    }

    public void jumpCursorToLast(int edtIndex) {
        //(disable log) SmartLog.d(TAG, "jumpCursorToLast edtIndex="+edtIndex);
        if (edtIndex < 4) {
            for (int i = 0; i < edtArray.length; i++) {
                if (i == edtIndex) {
                    edtArray[i].requestFocus();
                    edtArray[i].setSelection(edtArray[i].getText().length());
                } else {
                }
            }
        } else {
            edtArray[3].requestFocus();
            edtArray[3].setSelection(edtArray[3].length());
        }
    }

    public void jumpCursorToFirst(int edtIndex) {
        //(disable log) SmartLog.d(TAG, "jumpCursorToFirst edtIndex="+edtIndex);
        if (edtIndex < 4) {
            for (int i = 0; i < edtArray.length; i++) {
                if (i == edtIndex) {
                    edtArray[i].requestFocus();
                    edtArray[i].setSelection(0);
                } else {

                }
            }
        } else {
            edtArray[3].requestFocus();
            edtArray[3].setSelection(edtArray[3].length());
        }
    }

    public void fillNextEditText(int currentIndex, String input, int cursorPosition, MintTextChange change) {

        int finalCursorBox = 0;
        int finalCursorPosition = 0;
        //(disable log) SmartLog.d(TAG, "cursor calc");
        if (change.afterText.length() <= 4) {
            //(disable log) SmartLog.d(TAG, "cursor calc (001)");
            finalCursorBox = change.index;
            finalCursorPosition = change.cursorPositionEnd;
        } else if (change.afterText.length() > (3 - change.index + 1) * 4) {
            //(disable log) SmartLog.d(TAG, "cursor calc (002)");
            finalCursorBox = 3;
            finalCursorPosition = 4;
        } else {
            //(disable log) SmartLog.d(TAG, "cursor calc (003) change.cursorPositionEnd="+change.cursorPositionEnd);
            int nextAllTextLength = nextAllText(change.index);
            // how many characters before cursor endpoint
            int beforeCursorCount = change.cursorPositionEnd;
            if (beforeCursorCount % 4 == 0) {
                finalCursorBox = change.index + (int) (beforeCursorCount / 4) - 1;
            } else {
                finalCursorBox = change.index + (int) (beforeCursorCount / 4);
            }
            if (finalCursorBox > 3) {
                //(disable log) SmartLog.d(TAG, "cursor calc (013)");
                finalCursorBox = 3;
                finalCursorPosition = 4;
            } else {

                finalCursorPosition = beforeCursorCount % 4;
                if (finalCursorPosition == 0) finalCursorPosition = 4;
                //(disable log) SmartLog.d(TAG, "cursor calc (023) finalCursorPosition="+finalCursorPosition+" finalCursorBox="+finalCursorBox);
            }
        }

        //(disable log) SmartLog.d(TAG, "fillNextEditText currentIndex="+currentIndex+" input="+input+" position="+cursorPosition);
        edtArray[currentIndex].setText(edtArray[currentIndex].getText().toString().substring(0, 4));
        if (currentIndex == 3) {
            jumpCursorToLast(3);
            return;
        }
        int backCount = 0;
        currentIndex = currentIndex + 1;
        while (currentIndex < 4 && input.length() > 0) {
            //(disable log) SmartLog.d(TAG, "fillNextEditText (a) currentIndex="+currentIndex+" input="+input);
            if (isEditTextEmpty(currentIndex)) {
                if (currentIndex == 3) {
                    if (input.length() > 4) {
                        input = input.substring(0, 4);
                    }
                    edtArray[currentIndex].setText(input);
                    break;
                } else if (input.length() < 4) {
                    //(disable log) SmartLog.d(TAG, "fillNextEditText (b)");
                    edtArray[currentIndex].setText(input);
                    break;
                } else if (input.length() == 4) {
                    //(disable log) SmartLog.d(TAG, "fillNextEditText (c)");
                    edtArray[currentIndex].setText(input);
                    break;
                } else {
                    //(disable log) SmartLog.d(TAG, "fillNextEditText (d)");
                    edtArray[currentIndex].setText(input.substring(0, 4));
                    input = input.substring(4);
                    currentIndex = currentIndex + 1;
                }
            } else {
                int tempCount = input.length();
                //(disable log) SmartLog.d(TAG, "fillNextEditText (e1) tempCount="+tempCount);
                backCount = backCount + edtArray[currentIndex].getText().toString().length();
                input = input.concat(edtArray[currentIndex].getText().toString());
                //(disable log) SmartLog.d(TAG, "fillNextEditText (e2) currentIndex="+currentIndex);
                //(disable log) SmartLog.d(TAG, "fillNextEditText (e) backCount="+backCount+" input="+input);
                if (currentIndex == 3) {
                    //(disable log) SmartLog.d(TAG, "fillNextEditText (i)");
                    if (input.length() > 4) {
                        backCount = backCount - (input.length() - 4);
                        input = input.substring(0, 4);
                    }
                    edtArray[currentIndex].setText(input);
                    break;
                } else if (input.length() < 4 && currentIndex < 3) {
                    //(disable log) SmartLog.d(TAG, "fillNextEditText (f)");
                    edtArray[currentIndex].setText(input);
                    break;
                } else if (input.length() == 4) {
                    //(disable log) SmartLog.d(TAG, "fillNextEditText (g)");
                    edtArray[currentIndex].setText(input);
                    break;
                } else {
                    //(disable log) SmartLog.d(TAG, "fillNextEditText (h)");
                    edtArray[currentIndex].setText(input.substring(0, 4));
                    input = input.substring(4);
                    jumpCursorToFirst(currentIndex + 1);
                    currentIndex = currentIndex + 1;
                }
            }
        }
        moveCursorTo(finalCursorBox, finalCursorPosition);
    }

    public void fillNextEditText(int curIndex, String input, String followingText, MintTextChange change) {
        int currentIndex = curIndex;
        // Calc cursor position
        // Info: cursor position, before text, after text, index, next edittext(s)
        int finalCursorBox = 0;
        int finalCursorPosition = 0;
        //(disable log) SmartLog.d(TAG, "cursor calc");
        if (change.afterText.length() <= 4) {
            //(disable log) SmartLog.d(TAG, "cursor calc (001)");
            finalCursorBox = change.index;
            finalCursorPosition = change.cursorPositionEnd;
        } else if (change.afterText.length() > (3 - change.index + 1) * 4) {
            //(disable log) SmartLog.d(TAG, "cursor calc (002)");
            finalCursorBox = 3;
            finalCursorPosition = 4;
        } else {
            //(disable log) SmartLog.d(TAG, "cursor calc (003) change.cursorPositionEnd="+change.cursorPositionEnd);
            int nextAllTextLength = nextAllText(change.index);
            // how many characters before cursor endpoint
            int beforeCursorCount = change.cursorPositionEnd;
            if (beforeCursorCount % 4 == 0) {
                finalCursorBox = change.index + (int) (beforeCursorCount / 4) - 1;
            } else {
                finalCursorBox = change.index + (int) (beforeCursorCount / 4);
            }
            if (finalCursorBox > 3) {
                //(disable log) SmartLog.d(TAG, "cursor calc (013)");
                finalCursorBox = 3;
                finalCursorPosition = 4;
            } else {

                finalCursorPosition = beforeCursorCount % 4;
                if (finalCursorPosition == 0) finalCursorPosition = 4;
                //(disable log) SmartLog.d(TAG, "cursor calc (023) finalCursorPosition="+finalCursorPosition+" finalCursorBox="+finalCursorBox);
            }
        }
        //(disable log) SmartLog.d(TAG, "fillNextEditText (001) currentIndex="+currentIndex+" input"+input+ " followingText"+followingText);
        edtArray[currentIndex].setText(edtArray[currentIndex].getText().toString().substring(0, 4));
        int backCount = followingText.length();

        currentIndex = currentIndex + 1;
        while (currentIndex < 4 && input.length() > 0) {
            //(disable log) SmartLog.d(TAG, "fillNextEditText (002) currentIndex="+currentIndex+" input="+input);
            if (isEditTextEmpty(currentIndex)) {
                //(disable log) SmartLog.d(TAG, "fillNextEditText (003)");
                if (currentIndex == 3) {
                    //(disable log) SmartLog.d(TAG, "fillNextEditText (004)");
                    if (input.length() > 4) {
                        input = input.substring(0, 4);
                    }
                    edtArray[currentIndex].setText(input);
                    break;
                } else if (input.length() < 4) {
                    //(disable log) SmartLog.d(TAG, "fillNextEditText (005)");
                    edtArray[currentIndex].setText(input);
                    break;
                } else if (input.length() == 4) {
                    //(disable log) SmartLog.d(TAG, "fillNextEditText (006)");
                    edtArray[currentIndex].setText(input);
                    break;
                } else {
                    //(disable log) SmartLog.d(TAG, "fillNextEditText (007)");
                    edtArray[currentIndex].setText(input.substring(0, 4));
                    input = input.substring(4);
                    currentIndex = currentIndex + 1;
                }
            } else {
                //(disable log) SmartLog.d(TAG, "fillNextEditText (008)");
                backCount = backCount + edtArray[currentIndex].getText().toString().length();
                input = input.concat(edtArray[currentIndex].getText().toString());
                if (currentIndex == 3) {
                    //(disable log) SmartLog.d(TAG, "fillNextEditText (009)");
                    if (input.length() > 4) {
                        input = input.substring(0, 4);
                    }
                    edtArray[currentIndex].setText(input);
                    break;
                } else if (input.length() < 4) {
                    //(disable log) SmartLog.d(TAG, "fillNextEditText (010)");
                    edtArray[currentIndex].setText(input);
                    break;
                } else if (input.length() == 4) {
                    //(disable log) SmartLog.d(TAG, "fillNextEditText (011)");
                    edtArray[currentIndex].setText(input);
                    break;
                } else {
                    //(disable log) SmartLog.d(TAG, "fillNextEditText (012)");
                    edtArray[currentIndex].setText(input.substring(0, 4));
                    input = input.substring(4);
                    currentIndex = currentIndex + 1;
                }
            }
        }
        moveCursorTo(finalCursorBox, finalCursorPosition);
    }

    public void moveCursorTo(int index, int cursorPosition) {
        if (cursorPosition < 0) cursorPosition = 0;
        if (cursorPosition > edtArray[index].length()) cursorPosition = edtArray[index].length();
        edtArray[index].requestFocus();
        edtArray[index].setSelection(cursorPosition);
    }

    public boolean isEditTextEmpty(int index) {
        if (index >= 4) return false;
        else {
            return (edtArray[index].length() == 0);
        }
    }

    public int nextAllText(int index) {
        int allTextLength = 0;
        for (int i = index + 1; i < edtArray.length; i++) {
            allTextLength = allTextLength + edtArray[i].length();
        }
        return allTextLength;
    }

    private int getOrientation() {
        return getResources().getConfiguration().orientation;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        populateViewForOrientation(inflater, (ViewGroup) getView());
    }

    private void populateViewForOrientation(LayoutInflater inflater, ViewGroup viewGroup) {
        viewGroup.removeAllViewsInLayout();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            View v = inflater.inflate(PwUtils.getLayoutId(self, "frag_mint"), viewGroup);
            bindView(v);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            View v = inflater.inflate(PwUtils.getLayoutId(self, "frag_mint"), viewGroup);
            bindView(v);
        }
        // Find your buttons in subview, set up onclicks, set up callbacks to your parent fragment or activity here.
    }

}
