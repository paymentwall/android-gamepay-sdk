package com.paymentwall.pwunifiedsdk.core;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.paymentwall.pwunifiedsdk.R;
import com.paymentwall.pwunifiedsdk.brick.core.Brick;
import com.paymentwall.pwunifiedsdk.brick.core.BrickCard;
import com.paymentwall.pwunifiedsdk.brick.core.BrickError;
import com.paymentwall.pwunifiedsdk.brick.core.BrickToken;
import com.paymentwall.pwunifiedsdk.brick.message.BrickRequest;
import com.paymentwall.pwunifiedsdk.brick.ui.views.MaskedEditText;
import com.paymentwall.pwunifiedsdk.brick.utils.Const;
import com.paymentwall.pwunifiedsdk.brick.utils.PaymentMethod;
import com.paymentwall.pwunifiedsdk.ui.CardEditText;
import com.paymentwall.pwunifiedsdk.ui.LoadingButton;
import com.paymentwall.pwunifiedsdk.ui.WaveHelper;
import com.paymentwall.pwunifiedsdk.util.Key;
import com.paymentwall.pwunifiedsdk.util.NoUnderlineLinkSpan;
import com.paymentwall.pwunifiedsdk.util.PwUtils;
import com.paymentwall.pwunifiedsdk.util.ResponseCode;
import com.paymentwall.pwunifiedsdk.util.SharedPreferenceManager;
import com.paymentwall.pwunifiedsdk.util.SmartLog;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by nguyen.anh on 7/19/2016.
 */

public class BrickNewFragment extends BaseFragment implements Brick.Callback {
    private ConstraintLayout layoutInputCard;
    private MaskedEditText etCardNumber;
    private EditText etCvv, etExpirationDate, etEnterCvvStoredCard;
    private EditText etEmail, etNameOnCard;
    private static String mCardNumber, mCvv, mExpDate, mEmail, mNameOnCard;
    private View expandedCvvView = null;
    //Layout stored cards
    private LinearLayout llCardList, layoutStoredCard, rootBrickLayout, contentLayout;
    private CardEditText clickedCard;
    private String permanentToken;
    private String fingerprint;
    private WaveHelper helper;
    private LoadingButton btnConfirm, btnPayStoredCard;
    private ImageView btnBack;
    private int statusCode;
    private BrickRequest request;
    private int timeout;
    private boolean isBrickError;
    private TextView tvAgreement, tvAgreementCardSelect;
    private TextView tvStoreCard;
    private TextView tvCardPayTitle;
    private CheckBox cbStoreCard;
    private final Handler handler = new Handler();
    private boolean isAddingNewCard = false;
    public static final int RC_SCAN_CARD = 2505;
    private static BrickNewFragment instance;

    public static BrickNewFragment getInstance() {
        if (instance == null)
            instance = new BrickNewFragment();
        return instance;
    }

