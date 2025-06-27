package com.paymentwall.pwunifiedsdk.core;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.button.ButtonOptions;
import com.google.android.gms.wallet.button.PayButton;
import com.google.android.gms.wallet.contract.TaskResultContracts;
import com.paymentwall.pwunifiedsdk.R;
import com.paymentwall.pwunifiedsdk.brick.core.Brick;
import com.paymentwall.pwunifiedsdk.googlepay.core.GPayCore;
import com.paymentwall.pwunifiedsdk.googlepay.ui.GPayCheckoutViewModel;
import com.paymentwall.pwunifiedsdk.mint.utils.PaymentMethod;
import com.paymentwall.pwunifiedsdk.mobiamo.core.MobiamoDialogActivity;
import com.paymentwall.pwunifiedsdk.mobiamo.core.MobiamoResponse;
import com.paymentwall.pwunifiedsdk.mobiamo.utils.Const;
import com.paymentwall.pwunifiedsdk.payalto.core.PayAltoActivity;
import com.paymentwall.pwunifiedsdk.payalto.core.PayAltoCore;
import com.paymentwall.pwunifiedsdk.payalto.message.CustomRequest;
import com.paymentwall.pwunifiedsdk.payalto.utils.ApiType;
import com.paymentwall.pwunifiedsdk.util.Key;
import com.paymentwall.pwunifiedsdk.util.PaymentMethodAdapter;
import com.paymentwall.pwunifiedsdk.util.PwUtils;
import com.paymentwall.pwunifiedsdk.util.ResponseCode;
import com.paymentwall.pwunifiedsdk.util.SmartLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by nguyen.anh on 7/18/2016.
 */

public class MainFragment extends BaseFragment implements GPayCore.PaymentCallback, GPayCore.ClickIdCallback, PayAltoCore.PaymentSystemsCallback {

    private UnifiedRequest request;
    private Bundle bundle;
    private RecyclerView rcvAnotherMethods;
    private ViewGroup layoutProgressBar;
    private View viewBreakLineStart, tvAnotherMethods, viewBreakLineEnd;
    private PayButton payButton;
    private final int RC_SEND_SMS = 114;
    private final boolean isMerchantHandleGooglePayToken = false;
    private ViewGroup _mainViewGroup;
    private List<PayAltoCore.PayAltoMethod> listPaymentMethods = new ArrayList<>();
    private static MainFragment instance;

    public static MainFragment getInstance() {
        if (instance == null) {
            instance = new MainFragment();
        }
        return instance;
    }

    public static void clearInstance() {
        instance = null;
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(self.getPackageName() + Brick.FILTER_BACK_PRESS_FRAGMENT)) {
                Intent i = new Intent();
                i.setAction(self.getPackageName() + Brick.FILTER_BACK_PRESS_ACTIVITY);
                LocalBroadcastManager.getInstance(self).sendBroadcast(i);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        if (bundle != null && bundle.containsKey(Key.REQUEST_MESSAGE)) {
            request = bundle.getParcelable(Key.REQUEST_MESSAGE);
        }
        LocalBroadcastManager.getInstance(self).registerReceiver(receiver, new IntentFilter(self.getPackageName() + Brick.FILTER_BACK_PRESS_FRAGMENT));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(self).unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(PwUtils.getLayoutId(self, "frag_main"), container, false);
        _mainViewGroup = container;
        bindView(v);
        setFonts(v);
        init();
        return v;
    }

    private void setFonts(View v) {
        PwUtils.setFontLight(self, v.findViewById(R.id.tvCopyRight));
        PwUtils.setFontRegular(self, v.findViewById(R.id.tvProduct), v.findViewById(R.id.tvTotal), v.findViewById(R.id.tvPrice));
    }

    private void bindView(View v) {
        payButton = v.findViewById(R.id.btnGooglePay);

        ImageView btnClose = v.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(view -> {
            self.setResult(ResponseCode.CANCEL, new Intent());
            self.finish();
        });

        layoutProgressBar = v.findViewById(R.id.prBarLoading);

        tvAnotherMethods = v.findViewById(R.id.tvPayAnotherMethods);
        viewBreakLineStart = v.findViewById(R.id.viewBreakLineStart);
        viewBreakLineEnd = v.findViewById(R.id.viewBreakLineEnd);

        PwUtils.setCustomAttributes(self, v);
        rcvAnotherMethods = v.findViewById(R.id.rcvAnotherMethods);
    }

