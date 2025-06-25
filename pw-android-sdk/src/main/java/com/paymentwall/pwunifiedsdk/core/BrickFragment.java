package com.paymentwall.pwunifiedsdk.core;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.paymentwall.pwunifiedsdk.R;
import com.paymentwall.pwunifiedsdk.brick.core.Brick;
import com.paymentwall.pwunifiedsdk.brick.core.BrickCard;
import com.paymentwall.pwunifiedsdk.brick.core.BrickError;
import com.paymentwall.pwunifiedsdk.brick.core.BrickToken;
import com.paymentwall.pwunifiedsdk.brick.message.BrickRequest;
import com.paymentwall.pwunifiedsdk.brick.ui.adapter.ExpireMonthAdapter;
import com.paymentwall.pwunifiedsdk.brick.ui.views.MaskedEditText;
import com.paymentwall.pwunifiedsdk.brick.utils.PaymentMethod;
import com.paymentwall.pwunifiedsdk.googlepay.core.GPayCore;
import com.paymentwall.pwunifiedsdk.ui.CardEditText;
import com.paymentwall.pwunifiedsdk.ui.WaveHelper;
import com.paymentwall.pwunifiedsdk.util.Key;
import com.paymentwall.pwunifiedsdk.util.MiscUtils;
import com.paymentwall.pwunifiedsdk.util.PwUtils;
import com.paymentwall.pwunifiedsdk.util.ResponseCode;
import com.paymentwall.pwunifiedsdk.util.SharedPreferenceManager;
import com.paymentwall.pwunifiedsdk.util.SmartLog;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by nguyen.anh on 7/19/2016.
 */

public class BrickFragment extends BaseFragment implements Brick.Callback {

    private TextView tvProduct, tvPrice, tvCopyright;
    private ImageView ivProduct;

    private LinearLayout llInputCard, llStoredCard;

    private MaskedEditText etCardNumber;
    private EditText etCvv, etExpirationDate;
    private EditText etEmail, etNameOnCard;
    private static String mCardNumber, mCvv, mExpDate, mEmail, mNameOnCard;
    private LinearLayout llScanCard;
    private static int monthSelection = -1;

    //Layout stored cards
    private LinearLayout llCardList;
    private EditText etNewCard;
    private CardEditText clickedCard;
    private String permanentToken;
    private String fingerprint;

    private WaveHelper helper;

    private Button btnConfirm;

    private int statusCode;
    private BrickRequest request;

    private int timeout;
    private boolean isBrickError;


    private int month = -1, year = -1, currentYear = -1;

    //layout datePicker
    private LinearLayout llExpirationDate;
    private ImageView ivPrevious, ivNext;
    private TextView tvYear;
    private GridView gvMonth;
    private ExpireMonthAdapter expireMonthAdapter;
    private static boolean isDatePickerShowing;
    private Handler handler = new Handler();

    public static final int RC_SCAN_CARD = 2505;

    private final Integer[] SIMPLIFIED_MONTHS = {R.string.month_1, R.string.month_2, R.string.month_3, R.string.month_4, R.string.month_5, R.string.month_6, R.string.month_7, R.string.month_8, R.string.month_9, R.string.month_10, R.string.month_11, R.string.month_12};

    private final Integer[] FULL_MONTHS = {R.string.month_1_full, R.string.month_2_full, R.string.month_3_full, R.string.month_4_full, R.string.month_5_full, R.string.month_6_full, R.string.month_7_full, R.string.month_8_full, R.string.month_9_full, R.string.month_10_full, R.string.month_11_full, R.string.month_12_full};

    public static BrickFragment instance;