    public static void clearInstance() {
        instance = null;
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(self.getPackageName() + Brick.BROADCAST_FILTER_SDK)) {

                if (intent.getExtras().containsKey(Brick.KEY_MERCHANT_SUCCESS)) {
                    int success = intent.getIntExtra(Brick.KEY_MERCHANT_SUCCESS, -1);
                    SmartLog.i("BRICK_RECEIVER", success + "");
                    handler.removeCallbacks(checkTimeoutTask);
                    if (success == 1) {
                        String permanentToken = intent.getStringExtra(Brick.KEY_PERMANENT_TOKEN);
                        if (!permanentToken.equalsIgnoreCase("")) {
                            onChargeSuccess(permanentToken);
                        } else {
                            statusCode = ResponseCode.SUCCESSFUL;
                            sendResultBack(statusCode);
                        }
                    } else {
                        String error = intent.getStringExtra(Brick.KEY_PERMANENT_TOKEN);
                        isBrickError = true;
                        if (error != null && !error.equals("")) {
                            getMainActivity();
                            PaymentSelectionActivity.paymentError = error;
                        } else {
                            getMainActivity();
                            PaymentSelectionActivity.paymentError = getString(R.string.payment_error);
                        }
                        getMainActivity();
                        showErrorLayout(PaymentSelectionActivity.paymentError);
                    }
                } else if (intent.getExtras().containsKey(Brick.KEY_3DS_FORM)) {
                    String form3ds = intent.getStringExtra(Brick.KEY_3DS_FORM);
                    if (isWaitLayoutShowing()) {
                        hideWaitLayout();
                    }
                    getMainActivity().show3dsWebView(form3ds);
                }
            } else if (intent.getAction().equalsIgnoreCase(self.getPackageName() + Brick.FILTER_BACK_PRESS_FRAGMENT)) {
                if (getMainActivity().isUnsuccessfulShowing) {
                    hideErrorLayout();
                } else if (getMainActivity().isSuccessfulShowing || isWaitLayoutShowing()) {
                } else {
                    Intent i = new Intent();
                    i.setAction(self.getPackageName() + Brick.FILTER_BACK_PRESS_ACTIVITY);
                    LocalBroadcastManager.getInstance(self).sendBroadcast(i);
                }
            } else if (intent.getAction().equalsIgnoreCase(self.getPackageName() + Brick.BROADCAST_ERROR_SDK)) {
                String errMessage = intent.getStringExtra(Brick.KEY_BRICK_ERROR);
                SmartLog.e("BRICK-ERROR", errMessage);
                hideWaitLayout();
                showErrorLayout(errMessage);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PwUtils.logFabricCustom("Visit-BrickScreen");

        SmartLog.i("BRICK-packagename", self.getPackageName());

        IntentFilter filter = new IntentFilter();
        filter.addAction(self.getPackageName() + Brick.FILTER_BACK_PRESS_FRAGMENT);
        filter.addAction(self.getPackageName() + Brick.BROADCAST_FILTER_SDK);
        filter.addAction(self.getPackageName() + Brick.BROADCAST_ERROR_SDK);
        LocalBroadcastManager.getInstance(self).registerReceiver(receiver, filter);

        Brick.getInstance().setContext(self);

        mCardNumber = null;
        mCvv = null;
        mExpDate = null;
        mEmail = null;
        mNameOnCard = null;

        getMainActivity().isWaitLayoutShowing = false;
        getMainActivity().isUnsuccessfulShowing = false;
        getMainActivity().isSuccessfulShowing = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        Brick.getInstance().callBrickInit(request.getAppKey(), new Brick.FingerprintCallback() {
            @Override
            public void onSuccess(String f) {
                fingerprint = f;
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(self).unregisterReceiver(receiver);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(PwUtils.getLayoutId(self, "frag_brick_new"), container, false);
        bindView(v);
        return v;
    }

    private void initDataView() {
        TypedValue merchantTermsUrl = new TypedValue();
        TypedValue merchantPrivacyUrl = new TypedValue();
        Resources.Theme theme = getMainActivity().getTheme();
        theme.resolveAttribute(R.attr.merchantTermsUrl, merchantTermsUrl, true);
        theme.resolveAttribute(R.attr.merchantPrivacyUrl, merchantPrivacyUrl, true);

        String rawAgreement = getString(
                R.string.payment_agreement_text,
                request.getMerchantName(),
                merchantTermsUrl.string.toString(),
                merchantPrivacyUrl.string.toString()
        );
        Spanned htmlAgreement = Html.fromHtml(rawAgreement, Html.FROM_HTML_MODE_COMPACT);
        Spannable spannable = new SpannableString(htmlAgreement);

        URLSpan[] spans = spannable.getSpans(0, spannable.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = spannable.getSpanStart(span);
            int end = spannable.getSpanEnd(span);
            spannable.removeSpan(span);
            spannable.setSpan(new NoUnderlineLinkSpan(span.getURL()), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        tvAgreement.setText(spannable);
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());

        tvAgreementCardSelect.setText(spannable);
        tvAgreementCardSelect.setMovementMethod(LinkMovementMethod.getInstance());

        String amountCurrency = PwUtils.getCurrencySymbol(request.getCurrency()) + request.getAmount();

        btnConfirm.setButtonText(getString(R.string.pay_amount, amountCurrency));

        btnPayStoredCard.setButtonText(getString(R.string.pay_amount, amountCurrency));

        tvStoreCard.setText(getString(R.string.store_card_confirmation, request.getMerchantName()));

        checkStoredCard();
        handleBackPressed();
    }

    private void handleBackPressed() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (isAddingNewCard) {
                    isAddingNewCard = false;
                    layoutStoredCard.setVisibility(View.VISIBLE);
                    layoutInputCard.setVisibility(View.GONE);
                } else {
                    setEnabled(false);
                    getMainActivity().onBackPressed();
                }
            }
        };

        getMainActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                callback
        );
    }