    private PayAltoCore.PayAltoMethod generateCardMethod() {
        try {
            String json = "{\"id\":\"gateway\"," +
                    "\"img_class\":\"gateway_dinerclub_discover\"," +
                    "\"img_url\":\"\"," +
                    "\"name\":\"Card\"," +
                    "\"new_window\":false," +
                    "\"ps_type_id\":1}";

            return new PayAltoCore.PayAltoMethod(new JSONObject(json));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void setRecyclerView() {
        getMainActivity().setDataPaymentMethod(listPaymentMethods);
        rcvAnotherMethods.setNestedScrollingEnabled(false);
        PaymentMethodAdapter methodAdapter = new PaymentMethodAdapter(requireContext(), listPaymentMethods, position -> {
            PayAltoCore.PayAltoMethod method = listPaymentMethods.get(position);
            if (Objects.equals(method.name, "Card")) {
                payWithBrick();
            } else {
                payWithLocalPS(method.id, method.name);
            }
        });
        rcvAnotherMethods.setAdapter(methodAdapter);
        if (listPaymentMethods.isEmpty()) setBreakLineVisibility(false);
    }

    private void payWithLocalPS(String psId, String psName) {
        Intent intent = new Intent(getActivity(), PayAltoActivity.class);
        CustomRequest cRequest = (CustomRequest) request.getPayAltoRequest();
        cRequest.put(ApiType.PS, psId);
        cRequest.put(ApiType.PS_NAME, psName);
        intent.putExtra(com.paymentwall.pwunifiedsdk.payalto.utils.Key.CUSTOM_REQUEST_TYPE, ApiType.DIGITAL_GOODS);
        intent.putExtra(com.paymentwall.pwunifiedsdk.payalto.utils.Key.CUSTOM_REQUEST_MAP, request.getPayAltoRequest());
        self.startActivityForResult(intent, PayAltoActivity.REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RC_SEND_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    payWithMobiamo();

                } else {

                }
            }
        }
    }

    // Track API calls to know when to hide loading
    final AtomicInteger pendingApiCalls = new AtomicInteger(0);

    private void init() {
        initGooglePay();
        initPayAlto();
    }

    private void payWithMint() {
        if (request.getMintRequest().validate()) {
            Bundle bundle = new Bundle();
            bundle.putInt(Key.PAYMENT_TYPE, PaymentMethod.MINT);
            bundle.putParcelable(Key.REQUEST_MESSAGE, request.getMintRequest());
            getMainActivity().replaceContentFragment(new MintFragment(), bundle);
        }
    }

    private void payWithBrick() {
        if (getMainActivity().isSuccessfulShowing = true) {
            getMainActivity().replaceContentFragment(BrickNewFragment.getInstance(), bundle);
        }
        if (request.getBrickRequest().validate()) {
            Bundle bundle = new Bundle();
            bundle.putInt(Key.PAYMENT_TYPE, com.paymentwall.pwunifiedsdk.brick.utils.PaymentMethod.BRICK);
            bundle.putParcelable(Key.REQUEST_MESSAGE, request.getBrickRequest());
            getMainActivity().replaceContentFragment(BrickNewFragment.getInstance(), bundle);
        }
    }

    private void payWithMobiamo() {
        Intent intent = new Intent(self, MobiamoDialogActivity.class);
        intent.putExtra(Const.KEY.REQUEST_MESSAGE, request.getMobiamoRequest());
        startActivityForResult(intent, MobiamoDialogActivity.MOBIAMO_REQUEST_CODE);
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
            View v = inflater.inflate(PwUtils.getLayoutId(self, "frag_main"), viewGroup);
            bindView(v);
            init();
            setFonts(v);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            View v = inflater.inflate(PwUtils.getLayoutId(self, "frag_main"), viewGroup);
            bindView(v);
            init();
            setFonts(v);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent = new Intent();
        if (requestCode == MobiamoDialogActivity.MOBIAMO_REQUEST_CODE) {

            if (resultCode == ResponseCode.ERROR) {
                self.setResult(ResponseCode.ERROR, intent);
                self.finish();
            } else if (resultCode == ResponseCode.FAILED) {
                self.setResult(ResponseCode.FAILED, intent);
                self.finish();
            } else if (resultCode == ResponseCode.CANCEL) {
                self.setResult(ResponseCode.CANCEL, intent);
                self.finish();

            } else if (resultCode == ResponseCode.SUCCESSFUL) {
                if (data != null) {
                    MobiamoResponse response = (MobiamoResponse) data.getSerializableExtra(Const.KEY.RESPONSE_MESSAGE);
                    if (response != null && response.isCompleted()) {
                        intent.putExtra(Const.KEY.RESPONSE_MESSAGE, (Serializable) response);
                        self.setResult(ResponseCode.SUCCESSFUL, intent);
                        self.finish();
                    }
                } else {
                }
            }
        } else if (requestCode == PayAltoActivity.REQUEST_CODE) {
            if (resultCode == ResponseCode.ERROR) {
                self.setResult(ResponseCode.ERROR, intent);
                self.finish();
            } else if (resultCode == ResponseCode.FAILED) {
                self.setResult(ResponseCode.FAILED, intent);
                self.finish();
            } else if (resultCode == ResponseCode.CANCEL) {
                self.setResult(ResponseCode.CANCEL, intent);
                self.finish();

            } else if (resultCode == ResponseCode.SUCCESSFUL) {
                self.setResult(ResponseCode.SUCCESSFUL, intent);
                self.finish();

            }
        }
    }

