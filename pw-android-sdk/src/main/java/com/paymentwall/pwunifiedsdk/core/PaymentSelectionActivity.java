package com.paymentwall.pwunifiedsdk.core;

import static com.paymentwall.pwunifiedsdk.payalto.utils.Key.CUSTOM_REQUEST_MAP;
import static com.paymentwall.pwunifiedsdk.payalto.utils.Key.PAY_ALTO_REQUEST_MESSAGE;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.paymentwall.pwunifiedsdk.R;
import com.paymentwall.pwunifiedsdk.brick.core.Brick;
import com.paymentwall.pwunifiedsdk.object.ExternalPs;
import com.paymentwall.pwunifiedsdk.payalto.core.PayAltoActivity;
import com.paymentwall.pwunifiedsdk.payalto.core.PayAltoCore;
import com.paymentwall.pwunifiedsdk.payalto.message.CustomRequest;
import com.paymentwall.pwunifiedsdk.payalto.message.LocalDefaultRequest;
import com.paymentwall.pwunifiedsdk.payalto.message.LocalFlexibleRequest;
import com.paymentwall.pwunifiedsdk.payalto.message.LocalRequest;
import com.paymentwall.pwunifiedsdk.payalto.utils.ApiType;
import com.paymentwall.pwunifiedsdk.payalto.utils.PaymentMethod;
import com.paymentwall.pwunifiedsdk.util.Key;
import com.paymentwall.pwunifiedsdk.util.PwUtils;
import com.paymentwall.pwunifiedsdk.util.ResponseCode;
import com.paymentwall.pwunifiedsdk.util.SharedPreferenceManager;
import com.paymentwall.pwunifiedsdk.util.SmartLog;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * Created by nguyen.anh on 7/15/2016.
 */

public class PaymentSelectionActivity extends FragmentActivity {
    public static final int REQUEST_CODE = 0x2505;
    public UnifiedRequest request;
    private LocalRequest message;
    private CustomRequest customParameters;
    private ImageView ivBack, ivHelp;
    private TextView tvActionBarTitle;
    //Dialog
    public boolean isWaitLayoutShowing;
    public boolean isSuccessfulShowing;
    public boolean isUnsuccessfulShowing;
    public static String paymentError = "";
    private WebView webView;
    private final List<ExternalPs> _externalPsList = new ArrayList<>();
    protected Handler handler = new Handler();
    private Stack<Fragment> mStackFragments = new Stack<Fragment>();