    private void checkStoredCard() {
        String cards = SharedPreferenceManager.getInstance(self).getStringValue(SharedPreferenceManager.STORED_CARDS);
        SmartLog.d("TAGGGG", "CardList: " + cards);
        if (cards.equalsIgnoreCase("")) {
            tvCardPayTitle.setText(R.string.input_card_title);
            layoutInputCard.setVisibility(View.VISIBLE);
            layoutStoredCard.setVisibility(View.GONE);
        } else {
            tvCardPayTitle.setText(R.string.select_card_title);
            layoutInputCard.setVisibility(View.GONE);
            layoutStoredCard.setVisibility(View.VISIBLE);
            btnPayStoredCard.setButtonEnabled(false);
            try {
                LayoutInflater inflater = (LayoutInflater) self.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                JSONObject obj = new JSONObject(cards);
                Iterator<String> iter = obj.keys();
                llCardList.removeAllViews();
                while (iter.hasNext()) {
                    String key = iter.next();
                    String value = obj.getString(key);
                    String[] keys = key.split("###");
                    String[] values = value.split("###");

                    String cardNumber = keys[0];
                    String cardType = keys[1];
                    String permanentToken = values[0];
                    String email = values[1];

                    View view = inflater.inflate(PwUtils.getLayoutId(self, "stored_card_layout"), null);
                    final CardEditText etCard = view.findViewById(R.id.etStoredCard);

                    etCard.setCardNumber(cardNumber);
                    etCard.setCardType(cardType);
                    etCard.setPermanentToken(permanentToken);
                    etCard.setEmail(email);
                    etCard.setText("•••• •••• •••• " + cardNumber);
                    etCard.setOnClickListener(onClickStoredCard);
                    etCard.setLongClickable(false);
                    etCard.setTextIsSelectable(false);
                    etCard.setOnLongClickListener(v -> true);

                    etCard.setDrawableClickListener(target -> {
                        if (target == CardEditText.DrawableClickListener.DrawablePosition.RIGHT) {
                            if (!isWaitLayoutShowing()) showDeleteCardConfirmation(etCard);
                        }
                    });

                    int resDrawableStart = switch (Const.getCardTypeFromName(cardType)) {
                        case MASTERCARD -> R.drawable.ic_master_card;
                        case VISA -> R.drawable.ic_visa_stored;
                        case AMEX -> R.drawable.ic_amex;
                        case JCB -> R.drawable.ic_jcb;
                        default -> R.drawable.ic_master_card;
                    };

                    Drawable drawableStart = ResourcesCompat.getDrawable(getResources(), resDrawableStart, null);
                    Drawable drawableEnd = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_remove_card, null);

                    etCard.setCompoundDrawablesWithIntrinsicBounds(drawableStart, null, drawableEnd, null);

                    llCardList.addView(view);
                }
            } catch (Exception e) {
                SmartLog.e(e.getMessage());
            }
        }
    }

    private final View.OnClickListener onClickStoredCard = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickedCard = (CardEditText) v;
            mEmail = clickedCard.getEmail();
            permanentToken = clickedCard.getPermanentToken();
            ViewGroup parent = (ViewGroup) v.getParent();
            ViewGroup layoutCvv = parent.findViewById(R.id.layoutCvv);

            if (expandedCvvView != null && expandedCvvView != layoutCvv) {
                expandedCvvView.setVisibility(View.GONE);
            }

            if (layoutCvv.getVisibility() == View.VISIBLE) {
                layoutCvv.setVisibility(View.GONE);
                expandedCvvView = null;
            } else {
                layoutCvv.setVisibility(View.VISIBLE);
                expandedCvvView = layoutCvv;
                etEnterCvvStoredCard = layoutCvv.findViewById(R.id.etEnterCvv);
                etEnterCvvStoredCard.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() == 3) {
                            btnPayStoredCard.setButtonEnabled(true);
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                });
            }
        }
    };

    private void bindView(View v) {
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
        if (requestType != PaymentMethod.BRICK) {
            Intent result = new Intent();
            result.putExtra(Key.SDK_ERROR_MESSAGE, "NULL REQUEST_TYPE");
            statusCode = ResponseCode.ERROR;
            self.setResult(statusCode, result);
            getMainActivity().backFragment(null);
            return;
        }
        try {
            request = extras
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
        }

        timeout = request.getTimeout();

        layoutInputCard = v.findViewById(R.id.layoutInputCard);
        layoutStoredCard = v.findViewById(R.id.llStoredCard);

        etCardNumber = v.findViewById(R.id.etCardNumber);
        etExpirationDate = v.findViewById(R.id.etExpireDate);
        etCvv = v.findViewById(R.id.etCvv);
        etEmail = v.findViewById(R.id.etEmail);
        etNameOnCard = v.findViewById(R.id.etName);

        llCardList = v.findViewById(R.id.llCardList);
        TextView etNewCard = v.findViewById(R.id.etNewCard);

        btnConfirm = v.findViewById(R.id.btnPay);
        btnPayStoredCard = v.findViewById(R.id.btnPayStoredCard);
        btnBack = v.findViewById(R.id.btnBack);

        tvAgreement = v.findViewById(R.id.tvAgreement);
        tvAgreementCardSelect = v.findViewById(R.id.tvAgreementSelectCard);
        tvStoreCard = v.findViewById(R.id.tvStoreCard);
        cbStoreCard = v.findViewById(R.id.cbStoreCard);
        tvCardPayTitle = v.findViewById(R.id.tvCardPayTitle);
        rootBrickLayout = v.findViewById(R.id.rootBrickLayout);
        contentLayout = v.findViewById(R.id.contentLayout);

        btnPayStoredCard.setButtonOnClickListener(v1 -> {
            hideKeyboard();
            showWaitLayout();
            Brick.getInstance().generateTokenFromPermanentToken(
                    request.getAppKey(), permanentToken,
                    etEnterCvvStoredCard.getText().toString().trim(),
                    BrickNewFragment.this
            );
        });

        btnConfirm.setButtonOnClickListener(view -> validateBrickCardInfo(true));

        etNewCard.setOnClickListener(v2 -> {
            isAddingNewCard = true;
            layoutInputCard.setVisibility(View.VISIBLE);
            layoutStoredCard.setVisibility(View.GONE);
        });

        if (mCardNumber != null) {
            etCardNumber.setText(mCardNumber);
        }

        if (mCvv != null) {
            etCvv.setText(mCvv);
        }

        if (mExpDate != null) {
            etExpirationDate.setText(mExpDate);
        }

        if (mEmail != null) {
            etEmail.setText(mEmail);
        }

        if (mNameOnCard != null) {
            etNameOnCard.setText(mNameOnCard);
        }

        init(v);
        initDataView();
    }


    private void init(View v) {
        validateBrickCardInfo(false);
        etCardNumber.setTag(etCardNumber.getCompoundDrawables()[0]);
        etCardNumber.addTextChangedListener(new TextWatcher() {
            private boolean mFormatting; // this is a flag which prevents the  stack overflow.
            private int mAfter;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // nothing to do here..
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nothing to do here...
                mAfter = after; // flag to detect backspace..
            }

            @Override
            public void afterTextChanged(Editable s) {
//                // Make sure to ignore calls to afterTextChanged caused by the work done below
                if (!mFormatting) {
                    mFormatting = true;
                    // using US formatting...
                    if (mAfter != 0) // in case back space ain't clicked...
                        PhoneNumberUtils.formatNumber(s, PhoneNumberUtils.getFormatTypeForLocale(Locale.US));
                    mFormatting = false;
                }

                validateBrickCardInfo(false);
                mCardNumber = etCardNumber.getText().toString();
            }
        });

        etExpirationDate.setTag(etExpirationDate.getCompoundDrawables()[0]);
        etExpirationDate.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting = false;
            private int prevLength = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prevLength = s != null ? s.length() : 0;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isFormatting || s == null) return;

                isFormatting = true;

                String clean = s.toString().replaceAll("[^\\d]", "");
                int length = clean.length();
                StringBuilder builder = new StringBuilder();

                if (length >= 1) {
                    int mm;
                    String monthPart;

                    if (length >= 2) {
                        mm = Integer.parseInt(clean.substring(0, 2));
                        if (mm > 12) {

                            monthPart = "0" + clean.charAt(0);
                            builder.append(monthPart);
                            builder.append("/");

                            builder.append(clean.substring(1, Math.min(4, length)));
                        } else {
                            builder.append(String.format("%02d", mm));
                            builder.append("/");
                            if (length > 2) {
                                builder.append(clean.substring(2, Math.min(4, length)));
                            }
                        }
                    } else {
                        builder.append(clean);
                    }
                }

                String formatted = builder.toString();

                int selection = formatted.length() > prevLength
                        ? formatted.length()
                        : etExpirationDate.getSelectionStart();

                etExpirationDate.setText(formatted);
                etExpirationDate.setSelection(Math.min(selection, formatted.length()));
                isFormatting = false;
                mExpDate = etExpirationDate.getText().toString();
                validateBrickCardInfo(false);
            }

        });

        etCvv.setTag(etCvv.getCompoundDrawables()[0]);
        etCvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mCvv = etCvv.getText().toString();
                validateBrickCardInfo(false);
            }
        });

        etEmail.setTag(etEmail.getCompoundDrawables()[0]);
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                }
                mEmail = etEmail.getText().toString();
                validateBrickCardInfo(false);
            }
        });

        etNameOnCard.setTag(etNameOnCard.getCompoundDrawables()[0]);
        etNameOnCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                }
                mNameOnCard = etNameOnCard.getText().toString();
                validateBrickCardInfo(false);
            }
        });

        btnBack.setOnClickListener(button -> {
            if (isAddingNewCard) {
                isAddingNewCard = false;
                layoutStoredCard.setVisibility(View.VISIBLE);
                layoutInputCard.setVisibility(View.GONE);
            } else {
                getMainActivity().backFragment(null);
            }
        });

        tvStoreCard.setOnClickListener(view -> cbStoreCard.setChecked(!cbStoreCard.isChecked()));

        contentLayout.setOnClickListener(view ->
                hideKeyboard());

        PwUtils.setCustomAttributes(self, v);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SCAN_CARD) {
            if (data != null && data.hasExtra("com.paymentwall.cardio.scanResult")) {
                try {
                    Object scanResult = data.getParcelableExtra("com.paymentwall.cardio.scanResult");
                    Class<?> CreditCard = Class.forName("com.paymentwall.cardio.CreditCard");
                    Object creditCard = CreditCard.cast(scanResult);
                    Method mthGetFormattedCardNumber = CreditCard.getMethod("getFormattedCardNumber");

                    etCardNumber.setText(mthGetFormattedCardNumber.invoke(creditCard) + "");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
            }
        }
    }

    public void showDeleteCardConfirmation(final CardEditText et) {
        final Dialog dialog = new Dialog(self);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.saas_frag_dialog);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.delete_card_title));

        TextView tvMessage = dialog.findViewById(R.id.tvConfirmation);
        tvMessage.setText(getString(R.string.delete_card_confirmation));
        TextView tvYes = dialog.findViewById(R.id.tvYes);
        TextView tvNo = dialog.findViewById(R.id.tvNo);

        tvYes.setOnClickListener(v -> {
            dialog.dismiss();
            String cards = SharedPreferenceManager.getInstance(self).getStringValue(SharedPreferenceManager.STORED_CARDS);
            try {
                JSONObject obj = new JSONObject(cards);
                obj.remove(et.getCardNumber() + "###" + et.getCardType());
                SmartLog.i("CardList: " + obj);
                if (obj.toString().equalsIgnoreCase("{}")) {
                    SharedPreferenceManager.getInstance(self).putStringValue(SharedPreferenceManager.STORED_CARDS, "");
                    btnPayStoredCard.setButtonEnabled(false);
                } else {
                    SharedPreferenceManager.getInstance(self).putStringValue(SharedPreferenceManager.STORED_CARDS, obj.toString());
                }
                llCardList.removeView((View) et.getParent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        tvNo.setOnClickListener(v -> dialog.dismiss());

        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onChargeSuccess(String permanentToken) {
        this.permanentToken = permanentToken;
        if (isWaitLayoutShowing()) {
            hideWaitLayout();
        }
        hide3dsWebview();
        handler.removeCallbacks(checkTimeoutTask);
        if (SharedPreferenceManager.getInstance(self).isCardExisting(mCardNumber)) {
            statusCode = ResponseCode.SUCCESSFUL;
            sendResultBack(statusCode);
        } else {
            BrickNewFragment.getInstance().onStoreCardConfirm(cbStoreCard.isChecked());
        }
    }

    public void onChargeFailed(String error) {
        if (isWaitLayoutShowing()) {
            hideWaitLayout();
        }
        hide3dsWebview();
        isBrickError = true;
        getMainActivity();
        PaymentSelectionActivity.paymentError = (error == null ? getString(R.string.payment_error) : error);
        showErrorLayout(error);
    }

    public void onStoreCardConfirm(boolean agree) {
        if (agree) {
            SharedPreferenceManager.getInstance(self).addCard(mCardNumber, permanentToken, mEmail, Const.getCardType(mCardNumber).name());
            Toast.makeText(self, getString(R.string.store_card_success), Toast.LENGTH_SHORT).show();
        }
        statusCode = ResponseCode.SUCCESSFUL;
        sendResultBack(statusCode);
    }

    @Override
    public void showWaitLayout() {
        getMainActivity().isWaitLayoutShowing = true;
        setViewHierarchyEnabled(rootBrickLayout, false);
        if (layoutInputCard.getVisibility() == View.VISIBLE) {
            btnConfirm.onStartLoading();
        }
        if (layoutStoredCard.getVisibility() == View.VISIBLE) {
            btnPayStoredCard.onStartLoading();
        }
    }

    @Override
    public void hideWaitLayout() {
        getMainActivity().isWaitLayoutShowing = false;
        setViewHierarchyEnabled(rootBrickLayout, true);
        if (layoutInputCard.getVisibility() == View.VISIBLE) {
            btnConfirm.onStopLoading();
        }
        if (layoutStoredCard.getVisibility() == View.VISIBLE) {
            btnPayStoredCard.onStopLoading();
        }
    }

    private Integer getExpTime(Const.TypeOfTime type) {
        if (etExpirationDate.getText().length() >= 4) {
            String[] values = etExpirationDate.getText().toString().split("/");
            if (type == Const.TypeOfTime.MONTH) return Integer.parseInt(values[0]);
            else return Integer.parseInt(values[1]);
        }
        return -1;
    }

    private void validateBrickCardInfo(boolean confirmed) {
        String cardNumber = etCardNumber.getText().toString().replaceAll(" ", "");
        String expMonth = String.valueOf(getExpTime(Const.TypeOfTime.MONTH));
        String expYear = String.valueOf(getExpTime(Const.TypeOfTime.YEAR));
        String cvv = etCvv.getText().toString();
        String email = etEmail.getText().toString();
        btnConfirm.setButtonEnabled(
                !cardNumber.isBlank()
                        && !expMonth.equals("-1")
                        && !expYear.equals("-1")
                        && !cvv.isBlank()
                        && !email.isBlank()
        );
        int colorNormal = PwUtils.getColorFromAttribute(self, "textInputForm");
        int colorError = PwUtils.getColorFromAttribute(self, "textInputErrorForm");

        // Create a brickcard object
        final BrickCard brickCard = new BrickCard(cardNumber, expMonth, expYear, cvv, email);
        // Check if the card data is possible or not
        if (!brickCard.isValid()) {
            // if confirm button is not pressed
            if (!confirmed) {
                if (brickCard.isNumberValid()) etCardNumber.setTextColor(colorNormal);
                if (brickCard.isExpValid()) etExpirationDate.setTextColor(colorNormal);
                if (brickCard.isCvcValid()) etCvv.setTextColor(colorNormal);
                if (brickCard.isEmailValid()) etEmail.setTextColor(colorNormal);
                if (etNameOnCard.getText().toString().trim().isEmpty())
                    etNameOnCard.setTextColor(colorNormal);
                return;
            }

            if (!brickCard.isNumberValid()) {
                etCardNumber.setTextColor(colorError);
            }
            if (!brickCard.isExpValid()) {
                etExpirationDate.setTextColor(colorError);
            }
            if (!brickCard.isCvcValid()) {
                etCvv.setTextColor(colorError);
            }
            if (!brickCard.isEmailValid()) {
                etEmail.setTextColor(colorError);
            }
            if (etNameOnCard.getText().toString().trim().isEmpty()) {
                etNameOnCard.setTextColor(colorError);
            }
        } else {
            etCardNumber.setTextColor(colorNormal);
            etExpirationDate.setTextColor(colorNormal);
            etCvv.setTextColor(colorNormal);
            etEmail.setTextColor(colorNormal);
            etNameOnCard.setTextColor(colorNormal);
            if (confirmed) {
                showWaitLayout();
                Brick.getInstance().createToken(self, request.getAppKey(), brickCard, this);
            }
        }
    }

    @Override
    public void onBrickSuccess(BrickToken brickToken) {
        String token = brickToken.getToken();
        if (token != null) {
            Intent intent = new Intent();
            intent.setAction(getActivity().getPackageName() + Brick.BROADCAST_FILTER_MERCHANT);
            intent.putExtra(Brick.KEY_BRICK_TOKEN, token);
            intent.putExtra(Brick.KEY_BRICK_EMAIL, mEmail);
            intent.putExtra(Brick.KEY_BRICK_CARDHOLDER, mNameOnCard);
            if (fingerprint != null) {
                intent.putExtra(Brick.KEY_BRICK_FINGERPRINT, fingerprint);
            }

//            getActivity().sendBroadcast(intent);
            LocalBroadcastManager.getInstance(self).sendBroadcast(intent);
            handler.postDelayed(checkTimeoutTask, timeout);
        }
    }

    @Override
    public void onBrickError(BrickError error) {
        hideWaitLayout();
        showErrorLayout(error.getMessage());
    }

    private final Runnable checkTimeoutTask = new Runnable() {
        @Override
        public void run() {
            isBrickError = true;
            getMainActivity();
            PaymentSelectionActivity.paymentError = getString(R.string.timeout_connnection);
            handler.removeCallbacks(this);
            hideWaitLayout();
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        populateViewForOrientation(inflater, (ViewGroup) getView());
    }

    private void populateViewForOrientation(LayoutInflater inflater, ViewGroup viewGroup) {
        viewGroup.removeAllViewsInLayout();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            View v = inflater.inflate(PwUtils.getLayoutId(self, "frag_brick_new"), viewGroup);
            bindView(v);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            View v = inflater.inflate(PwUtils.getLayoutId(self, "frag_brick_new"), viewGroup);
            bindView(v);
        }
    }
}
