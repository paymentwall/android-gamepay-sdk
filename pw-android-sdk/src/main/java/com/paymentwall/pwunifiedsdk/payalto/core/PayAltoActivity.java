package com.paymentwall.pwunifiedsdk.payalto.core;

import static com.paymentwall.pwunifiedsdk.util.PwUtils.HTTP_X_APP_NAME;
import static com.paymentwall.pwunifiedsdk.util.PwUtils.HTTP_X_APP_SIGNATURE;
import static com.paymentwall.pwunifiedsdk.util.PwUtils.HTTP_X_INSTALL_TIME;
import static com.paymentwall.pwunifiedsdk.util.PwUtils.HTTP_X_PACKAGE_CODE;
import static com.paymentwall.pwunifiedsdk.util.PwUtils.HTTP_X_PACKAGE_NAME;
import static com.paymentwall.pwunifiedsdk.util.PwUtils.HTTP_X_PERMISSON;
import static com.paymentwall.pwunifiedsdk.util.PwUtils.HTTP_X_UPDATE_TIME;
import static com.paymentwall.pwunifiedsdk.util.PwUtils.HTTP_X_VERSION_NAME;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.paymentwall.pwunifiedsdk.R;
import com.paymentwall.pwunifiedsdk.payalto.message.CustomRequest;
import com.paymentwall.pwunifiedsdk.payalto.message.LocalRequest;
import com.paymentwall.pwunifiedsdk.payalto.ui.JSDialog;
import com.paymentwall.pwunifiedsdk.payalto.ui.ProgressWheel;
import com.paymentwall.pwunifiedsdk.payalto.utils.ApiType;
import com.paymentwall.pwunifiedsdk.payalto.utils.Const;
import com.paymentwall.pwunifiedsdk.payalto.utils.Key;
import com.paymentwall.pwunifiedsdk.payalto.utils.PayAltoMiscUtils;
import com.paymentwall.pwunifiedsdk.payalto.utils.PaymentMethod;
import com.paymentwall.pwunifiedsdk.payalto.utils.ResponseCode;
import com.paymentwall.pwunifiedsdk.util.MiscUtils;
import com.paymentwall.pwunifiedsdk.util.PwUtils;
import com.paymentwall.pwunifiedsdk.util.SmartLog;

import java.util.Map;
import java.util.TreeMap;