    // #region Loading animation

    @Override
    public void showWaitLayout() {
        layoutProgressBar.setVisibility(View.VISIBLE);
        rcvAnotherMethods.setVisibility(View.GONE);
        TransitionManager.beginDelayedTransition(_mainViewGroup, new AutoTransition());
    }

    @Override
    public void hideWaitLayout() {
        layoutProgressBar.setVisibility(View.GONE);
        rcvAnotherMethods.setVisibility(View.VISIBLE);
        TransitionManager.beginDelayedTransition(_mainViewGroup, new AutoTransition());
    }

    // #end region

    // region PayAlto
    private void initPayAlto() {
        if (!getMainActivity().getPaymentMethods().isEmpty()) {
            //back from another screen
            listPaymentMethods = getMainActivity().getPaymentMethods();
            finalizeInitialization();
            hideWaitLayout();
        } else {
            //initialize screen
            listPaymentMethods = new ArrayList<>();
            if (request.isBrickEnabled() && request.getBrickRequest().validate()) {
                listPaymentMethods.add(generateCardMethod());
            }
            if (request.isPayAltoEnabled()) {
                PayAltoCore.getInstance().setContext(getActivity());
                showWaitLayout();
                String countryCode = "";
                try {
                    CustomRequest payAltoRequest = (CustomRequest) request.getPayAltoRequest();
                    countryCode = payAltoRequest.get(com.paymentwall.pwunifiedsdk.payalto.utils.Const.P.COUNTRY_CODE);
                } catch (Exception e) {
                    SmartLog.e(e.getMessage());
                }

                pendingApiCalls.incrementAndGet();
                PayAltoCore.getInstance().getPaymentSystems(
                        new PayAltoCore.PaymentSystemsRequest(request.getPwProjectKey(), countryCode), this);
            } else {
                finalizeInitialization();
            }
        }
    }


    @Override
    public void onPaymentSystemsSuccess(List<PayAltoCore.PayAltoMethod> payAltoMethods) {
        listPaymentMethods.addAll(payAltoMethods);
        finalizeInitialization();
    }

    @Override
    public void onPayAltoError(PayAltoCore.PayAltoError error) {
        SmartLog.i("PayAltoError", error.getMessage());
        finalizeInitialization();
    }
    // endregion