    public Bundle bundle;
    private List<PayAltoCore.PayAltoMethod> listPaymentMethod = new ArrayList<>();

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(getPackageName() + Brick.FILTER_BACK_PRESS_ACTIVITY)) {
                backFragment(null);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PwUtils.logFabricCustom("Launch SDK");

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(getPackageName() + Brick.FILTER_BACK_PRESS_ACTIVITY));
        preInitUi();
        acquireMessage();

        final GradientDrawable dialogDrawable = (GradientDrawable) getResources()
                .getDrawable(R.drawable.bgr_successful_dialog);
        dialogDrawable.setColor(PwUtils.getColorFromAttribute(this, "bgNotifyDialog"));

        setBottomSheet();
        handleBackPressed();
    }

    private void setBottomSheet() {
        FrameLayout bottomSheet = findViewById(R.id.main_frame);
        BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setDraggable(false);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        MainFragment.clearInstance();
        BrickNewFragment.clearInstance();
        super.onDestroy();
    }

    protected void preInitUi() {
        // Configure some display const
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void acquireMessage() {
        //determine the number of payment options enabled by merchant
        bundle = getIntent().getExtras();
        if (bundle != null) {
            request = bundle.getParcelable(Key.REQUEST_MESSAGE);
            try {
                message = (LocalRequest) request.getPayAltoRequest();
                customParameters = (CustomRequest) request.getPayAltoRequest();
            } catch (Exception e) {
                SmartLog.e(e.getMessage());
            }

            if (!request.isRequestValid()) {
                Intent intent = new Intent();
                intent.putExtra("error", "invalid request");
                setResult(ResponseCode.ERROR, intent);
                finish();
                return;
            }

            SharedPreferenceManager.getInstance(this).setUIStyle("");
            if (request.getUiStyle() != null)
                SharedPreferenceManager.getInstance(this).setUIStyle(request.getUiStyle());

            if (request.isSelectionSkipped()) {
                if (request.getPayAltoRequest() == null)
                    throw new RuntimeException("You must set PayAltoRequest in UnifiedRequest object");
                if (request.getPsId() == null)
                    throw new RuntimeException("You must provide id for specific payment system");
                payWithPayAlto();
            } else {
                int resID = PwUtils.getLayoutId(this, "activity_payment_selection");
                setContentView(resID);
                initUI();
            }
        }
    }

    private void initUI() {
        ivBack = findViewById(R.id.ivToolbarBack);
        ivBack.setOnClickListener(v -> onBackPressed());
        ivHelp = findViewById(R.id.ivHelp);
        ivHelp.setVisibility(View.GONE);
        ivHelp.setOnClickListener(v -> showFeedbackDialog());
        tvActionBarTitle = findViewById(R.id.tvActionBarTitle);
        PwUtils.setFontBold(this, tvActionBarTitle);

        if (!request.isBrickEnabled() && !request.isMintEnabled() && !request.isMobiamoEnabled()) {
            if (request.isPayAltoEnabled() || !request.getExternalPsList().isEmpty()) {
                replaceContentFragment(MainFragment.getInstance(), bundle);
            } else {
                setResult(ResponseCode.CANCEL);
                finish();
            }
        } else if (request.isBrickEnabled()
                && !request.isMintEnabled()
                && !request.isMobiamoEnabled()
                && !request.isPayAltoEnabled()
                && (request.getExternalPsList() == null || request.getExternalPsList().isEmpty())) {
            payWithBrick();
        } else {
            replaceContentFragment(MainFragment.getInstance(), bundle);
        }

        webView = findViewById(R.id.webView);

        if (isWaitLayoutShowing) {
            showWaitLayout();
        }
        if (isUnsuccessfulShowing) {
            showErrorLayout(paymentError);
        }
        if (isSuccessfulShowing) {
            setResultBack(ResponseCode.SUCCESSFUL);
        }
    }

    private void payWithBrick() {
        if (isSuccessfulShowing) {
            replaceContentFragment(BrickNewFragment.getInstance(), bundle);
        }
        if (request.getBrickRequest().validate()) {
            Bundle bundle = new Bundle();
            bundle.putInt(Key.PAYMENT_TYPE, com.paymentwall.pwunifiedsdk.brick.utils.PaymentMethod.BRICK);
            bundle.putParcelable(Key.REQUEST_MESSAGE, request.getBrickRequest());
            replaceContentFragment(BrickNewFragment.getInstance(), bundle);
        }
    }

    private void payWithPayAlto() {
        SmartLog.i("psa payWithPayAlto");
        if (request.getPayAltoRequest() != null) {
            Intent intent = new Intent(this, PayAltoActivity.class);

            if (request.getPayAltoRequest() instanceof LocalDefaultRequest) {
                intent.putExtra(com.paymentwall.pwunifiedsdk.payalto.utils.Key.PAYMENT_TYPE, PaymentMethod.PW_LOCAL_DEFAULT);
                intent.putExtra(PAY_ALTO_REQUEST_MESSAGE, request.getPayAltoRequest());
            } else if (request.getPayAltoRequest() instanceof LocalFlexibleRequest) {
                intent.putExtra(com.paymentwall.pwunifiedsdk.payalto.utils.Key.PAYMENT_TYPE, PaymentMethod.PW_LOCAL_FLEXIBLE);
                intent.putExtra(PAY_ALTO_REQUEST_MESSAGE, request.getPayAltoRequest());
            } else if (request.getPayAltoRequest() instanceof CustomRequest) {
                intent.putExtra(com.paymentwall.pwunifiedsdk.payalto.utils.Key.CUSTOM_REQUEST_TYPE, ApiType.DIGITAL_GOODS);
                intent.putExtra(CUSTOM_REQUEST_MAP, request.getPayAltoRequest());
            }
            startActivityForResult(intent, PayAltoActivity.REQUEST_CODE);
        } else {
            throw new RuntimeException("You must set payAltoRequest value in unifiedRequest object");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SmartLog.i("psa onActivityResult");
        SmartLog.i("psa onActivityResult requestCode = " + requestCode);
        SmartLog.i("psa onActivityResult resultCode= " + resultCode);
        if (data == null) return;
        if (requestCode == PayAltoActivity.REQUEST_CODE) {
            if (resultCode == ResponseCode.ERROR) {
                setResult(ResponseCode.ERROR, data);
            } else if (resultCode == ResponseCode.FAILED) {
                setResult(ResponseCode.FAILED, data);
                finish();
            } else if (resultCode == ResponseCode.CANCEL) {
                setResult(ResponseCode.CANCEL, data);
                finish();
            } else if (resultCode == ResponseCode.SUCCESSFUL) {
                setResult(ResponseCode.SUCCESSFUL, data);
                finish();
            }
            return;
        }
        if (requestCode == BrickNewFragment.RC_SCAN_CARD && BrickNewFragment.getInstance() != null) {
            BrickNewFragment.getInstance().onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (requestCode == PayAltoActivity.REQUEST_CODE) {
            LocalPsFragment.getInstance().onActivityResult(requestCode, resultCode, data);
            return;
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_frame);
        if (fragment instanceof MainFragment) {
            ((MainFragment) fragment).onPaymentResult(resultCode, data);
            return;
        }

        if (LocalPsFragment.getInstance() != null && data.getExtras() != null) {
            LocalPsFragment.getInstance().onActivityResult(requestCode, resultCode, data);
        }
    }

    public void showWaitLayout() {
        hideKeyboard();
        isWaitLayoutShowing = true;
    }

    public void hideWaitLayout() {
        SmartLog.i("HIDE WAIT", "");
        isWaitLayoutShowing = false;
    }

    public void showErrorLayout(final String error) {
        if (isWaitLayoutShowing) {
            isWaitLayoutShowing = false;
        } else {
            SmartLog.i("SHOW ERROR", error);
            isUnsuccessfulShowing = true;
            if (!error.isEmpty()) {
                paymentError = error;
            }
        }
        showMessageDialog(error, "");
    }

    public void hideErrorLayout() {
        SmartLog.i("HIDE ERROR", "");
        isUnsuccessfulShowing = false;
    }

    public void showMessageDialog(String msg, String title) {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.saas_frag_dialog);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        if (title.isBlank()) {
            title = getString(R.string.payment_unsuccessful);
        }
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        tvTitle.setText(title);

        if (msg.isBlank()) {
            msg = getString(R.string.try_again_message);
        }

        TextView tvMessage = dialog.findViewById(R.id.tvConfirmation);
        tvMessage.setText(msg);
        TextView tvPositive = dialog.findViewById(R.id.tvYes);
        TextView tvNegative = dialog.findViewById(R.id.tvNo);
        tvNegative.setVisibility(View.GONE);
        tvPositive.setText(getString(R.string.text_ok));

        tvPositive.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void showKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager)
                this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            view.requestFocus();
            inputManager.showSoftInput(view,
                    InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void setResultBack(int resultCode) {
        setResult(resultCode);
        finish();
    }

    public void displayPaymentSucceeded() {
        if (isWaitLayoutShowing) {
            isWaitLayoutShowing = false;
            handler.postDelayed(() -> {
                setResult(ResponseCode.SUCCESSFUL);
                finish();
            }, 2000);
            isSuccessfulShowing = true;

        } else {

            handler.postDelayed(() -> {
                setResult(ResponseCode.SUCCESSFUL);
                finish();
            }, 2000);
            isSuccessfulShowing = true;
        }
    }

    public void replaceContentFragment(Fragment fragment, Bundle arguments) {
        replaceContentFragment(fragment, arguments, true);
    }


    public void replaceContentFragment(Fragment fragment,
                                       Bundle arguments, boolean forward) {
        if (fragment != null) {
            Fragment fragmentExists = getFragmentExists(fragment);
            if (fragmentExists != null) {
                fragment = fragmentExists;
                validateStack(fragment);
            }
            pushFragmentonStack(fragment);
            if (fragment.getArguments() != null && arguments != null) {
                fragment.getArguments().putAll(arguments);
            } else if (fragment.getArguments() != null && arguments == null) {
                fragment.getArguments().clear();
            } else {
                fragment.setArguments(arguments);
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            try {
                if (forward)
                    transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.push_left_out);
                else
                    transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.push_right_out);
                transaction.replace(R.id.main_frame, fragment);
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
                transaction.replace(R.id.main_frame, fragment);
                transaction.commit();
            }
            SmartLog.d(this.getClass().getSimpleName(), "stackTabFragments:" + mStackFragments);
        }
    }

    private void validateStack(Fragment fragment) {
        if (mStackFragments != null && mStackFragments.size() > 0) {
            Stack<Fragment> newStack = new Stack<Fragment>();
            for (Fragment fragment2 : mStackFragments) {
                if (fragment2.getClass().getSimpleName()
                        .equals(fragment.getClass().getSimpleName())) {
                    break;
                } else {
                    newStack.add(fragment2);
                }
            }
            mStackFragments = newStack;
            SmartLog.d(this.getClass().getSimpleName(), "validateStack - stackTabFragments:" + mStackFragments);
        }
    }

    private void validateStacks(Fragment fragment) {
        if (mStackFragments != null && mStackFragments.size() > 0) {
            Stack<Fragment> newStack = new Stack<Fragment>();
            for (Fragment fragment2 : mStackFragments) {
                if (fragment2.getClass().getSimpleName().equals(fragment.getClass().getSimpleName())) {
                    break;
                } else {
                    newStack.add(fragment2);
                }
            }
            mStackFragments = newStack;
            SmartLog.d(this.getClass().getSimpleName(), "validateStack - stackTabFragments:" + mStackFragments);
        }
    }

    public Fragment getFragmentExists(final Fragment fragment) {
        if (mStackFragments != null) {
            for (Fragment fragment2 : mStackFragments) {
                if (fragment2.getClass().getSimpleName()
                        .equals(fragment.getClass().getSimpleName())) {
                    return fragment2;
                }
            }
        }
        return null;
    }

    private void pushFragmentonStack(Fragment fragment) {
        if (mStackFragments != null) {
            mStackFragments.push(fragment);
        }
    }

    private void handleBackPressed() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!mStackFragments.isEmpty() && !(mStackFragments.get(mStackFragments.size() - 1) instanceof BaseFragment)) {
                    backFragment(null);
                } else if (webView.getVisibility() == View.VISIBLE) {
                    webView.setVisibility(View.GONE);
                } else {
                    LocalBroadcastManager.getInstance(PaymentSelectionActivity.this).sendBroadcast(
                            new Intent().setAction(getPackageName() + Brick.FILTER_BACK_PRESS_FRAGMENT));
                }
            }
        });
    }

    public void backFragment(Bundle bundle) {
        try {
            if (mStackFragments != null) {
                if (mStackFragments.size() == 1) {
                    setResult(ResponseCode.CANCEL);
                    finish();
                } else {
                    popFragments(bundle);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void popFragments(Bundle bundle) {
        if (mStackFragments != null && mStackFragments.size() > 0) {
            Fragment fragment = mStackFragments.elementAt(mStackFragments
                    .size() - 2);
            /* pop current fragment from stack.. */
            mStackFragments.pop();
            /*
             * We have the target fragment in hand.. Just show it.. Show a
             * standard navigation animation
             */
            replaceContentFragment(fragment, bundle, false);
        } else {
            finish();
        }
    }

    public void clearStackFragment() {
        if (mStackFragments != null) {
            mStackFragments.clear();
        } else {
            mStackFragments = new Stack<Fragment>();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SmartLog.i("On new intent", intent.toString());
        LocalPsFragment.getInstance().onNewIntent(intent);
    }

    private void showFeedbackDialog() {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.saas_dialog_feedback);
        dialog.setCancelable(true);

        dialog.show();
    }

    public void show3dsWebView(String url) {
        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this, "HTMLOUT");
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        CookieManager.getInstance().setAcceptCookie(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                SmartLog.i("URL Redirecting", url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                SmartLog.i("WEB_VIEW_TEST", "onPageStarted" + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                SmartLog.i("REQUEST", request.getRequestHeaders() + "");
                return null;
            }
        });
        webView.loadUrl(url);
    }

    public void hide3dsWebview() {
        runOnUiThread(() -> {
            webView.loadUrl("about:blank");
            webView.setVisibility(View.GONE);
        });
    }

    @JavascriptInterface
    public void processHTML(String html) {
        if (html == null)
            return;
        try {
            Document doc = Jsoup.parse(html);
            Element body = doc.select("body").first();
            SmartLog.i("BODY", body.text());
            if (body.text().equalsIgnoreCase("")) return;

            JSONObject obj = new JSONObject(body.text());
            if (obj.has("object") && obj.getString("object").equalsIgnoreCase("charge")) {
                final String permanentToken = obj.getJSONObject("card").getString("token");
                runOnUiThread(() -> BrickNewFragment.getInstance().onChargeSuccess(permanentToken));
                return;
            }
            if (obj.has("type") && obj.getString("type").equalsIgnoreCase("error")) {
                final String error = obj.getString("error");
                runOnUiThread(() -> BrickNewFragment.getInstance().onChargeFailed(error));
            }

        } catch (Exception e) {
            BrickNewFragment.getInstance().onChargeFailed(getString(R.string.try_again_message));
            SmartLog.e(e.getMessage());
        }
    }

    public List<PayAltoCore.PayAltoMethod> getPaymentMethods() {
        return listPaymentMethod;
    }

    public void setDataPaymentMethod(List<PayAltoCore.PayAltoMethod> methods) {
        this.listPaymentMethod = methods;
    }

    public void onPaymentSuccessful() {
        SmartLog.i("OnPaymentSuccessful", "successful");
        setResult(ResponseCode.SUCCESSFUL);
        finish();
    }

    public void onPaymentError() {
        SmartLog.i("OnPaymentError", "error");
        Toast.makeText(this, "Payment Error", Toast.LENGTH_SHORT).show();
    }

    public void onPaymentCancel() {
        SmartLog.i("OnPaymentCancel", "Cancel");
        Toast.makeText(this, "Payment cancelled", Toast.LENGTH_SHORT).show();
    }
}