    public static BrickFragment getInstance() {
        if (instance == null)
            instance = new BrickFragment();
        return instance;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(self.getPackageName() + Brick.BROADCAST_FILTER_SDK)) {

                if (intent.getExtras().containsKey(Brick.KEY_MERCHANT_SUCCESS)) {
                    int success = intent.getIntExtra(Brick.KEY_MERCHANT_SUCCESS, -1);
                    Log.i("BRICK_RECEIVER", success + "");
//                    if (isWaitLayoutShowing()) {
//                        hideWaitLayout();
//                    }
                    handler.removeCallbacks(checkTimeoutTask);
                    if (success == 1) {
                        String permanentToken = intent.getStringExtra(Brick.KEY_PERMANENT_TOKEN);
                        if (!permanentToken.equalsIgnoreCase("")) {
                            onChargeSuccess(permanentToken);
                        } else {
                            statusCode = ResponseCode.SUCCESSFUL;
                            displayPaymentSucceeded();
                        }
                    } else {
                        String error = intent.getStringExtra(Brick.KEY_PERMANENT_TOKEN);
                        isBrickError = true;
                        if (error != null && !error.equals("")) {
                            getMainActivity().paymentError = error;
                        } else {
                            getMainActivity().paymentError = getString(R.string.payment_error);
                        }
                        showErrorLayout(getMainActivity().paymentError);
                    }
                } else if (intent.getExtras().containsKey(Brick.KEY_3DS_FORM)) {
                    String form3ds = intent.getStringExtra(Brick.KEY_3DS_FORM);
                    if (isWaitLayoutShowing()) {
                        hideWaitLayout();
                    }
                    getMainActivity().show3dsWebView(form3ds);
                }
            } else if (intent.getAction().equalsIgnoreCase(self.getPackageName() + Brick.FILTER_BACK_PRESS_FRAGMENT)) {
                if (isDatePickerShowing) {
                    hideDatePicker();
                } else if (getMainActivity().isUnsuccessfulShowing) {
                    hideErrorLayout();
                } else if (getMainActivity().isSuccessfulShowing || isWaitLayoutShowing()) {
                    return;
                } else {
                    Intent i = new Intent();
                    i.setAction(self.getPackageName() + Brick.FILTER_BACK_PRESS_ACTIVITY);
                    LocalBroadcastManager.getInstance(self).sendBroadcast(i);
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PwUtils.logFabricCustom("Visit-BrickScreen");

        Log.i("BRICK-packagename", self.getPackageName());

        IntentFilter filter = new IntentFilter();
        filter.addAction(self.getPackageName() + Brick.FILTER_BACK_PRESS_FRAGMENT);
        filter.addAction(self.getPackageName() + Brick.BROADCAST_FILTER_SDK);
        LocalBroadcastManager.getInstance(self).registerReceiver(receiver, filter);

        Brick.getInstance().setContext(self);

        mCardNumber = null;
        mCvv = null;
        mExpDate = null;
        mEmail = null;
        mNameOnCard = null;

        monthSelection = -1;
        currentYear = -1;
        month = -1;
        year = -1;

        getMainActivity().isWaitLayoutShowing = false;
        getMainActivity().isUnsuccessfulShowing = false;
        getMainActivity().isSuccessfulShowing = false;
        isDatePickerShowing = false;
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
        View v = inflater.inflate(PwUtils.getLayoutId(self, "frag_brick"), container, false);
        bindView(v);
        setFonts(v);
        return v;
    }

    private void setFonts(View v) {
        PwUtils.setFontLight(self, new TextView[]{(TextView) v.findViewById(R.id.tvCopyRight)});
        PwUtils.setFontRegular(self, new TextView[]{(TextView) v.findViewById(R.id.tvProduct), (TextView) v.findViewById(R.id.tvTotal), (TextView) v.findViewById(R.id.tvPrice)});
        PwUtils.setFontBold(self, new TextView[]{(Button) v.findViewById(R.id.btnConfirm)});
    }

    private void checkScannerPlugin() {
        try {
            Class<?> CardIOActivity = Class.forName("com.paymentwall.cardio.CardIOActivity");
            if (CardIOActivity != null) llScanCard.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            llScanCard.setVisibility(View.GONE);
        }
    }

    private void checkStoredCard() {
        String cards = SharedPreferenceManager.getInstance(self).getStringValue(SharedPreferenceManager.STORED_CARDS);
        SmartLog.i("CardList: " + cards);
        if (cards.equalsIgnoreCase("")) {
            llInputCard.setVisibility(View.VISIBLE);
            llStoredCard.setVisibility(View.GONE);
        } else {
            llInputCard.setVisibility(View.GONE);
            llStoredCard.setVisibility(View.VISIBLE);
            try {
                LayoutInflater inflater = (LayoutInflater) self.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                JSONObject obj = new JSONObject(cards);
                Iterator<String> iter = obj.keys();
                llCardList.removeAllViews();
                while (iter.hasNext()) {
                    String key = iter.next();
                    String value = obj.getString(key);
                    String[] values = value.split("###");

                    View view = inflater.inflate(PwUtils.getLayoutId(self, "stored_card_layout"), null);
                    final CardEditText etCard = (CardEditText) view.findViewById(R.id.etStoredCard);

                    etCard.setCardNumber(key);
                    etCard.setPermanentToken(values[0]);
                    etCard.setEmail(values[1]);
                    etCard.setText("xxxx xxxx xxxx " + key);
                    etCard.setOnClickListener(onClickStoredCard);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) PwUtils.dpToPx(self, 64f));
                    params.setMargins(0, (int) PwUtils.dpToPx(self, 2f), 0, 0);
                    view.setLayoutParams(params);

                    etCard.setDrawableClickListener(new CardEditText.DrawableClickListener() {
                        @Override
                        public void onClick(DrawablePosition target) {
                            if (target == DrawablePosition.RIGHT) {
                                showDeleteCardConfirmation(etCard);
                            }
                        }
                    });
//                    view.setOnLongClickListener(onLongClickStoredCard);

                    Drawable drawableLeft = getResources().getDrawable(R.drawable.ic_creditcard);
                    Drawable drawableRight = getResources().getDrawable(R.drawable.ic_delete_card);
                    etCard.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableRight, null);

                    llCardList.addView(view);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private View.OnClickListener onClickStoredCard = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickedCard = (CardEditText) v;
            mEmail = clickedCard.getEmail();
            showInputCvvDialog(clickedCard.getPermanentToken());
        }
    };