    //#region Google Pay integration
    private GPayCheckoutViewModel gPayViewModel;
    private ActivityResultLauncher<Task<PaymentData>> paymentDataLauncher;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        paymentDataLauncher = registerForActivityResult(new TaskResultContracts.GetPaymentDataResult(), result -> {
            int statusCode = result.getStatus().getStatusCode();
            switch (statusCode) {
                case CommonStatusCodes.SUCCESS:
                    handlePaymentSuccess(result.getResult());
                    break;
                case CommonStatusCodes.CANCELED:
                    hideWaitLayout();
                    break;
                case CommonStatusCodes.DEVELOPER_ERROR:
                    handleError(statusCode, result.getStatus().getStatusMessage());
                    break;
                default:
                    handleError(statusCode, "Unexpected non API" +
                            " exception when trying to deliver the task result to an activity!");
                    break;
            }
        });
    }

    private void initGooglePay() {
        if (request.isGooglePayEnable() && gPayViewModel == null) {
            gPayViewModel = new ViewModelProvider(this).get(GPayCheckoutViewModel.class);
            GPayCore.getInstance().setContext(self);
            pendingApiCalls.incrementAndGet();

            GPayCore.getInstance().getGooglePayInstruction(new GPayCore.GPayClickRequest(
                    request.getUserId(),
                    request.getUserEmail(),
                    request.getAmount(),
                    request.getCurrency(),
                    request.getPwProjectKey(),
                    request.getPwSecretKey()
            ), this);
        }
    }

    private void checkGooglePayAvailability() {
        gPayViewModel.canUseGooglePay.observe(getViewLifecycleOwner(), this::finishCheckGooglePayAvailability);
    }

    private void finishCheckGooglePayAvailability(boolean available) {
        finalizeInitialization();
    }

    private void setGooglePayAvailable(boolean available) {
        if (request.isGooglePayEnable() && available) {
            try {
                payButton.initialize(
                        ButtonOptions.newBuilder()
                                .setCornerRadius(16)
                                .setAllowedPaymentMethods(GPayCore.getInstance().getAllowedPaymentMethods().toString()).build()
                );
                payButton.setOnClickListener(this::requestPayment);
            } catch (JSONException e) {
                SmartLog.e(e.getMessage());
                // Keep Google Pay button hidden (consider logging this to your app analytics service)
            }
            payButton.setVisibility(View.VISIBLE);
            setBreakLineVisibility(true);
        } else {
            SmartLog.i("GooglePay is not available");
        }
    }

    private void setBreakLineVisibility(boolean enable) {
        if (enable) {
            tvAnotherMethods.setVisibility(View.VISIBLE);
            viewBreakLineEnd.setVisibility(View.VISIBLE);
            viewBreakLineStart.setVisibility(View.VISIBLE);
        } else {
            tvAnotherMethods.setVisibility(View.GONE);
            viewBreakLineEnd.setVisibility(View.GONE);
            viewBreakLineStart.setVisibility(View.GONE);
        }
    }

    public void requestPayment(View view) {
        showWaitLayout();
        processGooglePayPayment();
    }

    @Override
    public void onClickIdSuccess(String clickId) {
        SmartLog.i("Click ID", clickId);
        checkGooglePayAvailability();
    }

    private void processGooglePayPayment() {
        final Task<PaymentData> task = gPayViewModel.getLoadPaymentDataTask(
                request.getAmount().toString(),
                request.getCurrency());
        task.addOnCompleteListener(paymentDataLauncher::launch);
    }

    // Forwarded from PaymentSelectionActivity
    public void onPaymentResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            PaymentData paymentData = PaymentData.getFromIntent(data);
            handlePaymentSuccess(paymentData);
        } else {
            handleError(resultCode, "Payment failed");
        }
    }

    private void handlePaymentSuccess(PaymentData paymentData) {
        showWaitLayout();
        final String paymentInfo = paymentData.toJson();

        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".

            SmartLog.d("Google Pay token", paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("token"));

            String googlePayToken = paymentMethodData.getJSONObject("tokenizationData").getString("token");

            if (isMerchantHandleGooglePayToken) {
                Intent intent = new Intent();
                intent.putExtra(Const.KEY.GOOGLEPAY_TOKEN, googlePayToken);
                self.setResult(ResponseCode.MERCHANT_PROCESSING, intent);
                self.finish();

                hideWaitLayout();
                return;
            }

            GPayCore.GPayPaymentRequest gRequest = new GPayCore.GPayPaymentRequest(
                    request.getPwProjectKey(),
                    request.getPwSecretKey(),
                    request.getUserId(),
                    googlePayToken,
                    request.getUserEmail()
            );
            GPayCore.getInstance().processGPayPayment(gRequest, this);

        } catch (JSONException e) {
            onGooglePayError(GPayCore.GPayRequestType.PAYMENT, new GPayCore.GooglePayError(e.getMessage(), GPayCore.GooglePayError.Kind.UNEXPECTED));
            SmartLog.e("Error: " + e.getMessage());
        }
    }

    @Override
    public void onPaymentSuccess(String result) {
        hideWaitLayout();
        Intent intent = new Intent();
        intent.putExtra("result", result);
        self.setResult(ResponseCode.SUCCESSFUL, intent);
        self.finish();
    }

    @Override
    public void onGooglePayError(GPayCore.GPayRequestType type, GPayCore.GooglePayError error) {
        SmartLog.e(GPayCore.TAG, error.getMessage());
        if (type == GPayCore.GPayRequestType.INSTRUCTION) {
            gPayViewModel.setCanUseGooglePay(false);
            finalizeInitialization();
        } else {
            Intent intent = new Intent();
            intent.putExtra("error", error.getMessage());
            self.setResult(ResponseCode.ERROR, intent);
            self.finish();
        }
    }

    private void handleError(int statusCode, @Nullable String message) {
        showErrorLayout(message);
        SmartLog.e("loadPaymentData failed",
                String.format(Locale.getDefault(), "Error code: %d, Message: %s", statusCode, message));
    }
    //#endregion

    // region Helper
    // Helper method to finalize initialization after all API calls complete
    private void finalizeInitialization() {
        if (pendingApiCalls.get() > 0) pendingApiCalls.decrementAndGet();
        if (pendingApiCalls.get() == 0) {
            self.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideWaitLayout();
                    if (request.isPayAltoEnabled()) {
                        setRecyclerView();
                    }
                    if (request.isGooglePayEnable() && gPayViewModel != null) {
                        setGooglePayAvailable(Boolean.TRUE.equals(gPayViewModel.canUseGooglePay.getValue()));
                    }
                }
            });
        }
    }

    // endregion
}