public class PayAltoActivity extends FragmentActivity implements
        JSDialog.SuccessUrlListener {
    public static final String TAG_WEB_DIALOG = " WebDialog";
    public static final int REQUEST_CODE = 0x8087;

    View backbutton;
    WebView webView;
    TextView screenTitle;
    ProgressWheel progressWheel;
    FrameLayout progressWheelContainer;

    private String url;
    private LocalRequest message;
    private CustomRequest customParameters;
    private String successfulUrl = Const.DEFAULT_SUCCESS_URL;
    private final boolean customRequestMode = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_pay_alto_activity);
        SmartLog.i("New PayAltoActivity");
        webView = findViewById(R.id.payAltoWebView);
        screenTitle = findViewById(R.id.tvTittle);
        backbutton = findViewById(R.id.imgBackButton);
        progressWheel = findViewById(R.id.pbLoadingWheel);
        progressWheelContainer = findViewById(R.id.frameLoading);
        progressWheel.setCircleRadius(getResources().getDimensionPixelSize(R.dimen.pwl_wheel_radius));
        progressWheel.setBarWidth(getResources().getDimensionPixelSize(R.dimen.pwl_wheel_bar_width));
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initWebView();
        acquireMessage();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState == null) outState = new Bundle();
        if (webView != null) webView.saveState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (webView != null && savedInstanceState != null) webView.restoreState(savedInstanceState);
    }

    private void acquireMessage() {
        Map<String, String> extraHeaders = getAppParametersFull(this);
        Bundle extras = getIntent().getExtras();

        if (getIntent() == null || extras == null) {
            Intent result = new Intent();
            result.putExtra(Key.SDK_ERROR_MESSAGE, "NULL REQUEST_TYPE");
            errorRespond(result);
            return;
        }

        if (extras.containsKey(Key.CUSTOM_REQUEST_MAP) && extras.containsKey(Key.CUSTOM_REQUEST_TYPE)) {
            try {
                customParameters = extras.getParcelable(Key.CUSTOM_REQUEST_MAP);
                if (customParameters.containsKey(Const.P.SUCCESS_URL)) {
                    successfulUrl = customParameters.get(Const.P.SUCCESS_URL);
                } else {
                    customParameters.put(Const.P.SUCCESS_URL, successfulUrl);
                }
                String customRequestType = extras.getString(Key.CUSTOM_REQUEST_TYPE);
                String rootUrl;
                if (customRequestType.equals(ApiType.VIRTUAL_CURRENCY)) {
                    rootUrl = Const.PW_URL.PS;
                    rootUrl = PayAltoCore.PAY_ALTO_PS;
                } else if (customRequestType.equals(ApiType.CART)) {
                    rootUrl = Const.PW_URL.CART;
                    rootUrl = PayAltoCore.PAY_ALTO_CART;
                } else if (customRequestType.equals(ApiType.DIGITAL_GOODS)) {
                    rootUrl = Const.PW_URL.SUBSCRIPTION;
                    rootUrl = PayAltoCore.PAY_ALTO_SUBSCRIPTION;
                } else {
                    Intent result = new Intent();
                    result.putExtra(Key.SDK_ERROR_MESSAGE, "MESSAGE ERROR");
                    errorRespond(result);
                    return;
                }

                String query = customParameters.getUrlParam();
                url = rootUrl + query;
                SmartLog.d("Query: " + query);
                SmartLog.d("URL: " + url);
//                url = "https://api.paymentwall.com/v1/checkout/orders/?key=02e68a437754e67f46269bd6597ef5ba&uid=saulnara0916&widget=pw_7&amount=5&currencyCode=USD&ag_name=Test+Product&ag_type=fixed&ag_external_id=1&country_code=VN&ps=banktransfervn&sign_version=2&sign=8aa06558d9f7b48f4aa7e9a0230ae9f5";
//                url = "https://api.paymentwall.com/v1/checkout/orders/?key=02e68a437754e67f46269bd6597ef5ba&uid=saulnara0916&widget=pw_7&amount=5&currencyCode=USD&ag_name=Test+Product&ag_type=fixed&ag_external_id=1&country_code=VN&sign_version=2&sign=61632b07308e7fc713f729db39bc8421";
                if (webView != null) {
                    if (customParameters.getMobileDownloadLink() != null)
                        extraHeaders.put(Const.P.HISTORY_MOBILE_DOWNLOAD_LINK, customParameters.getMobileDownloadLink());
                    webView.loadUrl(url, extraHeaders);
                }

                String title = customParameters.get(ApiType.PS_NAME);
                screenTitle.setText(title);
            } catch (Exception e) {
                e.printStackTrace();
                Intent result = new Intent();
                result.putExtra(Key.SDK_ERROR_MESSAGE, "MESSAGE ERROR");
                errorRespond(result);
            }
        } else if ((getIntent().hasExtra(Key.PAY_ALTO_REQUEST_MESSAGE) || getIntent().hasExtra(Key.REQUEST_MESSAGE))
                && extras.containsKey(Key.PAYMENT_TYPE)) {
            int requestType = extras.getInt(Key.PAYMENT_TYPE, PaymentMethod.NULL);
            if (getIntent().hasExtra(Key.PAY_ALTO_REQUEST_MESSAGE)) {
                message = getIntent().getParcelableExtra(Key.PAY_ALTO_REQUEST_MESSAGE);
            } else if (getIntent().hasExtra(Key.REQUEST_MESSAGE)) {
                message = (LocalRequest) getIntent().getSerializableExtra(
                        Key.REQUEST_MESSAGE);
            }

            if (message == null || requestType == PaymentMethod.NULL) {
                Intent result = new Intent();
                result.putExtra(Key.SDK_ERROR_MESSAGE, "MESSAGE ERROR");
                errorRespond(result);
                return;
            }

            try {
                final String rootUrl;
                if (message.getApiType().equalsIgnoreCase(ApiType.VIRTUAL_CURRENCY)) {
                    rootUrl = Const.PW_URL.PS;
                } else if (message.getApiType().equalsIgnoreCase(ApiType.DIGITAL_GOODS)) {
                    rootUrl = Const.PW_URL.SUBSCRIPTION;
                } else if (message.getApiType().equalsIgnoreCase(ApiType.CART)) {
                    rootUrl = Const.PW_URL.CART;
                } else {
                    throw new Exception("Invalid Paymentwall API type");
                }

                this.url = message.getUrl(rootUrl);
                if (webView != null) {
                    if (message.getMobileDownloadLink() != null)
                        extraHeaders.put(Const.P.HISTORY_MOBILE_DOWNLOAD_LINK, message.getMobileDownloadLink());

                    webView.loadUrl(url, extraHeaders);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Intent result = new Intent();
                result.putExtra(Key.SDK_ERROR_MESSAGE, "MESSAGE ERROR " + e.getMessage());
                errorRespond(result);
            }

        } else {
            Intent result = new Intent();
            result.putExtra(Key.SDK_ERROR_MESSAGE, "MESSAGE ERROR");
            errorRespond(result);
        }

    }

    private void successRespond(Intent intent) {
//        SmartLog.i("successRespond");
        if (message != null) {
            intent.putExtra(Key.PAY_ALTO_REQUEST_MESSAGE, (Parcelable) message);
        } else if (customParameters != null) {
            intent.putExtra(Key.CUSTOM_REQUEST_MAP, customParameters);
        }
        setResult(ResponseCode.SUCCESSFUL, intent);
        finish();
    }

    private void successRespond() {
        Intent intent = new Intent();
        if (message != null) {
            intent.putExtra(Key.PAY_ALTO_REQUEST_MESSAGE, (Parcelable) message);
        } else if (customParameters != null) {
            intent.putExtra(Key.CUSTOM_REQUEST_MAP, customParameters);
        }
        successRespond(intent);
    }

    private void errorRespond(Intent intent) {
        if (message == null) {
            if (getIntent().hasExtra(Key.PAY_ALTO_REQUEST_MESSAGE)) {
                message = getIntent().getParcelableExtra(Key.PAY_ALTO_REQUEST_MESSAGE);
            } else if (getIntent().hasExtra(Key.REQUEST_MESSAGE)) {
                message = (LocalRequest) getIntent().getSerializableExtra(
                        Key.REQUEST_MESSAGE);
            }
        }

        if (customParameters == null) {
            if (getIntent().hasExtra(Key.CUSTOM_REQUEST_MAP)) {
                customParameters = getIntent().getParcelableExtra(Key.CUSTOM_REQUEST_MAP);
            }
        }

        if (message != null) {
            intent.putExtra(Key.PAY_ALTO_REQUEST_MESSAGE, (Parcelable) message);
        } else if (customParameters != null) {
            intent.putExtra(Key.CUSTOM_REQUEST_MAP, customParameters);
        }

        setResult(ResponseCode.ERROR, intent);
        finish();
    }

    private void cancelRespond() {
        Intent intent = new Intent();
        if (message == null) {
            if (getIntent().hasExtra(Key.PAY_ALTO_REQUEST_MESSAGE)) {
                message = getIntent().getParcelableExtra(Key.PAY_ALTO_REQUEST_MESSAGE);
            } else if (getIntent().hasExtra(Key.REQUEST_MESSAGE)) {
                message = (LocalRequest) getIntent().getSerializableExtra(
                        Key.REQUEST_MESSAGE);
            }
        }

        if (customParameters == null) {
            if (getIntent().hasExtra(Key.CUSTOM_REQUEST_MAP)) {
                customParameters = getIntent().getParcelableExtra(Key.CUSTOM_REQUEST_MAP);
            }
        }

        if (message != null) {
            intent.putExtra(Key.PAY_ALTO_REQUEST_MESSAGE, (Parcelable) message);
        } else if (customParameters != null) {
            intent.putExtra(Key.CUSTOM_REQUEST_MAP, customParameters);
        }

        setResult(ResponseCode.CANCEL, intent);
        finish();
    }


    @Override
    public void onBackPressed() {
//        cancelRespond();
        super.onBackPressed();
    }

    public static Map<String, String> getAppParametersFull(Context context) {
        TreeMap<String, String> headers = new TreeMap<>();
        try {
            Context appContext = context.getApplicationContext();
            PackageManager pm = appContext.getPackageManager();
            String packageName = appContext.getPackageName();
            headers.put(HTTP_X_PACKAGE_NAME, packageName);

            ApplicationInfo ai;
            try {
                ai = pm.getApplicationInfo(packageName, 0);
            } catch (final PackageManager.NameNotFoundException e) {
                ai = null;
            }
            String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
            headers.put(HTTP_X_APP_NAME, applicationName);

            PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            StringBuilder stringBuilder = new StringBuilder();
            for (Signature sig : packageInfo.signatures) {
                stringBuilder.append(sig.toChars());
            }

            headers.put(HTTP_X_APP_SIGNATURE, PayAltoMiscUtils.sha256(stringBuilder.toString()));
            headers.put(HTTP_X_VERSION_NAME, packageInfo.versionName);
            headers.put(HTTP_X_PACKAGE_CODE, packageInfo.versionCode + "");
            headers.put(HTTP_X_INSTALL_TIME, packageInfo.firstInstallTime + "");
            headers.put(HTTP_X_UPDATE_TIME, packageInfo.lastUpdateTime + "");
            headers.put(HTTP_X_PERMISSON, PwUtils.getPermissions(context));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return headers;
    }

    private void initWebView() {
        if (webView == null) return;
        String currentAgent = webView.getSettings().getUserAgentString();
        String appAgent = currentAgent + "{" + Const.USER_AGENT_VERSION + "}";
        Log.e("User_agent", appAgent);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setUserAgentString(appAgent);
        webView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(SmartLog.DEBUG);
        }
        MiscUtils.removeJsInterface(webView);
        CookieManager.getInstance().setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            compatSetAccept3rdPartyCookie(webView, true);
        }
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                SmartLog.i("WEB_VIEW_TEST", "Start JSDialog " + resultMsg.toString());
                JSDialog webviewDialogFragment = JSDialog.newInstance(resultMsg, successfulUrl);
                webviewDialogFragment.setSuccessUrlListener(PayAltoActivity.this);
                webviewDialogFragment.show(getSupportFragmentManager(), TAG_WEB_DIALOG);
                return true;
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                SmartLog.i("WEB_VIEW_TEST", "onLoadResource: " + url);
                super.onLoadResource(view, url);
                onLoadWebViewResource(url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                SmartLog.i("WEB_VIEW_TEST", "onPageStarted: " + url);
                super.onPageStarted(view, url, favicon);
                onLoading();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                SmartLog.i("WEB_VIEW_TEST", "onPageFinished: " + url);
                super.onPageFinished(view, url);
                onHideLoading();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                SmartLog.i("WEB_VIEW_TEST", "error code: " + view.getUrl());
                onOpenedLinkSuccess(view.getUrl());
                return super.shouldOverrideUrlLoading(view, request);

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                SmartLog.i("WEB_VIEW_TEST", "error code: " + error.getDescription());
                super.onReceivedError(view, request, error);
                onReceivedWebViewError(view.getUrl());
            }
        });
    }

    @RequiresApi(21)
    private static void compatSetAccept3rdPartyCookie(WebView webView, boolean accepted) {
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, accepted);
    }

    private void startLoading() {
        if (progressWheel != null) {
            progressWheelContainer.setVisibility(View.VISIBLE);
            progressWheel.setVisibility(View.VISIBLE);
            progressWheel.spin();
        }
    }

    private void stopLoading() {
        if (progressWheel != null) {
            progressWheel.stopSpinning();
            progressWheel.setVisibility(View.GONE);
            progressWheelContainer.setVisibility(View.GONE);
        }
    }

    public void onPayAltoCallback() {
        successRespond();
    }

    private boolean shouldOpenInApp(String url) {
        return (url != null && (url.startsWith("http") || url.startsWith("https")));
    }

    @Override
    public void onOpenedLinkSuccess(String url) {
        if (MiscUtils.isSuccessfulUrl(url, successfulUrl)) {
            onPayAltoCallback();
        } else if (shouldOpenInApp(url)) {
//            webView.loadUrl(url);
        } else {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                webView.loadUrl(url);
            }
        }
    }

    @Override
    public void onLoading() {
        startLoading();
    }

    @Override
    public void onHideLoading() {
        stopLoading();
    }

    @Override
    public void onReceivedWebViewError(String failingUrl) {
        if (MiscUtils.isFasterpayLink(failingUrl)) {
        }
//        if (!MiscUtils.isSuccessfulUrl(failingUrl, successfulUrl)) {
//            Toast.makeText(PayAltoActivity.this, getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
//            Intent intent = new Intent();
//            intent.putExtra(Key.SDK_ERROR_MESSAGE, "CONNECTION ERROR");
//            errorRespond(intent);
//        }
    }

    @Override
    public void onLoadWebViewResource(String url) {
        if (MiscUtils.isSuccessfulUrl(url, successfulUrl)) {
            onPayAltoCallback();
        }
    }

    @Override
    public void onAppNotFound() {
        Toast.makeText(PayAltoActivity.this, getString(R.string.app_not_found), Toast.LENGTH_LONG).show();
    }
}