    private View.OnLongClickListener onLongClickStoredCard = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            showDeleteCardConfirmation((CardEditText) v);
            return true;
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
            request = (BrickRequest) extras
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

        llInputCard = (LinearLayout) v.findViewById(R.id.llInputCard);
        llStoredCard = (LinearLayout) v.findViewById(R.id.llStoredCard);

        tvProduct = (TextView) v.findViewById(R.id.tvProduct);
        tvPrice = (TextView) v.findViewById(R.id.tvPrice);
        ivProduct = (ImageView) v.findViewById(R.id.ivProduct);

        ivNext = (ImageView) v.findViewById(R.id.ivNext);
        ivPrevious = (ImageView) v.findViewById(R.id.ivPrevious);

        etCardNumber = (MaskedEditText) v.findViewById(R.id.etCardNumber);
        etExpirationDate = (EditText) v.findViewById(R.id.etExpireDate);
        etCvv = (EditText) v.findViewById(R.id.etCvv);
        etEmail = (EditText) v.findViewById(R.id.etEmail);
        etNameOnCard = (EditText) v.findViewById(R.id.etName);
        llScanCard = (LinearLayout) v.findViewById(R.id.llScanCard);

        llCardList = (LinearLayout) v.findViewById(R.id.llCardList);
        etNewCard = (EditText) v.findViewById(R.id.etNewCard);

        btnConfirm = (Button) v.findViewById(R.id.btnConfirm);

        llExpirationDate = (LinearLayout) v.findViewById(R.id.llExpirationDate);
        gvMonth = (GridView) v.findViewById(R.id.gvMonth);
        tvYear = (TextView) v.findViewById(R.id.tvYear);

        tvCopyright = (TextView) v.findViewById(R.id.tvCopyRight);
        tvCopyright.setText(String.format(getString(R.string.secured_by_pw), new SimpleDateFormat("yyyy").format(new Date())));

        v.findViewById(R.id.llAddress).setVisibility(getMainActivity().request.isFooterEnabled() ? View.VISIBLE : View.GONE);

//        tvRedirecting.setText(String.format(getString(R.string.redirecting_to), PwUtils.getApplicationName(self)));


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
                    Glide.with(self).load(new File(realPath)).into(ivProduct);
                } else {
                    ((View) (ivProduct.getParent())).setVisibility(View.GONE);
                }
            }
        });

        if (request.getName() != null) {
            tvProduct.setText(request.getName());
            if (request.getAmount() != null && request.getCurrency() != null)
                tvPrice.setText(PwUtils.getCurrencySymbol(request.getCurrency()) + request.getAmount());
        }

        etCardNumber.addOnFullFillListener(new MaskedEditText.onFullFillListener() {
            @Override
            public void onFullFill() {
            }
        });

        etExpirationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDatePicker();
                return;
            }
        });

        llScanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Class<?> CardIOActivity = Class.forName("com.paymentwall.cardio.CardIOActivity");
                    Intent scanIntent = new Intent(self, CardIOActivity);

                    scanIntent.putExtra("com.paymentwall.cardio.scanExpiry", true); // default: false

                    self.startActivityForResult(scanIntent, RC_SCAN_CARD);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateBrickCardInfo(true);
//                showWaitLayout();
                return;
            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangeExpirationYear(1);
            }
        });

        ivPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangeExpirationYear(-1);
            }
        });

        etNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llInputCard.setVisibility(View.VISIBLE);
                llStoredCard.setVisibility(View.GONE);
            }
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

        if (isDatePickerShowing) {
            displayDatePicker();
        }

        init(v);
        checkScannerPlugin();
        checkStoredCard();
    }

    private void init(View v) {
        validateBrickCardInfo(false);

        Calendar cal = Calendar.getInstance();
        String currentMonth = String.format("%tm", cal);
        if (month == -1)
            month = Integer.parseInt(currentMonth);

        String strYear = String.format("%tY", cal);
        if (year == -1)
            year = Integer.parseInt(strYear);
        if (currentYear == -1)
            currentYear = Integer.parseInt(strYear);
        tvYear.setText(year + "");
        onChangeExpirationYear(0);

        gvMonth = (GridView) v.findViewById(R.id.gvMonth);
        expireMonthAdapter = new ExpireMonthAdapter(self, Arrays.asList(SIMPLIFIED_MONTHS), new ExpireMonthAdapter.IExpirateMonth() {
            @Override
            public void onClickMonth(int position) {
                monthSelection = position;
                expireMonthAdapter.setSelectedPosition(position);
                expireMonthAdapter.notifyDataSetChanged();
                month = position + 1;
                onChangeExpirationYear(0);
                hideDatePicker();
                etEmail.requestFocus();
                showKeyboard(etEmail);
            }
        });

        if (monthSelection == -1) monthSelection = Integer.parseInt(currentMonth) - 1;
        expireMonthAdapter.setSelectedPosition(monthSelection);
        gvMonth.setAdapter(expireMonthAdapter);

        llExpirationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDatePicker();
            }
        });

        etCardNumber.setTag(etCardNumber.getCompoundDrawables()[0]);
        etCardNumber.addTextChangedListener(new TextWatcher() {
            private boolean mFormatting; // this is a flag which prevents the  stack overflow.
            private int mAfter;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // nothing to do here..
            }

            //called before the text is changed...
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nothing to do here...
                mAfter = after; // flag to detect backspace..
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Make sure to ignore calls to afterTextChanged caused by the work done below
                if (!mFormatting) {
                    mFormatting = true;
                    // using US formatting...
                    if (mAfter != 0) // in case back space ain't clicked...
                        PhoneNumberUtils.formatNumber(s, PhoneNumberUtils.getFormatTypeForLocale(Locale.US));
                    mFormatting = false;
                }

                if (s.length() > 0) {
                    etCardNumber.setBackgroundDrawable(PwUtils.getDrawableFromAttribute(self, "bgInputForm"));
                    etCardNumber.setHint(getString(R.string.card_number_hint));
                    etCardNumber.setCompoundDrawables((Drawable) etCardNumber.getTag(), null, null, null);
                }
                validateBrickCardInfo(false);
                mCardNumber = etCardNumber.getText().toString();
            }
        });

        etExpirationDate.setTag(etExpirationDate.getCompoundDrawables()[0]);
        etExpirationDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    Drawable drawableRight = getResources().getDrawable(R.drawable.ic_cc_down);
                    etExpirationDate.setCompoundDrawablesWithIntrinsicBounds((Drawable) etExpirationDate.getTag(), null, drawableRight, null);
                    etExpirationDate.setBackgroundDrawable(PwUtils.getDrawableFromAttribute(self, "bgInputForm"));
                    etExpirationDate.setHint("");
                }
                mExpDate = etCvv.getText().toString();
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
                if (s.length() > 0) {
                    etCvv.setBackgroundDrawable(PwUtils.getDrawableFromAttribute(self, "bgInputForm"));
                    etCvv.setHint("");
                    etCvv.setCompoundDrawables((Drawable) etCvv.getTag(), null, null, null);
                }
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
                    etEmail.setBackgroundDrawable(PwUtils.getDrawableFromAttribute(self, "bgInputForm"));
                    etEmail.setHint("");
                    etEmail.setCompoundDrawables((Drawable) etEmail.getTag(), null, null, null);
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
                    etNameOnCard.setBackgroundDrawable(PwUtils.getDrawableFromAttribute(self, "bgInputForm"));
                    etNameOnCard.setHint(getString(R.string.brick_hint_cardholder));
                    etNameOnCard.setCompoundDrawables((Drawable) etNameOnCard.getTag(), null, null, null);
                }
                mNameOnCard = etNameOnCard.getText().toString();
                validateBrickCardInfo(false);
            }
        });

        etCvv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    displayDatePicker();
                    return true;
                }
                return false;
            }
        });

        etEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });

        PwUtils.setCustomAttributes(self, v);

        final GradientDrawable expDrawable = (GradientDrawable) v.getResources()
                .getDrawable(R.drawable.saas_bg_expiration_date_dialog);
        expDrawable.setColor(PwUtils.getColorFromAttribute(self, "bgExpDialog"));
        final LinearLayout llExpDialog = (LinearLayout) v.findViewById(R.id.llExpDialog);
        if (llExpDialog != null) {
            llExpDialog.post(new Runnable() {
                @Override
                public void run() {
                    llExpDialog.setBackgroundDrawable(expDrawable);
                }
            });
        }
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
//                    if (scanResult.isExpiryValid()) {
//                        etExpirationDate.setText(scanResult.expiryMonth + "/" + scanResult.expiryYear);
//                    }
//
//                    if (scanResult.cvv != null) {
//                        etCvv.setText(scanResult.cvv);
//                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
            }
        }
    }

    public void showStoreCardConfirmationDialog() {
        final Dialog dialog = new Dialog(self);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_store_card_confirm);
        dialog.setCancelable(false);
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tvConfirmation);
        tvTitle.setText(getString(R.string.store_card_confirmation));
        TextView tvYes = (TextView) dialog.findViewById(R.id.tvYes);
        TextView tvNo = (TextView) dialog.findViewById(R.id.tvNo);

        tvYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                BrickFragment.getInstance().onStoreCardConfirm(true);
            }
        });

        tvNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                BrickFragment.getInstance().onStoreCardConfirm(false);
            }
        });

        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDeleteCardConfirmation(final CardEditText et) {
        final Dialog dialog = new Dialog(self);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_store_card_confirm);
        dialog.setCancelable(false);
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tvConfirmation);
        tvTitle.setText(getString(R.string.delete_card_confirmation));
        TextView tvYes = (TextView) dialog.findViewById(R.id.tvYes);
        TextView tvNo = (TextView) dialog.findViewById(R.id.tvNo);

        tvYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                String cards = SharedPreferenceManager.getInstance(self).getStringValue(SharedPreferenceManager.STORED_CARDS);
                try {
                    JSONObject obj = new JSONObject(cards);
                    obj.remove(et.getCardNumber());
                    SmartLog.i("CardList: " + obj.toString());
                    if (obj.toString().equalsIgnoreCase("{}")) {
                        SharedPreferenceManager.getInstance(self).putStringValue(SharedPreferenceManager.STORED_CARDS, "");
                    } else {
                        SharedPreferenceManager.getInstance(self).putStringValue(SharedPreferenceManager.STORED_CARDS, obj.toString());
                    }
                    llCardList.removeView(et);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tvNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showInputCvvDialog(final String permanentToken) {
        final Dialog dialog = new Dialog(self);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_input_cvv);
        dialog.setCancelable(false);
        final TextView tvSubmit = (TextView) dialog.findViewById(R.id.tvSubmit);
        tvSubmit.setEnabled(false);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
        final EditText etCvv = (EditText) dialog.findViewById(R.id.etCvv);

        etCvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 3) {
                    tvSubmit.setEnabled(true);
                } else {
                    tvSubmit.setEnabled(false);
                }
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                hideKeyboard();
                try {
                    showWaitLayout();
                    Brick.getInstance().generateTokenFromPermanentToken(request.getAppKey(), permanentToken, etCvv.getText().toString().trim(), BrickFragment.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                hideKeyboard();
            }
        });

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
            displayPaymentSucceeded();
        } else {
            showStoreCardConfirmationDialog();
        }
    }

    public void onChargeFailed(String error) {
        if (isWaitLayoutShowing()) {
            hideWaitLayout();
        }
        hide3dsWebview();
        isBrickError = true;
        getMainActivity().paymentError = (error == null ? getString(R.string.payment_error) : error);
        showErrorLayout(error);
    }

    public void onStoreCardConfirm(boolean agree) {
        if (agree) {
            SharedPreferenceManager.getInstance(self).addCard(mCardNumber, permanentToken, mEmail, "");
            Toast.makeText(self, getString(R.string.store_card_success), Toast.LENGTH_SHORT).show();
        }
        statusCode = ResponseCode.SUCCESSFUL;
        displayPaymentSucceeded();
    }

    private void onChangeExpirationYear(int increase) {
        year += increase;
        if (year == currentYear) {
            ivPrevious.setVisibility(View.INVISIBLE);
        } else {
            ivPrevious.setVisibility(View.VISIBLE);
        }
        tvYear.setText(year + "");
        etExpirationDate.setText(month + "/" + year);
    }

    private void validateBrickCardInfo(boolean confirmed) {
        String cardNumber = etCardNumber.getText().toString().replaceAll(" ", "");
        String expMonth = String.valueOf(month);
        String expYear = String.valueOf(year);
        String cvv = etCvv.getText().toString();
        String email = etEmail.getText().toString();

        etCardNumber.clearFocus();

        // Create a brickcard object
        final BrickCard brickCard = new BrickCard(cardNumber, expMonth, expYear, cvv, email);
        // Check if the card data is possible or not
        if (!brickCard.isValid()) {
            // if confirm button is not pressed
            if (!confirmed) {
                btnConfirm.setEnabled(false);
                return;
            }
            Drawable drawableRight = PwUtils.getDrawableFromAttribute(self, "iconErrorForm");
            if (!brickCard.isNumberValid()) {
//                tvErrCard.setVisibility(View.VISIBLE);
                etCardNumber.setHint(R.string.err_invalid_card_number);
                etCardNumber.setText("");
                etCardNumber.setBackgroundDrawable(PwUtils.getDrawableFromAttribute(self, "bgInputErrorForm"));
                etCardNumber.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null);
            }
            if (!brickCard.isExpValid()) {
                etExpirationDate.setHint(R.string.err_invalid_expiration_date);
                etExpirationDate.setBackgroundDrawable(PwUtils.getDrawableFromAttribute(self, "bgInputErrorForm"));
                etExpirationDate.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null);
            }
            if (!brickCard.isCvcValid()) {
                etCvv.setHint(R.string.error_invalid_cvv);
                etCvv.setBackgroundDrawable(PwUtils.getDrawableFromAttribute(self, "bgInputErrorForm"));
                etCvv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null);
            }
            if (!brickCard.isEmailValid()) {
                etEmail.setHint(R.string.error_invalid_email);
                etEmail.setBackgroundDrawable(PwUtils.getDrawableFromAttribute(self, "bgInputErrorForm"));
                etEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null);
            }
            if (etNameOnCard.getText().toString().trim().length() == 0) {
                etNameOnCard.setHint(R.string.error_empty_name_on_card);
                etNameOnCard.setBackgroundDrawable(PwUtils.getDrawableFromAttribute(self, "bgInputErrorForm"));
                etNameOnCard.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null);
            }
        } else {
            if (confirmed) {
                showWaitLayout();
                Brick.getInstance().createToken(self, request.getAppKey(), brickCard, this);
            } else {
                btnConfirm.setEnabled(true);
            }
        }
    }

    private void displayDatePicker() {
        hideKeyboard();
        llExpirationDate.setVisibility(View.VISIBLE);
        isDatePickerShowing = true;
    }

    private void hideDatePicker() {
        llExpirationDate.setVisibility(View.GONE);
        isDatePickerShowing = false;
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
        showErrorLayout(null);
    }

    private Runnable checkTimeoutTask = new Runnable() {
        @Override
        public void run() {
            isBrickError = true;
            getMainActivity().paymentError = getString(R.string.timeout_connnection);
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
            View v = inflater.inflate(PwUtils.getLayoutId(self, "frag _brick"), viewGroup);
            bindView(v);
            setFonts(v);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            View v = inflater.inflate(PwUtils.getLayoutId(self, "frag_brick"), viewGroup);
            bindView(v);
            setFonts(v);
        }
    }
}
