package com.paymentwall.pwunifiedsdk.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.widget.TextView;

import com.paymentwall.pwunifiedsdk.R;
import com.paymentwall.pwunifiedsdk.payalto.utils.PayAltoMiscUtils;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

/**
 * Created by nguyen.anh on 7/15/2016.
 */

public class PwUtils {

    private static Typeface fontBold;
    private static Typeface fontRegular;
    private static Typeface fontLight;

    public static final String HTTP_X_PACKAGE_NAME = "HTTP_X_PACKAGE_NAME";
    public static final String HTTP_X_APP_SIGNATURE = "HTTP_X_APP_SIGNATURE";
    public static final String HTTP_X_VERSION_NAME = "HTTP_X_VERSION_NAME";
    public static final String HTTP_X_PACKAGE_CODE = "HTTP_X_PACKAGE_CODE";
    public static final String HTTP_X_UPDATE_TIME = "HTTP_X_UPDATE_TIME";
    public static final String HTTP_X_INSTALL_TIME = "HTTP_X_INSTALL_TIME";
    public static final String HTTP_X_PERMISSON = "HTTP_X_PERMISSON";
    public static final String HTTP_X_APP_NAME = "X-App-Name";

    public static Typeface getFontBold(Context context) {
        if (fontBold == null)
//            fontBold = Typeface.createFromAsset(context.getAssets(), "fonts/ProximaNova-Bold.otf");
            fontBold = Typeface.DEFAULT_BOLD;
        return fontBold;
    }

    public static Typeface getFontRegular(Context context) {
        if (fontRegular == null)
//            fontRegular = Typeface.createFromAsset(context.getAssets(), "fonts/ProximaNova-Regular.otf");
            fontRegular = Typeface.DEFAULT;
        return fontRegular;
    }

    public static Typeface getFontLight(Context context) {
        if (fontLight == null)
//            fontLight = Typeface.createFromAsset(context.getAssets(), "fonts/ProximaNova-Light.otf");
            fontLight = Typeface.DEFAULT;
        return fontLight;
    }

    public static void setFontRegular(Context context, TextView... textViews) {
        Typeface regularFont = getFontRegular(context);
        for (TextView tv : textViews) {
            if (tv != null)
                tv.setTypeface(regularFont);
        }
    }

    public static void setFontBold(Context context, TextView... textViews) {
        Typeface regularFont = getFontBold(context);
        for (TextView tv : textViews) {
            if (tv != null)
                tv.setTypeface(regularFont);
        }
    }

    public static void setFontLight(Context context, TextView... textViews) {
        Typeface lightFont = getFontLight(context);
        for (TextView tv : textViews) {
            if (tv != null)
                tv.setTypeface(lightFont);
        }
    }

    public static String getApplicationName(Context context) {
        return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
    }

    public static String getCurrencySymbol(String currencyCode) {
        String symbol = CURRENCY_MAP.get(currencyCode);
        if (symbol != null)
            return symbol;
        return currencyCode;
    }

    public static float dpToPx(Context context, float dpValue) {
        float dpFactor = context.getResources().getDisplayMetrics().densityDpi / 160f;
        if (context.getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            return dpValue * dpFactor * 0.95f;
        } else {
            return dpValue * dpFactor;
        }
    }

    public static void logFabricCustom(String event) {

//        try{
//            Answers.getInstance().logCustom(new CustomEvent(event));
//        }catch(Exception e){
//            e.printStackTrace();
//        }
    }

    public static int getLayoutId(Context context, String layoutName) {
        try {
            return context.getResources().getIdentifier(SharedPreferenceManager.getInstance(context).getUIStyle() + "_" + layoutName, "layout", context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return context.getResources().getIdentifier("saas_" + layoutName, "layout", context.getPackageName());
        }
    }

    public static Drawable getDrawableFromAttribute(Context context, String attribute) {
        try {
            int attr = context.getResources().getIdentifier(attribute, "attr", context.getPackageName());
            int[] attrs = new int[]{attr};
            TypedArray ta = context.obtainStyledAttributes(attrs);
            Drawable drawable = ta.getDrawable(0);
            ta.recycle();
            return drawable;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public static int getResouceIdFromAttribute(Context context, String attribute) {
        try {
            int attr = context.getResources().getIdentifier(attribute, "attr", context.getPackageName());
            int[] attrs = new int[]{attr};
            TypedArray ta = context.obtainStyledAttributes(attrs);
            int base = ta.getResourceId(0, R.drawable.local);
            return base;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getColorFromAttribute(Context context, String attribute) {
        try {
            int attr = context.getResources().getIdentifier(attribute, "attr", context.getPackageName());
            int[] attrs = new int[]{attr};
            TypedArray ta = context.obtainStyledAttributes(attrs);
            int color = ta.getColor(0, 0);
            ta.recycle();
            return color;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static HttpsURLConnection addExtraHeaders(Context context, HttpsURLConnection connection) {
        try {
            Context appContext = context.getApplicationContext();
            PackageManager pm = appContext.getPackageManager();
            String packageName = appContext.getPackageName();
            connection.setRequestProperty(HTTP_X_PACKAGE_NAME, packageName);

            ApplicationInfo ai;
            try {
                ai = pm.getApplicationInfo(packageName, 0);
            } catch (final PackageManager.NameNotFoundException e) {
                ai = null;
            }
            String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
            connection.setRequestProperty(HTTP_X_APP_NAME, applicationName);

            PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            StringBuilder stringBuilder = new StringBuilder();
            for (Signature sig : packageInfo.signatures) {
                stringBuilder.append(sig.toChars());
            }

            connection.setRequestProperty(HTTP_X_APP_SIGNATURE, PayAltoMiscUtils.sha256(stringBuilder.toString()));
            connection.setRequestProperty(HTTP_X_VERSION_NAME, packageInfo.versionName);
            connection.setRequestProperty(HTTP_X_PACKAGE_CODE, packageInfo.versionCode + "");
            connection.setRequestProperty(HTTP_X_INSTALL_TIME, packageInfo.firstInstallTime + "");
            connection.setRequestProperty(HTTP_X_UPDATE_TIME, packageInfo.lastUpdateTime + "");
            connection.setRequestProperty(HTTP_X_PERMISSON, getPermissions(context));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static HttpURLConnection addExtraHeaders(Context context, HttpURLConnection connection) {
        try {
            Context appContext = context.getApplicationContext();
            PackageManager pm = appContext.getPackageManager();
            String packageName = appContext.getPackageName();
            connection.setRequestProperty(HTTP_X_PACKAGE_NAME, packageName);

            ApplicationInfo ai;
            try {
                ai = pm.getApplicationInfo(packageName, 0);
            } catch (final PackageManager.NameNotFoundException e) {
                ai = null;
            }
            String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
            connection.setRequestProperty(HTTP_X_APP_NAME, applicationName);

            PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            StringBuilder stringBuilder = new StringBuilder();
            for (Signature sig : packageInfo.signatures) {
                stringBuilder.append(sig.toChars());
            }

            connection.setRequestProperty(HTTP_X_APP_SIGNATURE, PayAltoMiscUtils.sha256(stringBuilder.toString()));
            connection.setRequestProperty(HTTP_X_VERSION_NAME, packageInfo.versionName);
            connection.setRequestProperty(HTTP_X_PACKAGE_CODE, packageInfo.versionCode + "");
            connection.setRequestProperty(HTTP_X_INSTALL_TIME, packageInfo.firstInstallTime + "");
            connection.setRequestProperty(HTTP_X_UPDATE_TIME, packageInfo.lastUpdateTime + "");
            connection.setRequestProperty(HTTP_X_PERMISSON, getPermissions(context));

            return connection;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static String getPermissions(Context context) {
        PackageManager pm = context.getPackageManager();
        StringBuilder builder = new StringBuilder();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : packages) {
//            Log.d("test", "App: " + applicationInfo.name + " Package: " + applicationInfo.packageName);
            try {
                PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS);

                //Get Permissions
                String[] requestedPermissions = packageInfo.requestedPermissions;

                if (requestedPermissions != null) {
                    for (int i = 0; i < requestedPermissions.length; i++) {
                        if (requestedPermissions[i] != null)
                            builder.append(requestedPermissions[i]);
                        if (i < requestedPermissions.length - 1)
                            builder.append(", ");
                    }
//                    Log.i("PERMISSION_LIST", builder.toString());
                    return builder.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static void setCustomAttributes(Context context, View v) {
        final GradientDrawable ovalDrawable = (GradientDrawable) context.getResources()
                .getDrawable(R.drawable.bg_product_info_oval);
        ovalDrawable.setColor(PwUtils.getColorFromAttribute(context, "colorMain"));

        final GradientDrawable ovalDrawableLand = (GradientDrawable) context.getResources()
                .getDrawable(R.drawable.bg_product_info_oval_land);
        ovalDrawableLand.setColor(PwUtils.getColorFromAttribute(context, "mainBackground"));


        final LayerDrawable layerDrawable = (LayerDrawable) context.getResources()
                .getDrawable(R.drawable.bg_product_info);
        GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable
                .findDrawableByLayerId(R.id.bg_product_info_top);
        gradientDrawable.setColor(PwUtils.getColorFromAttribute(context, "bgProductInfo"));
        final View llProductInfo = v.findViewById(R.id.ll_product_info);
        if (llProductInfo != null)
            llProductInfo.post(new Runnable() {
                @Override
                public void run() {
                    int paddingBottom = llProductInfo.getPaddingBottom();
                    int paddingTop = llProductInfo.getPaddingTop();
                    int paddingRight = llProductInfo.getPaddingRight();
                    int paddingLeft = llProductInfo.getPaddingLeft();
                    llProductInfo.setBackgroundDrawable(layerDrawable);
                    llProductInfo.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
                }
            });

        final LayerDrawable layerDrawableLand = (LayerDrawable) context.getResources()
                .getDrawable(R.drawable.bg_product_info_land);
        GradientDrawable gradientDrawableLand = (GradientDrawable) layerDrawableLand
                .findDrawableByLayerId(R.id.bg_product_info_top);
        gradientDrawableLand.setColor(PwUtils.getColorFromAttribute(context, "bgProductInfoLand"));
        final View llProductInfoLand = v.findViewById(R.id.ll_product_info);
        if (llProductInfo != null)
            llProductInfo.post(new Runnable() {
                @Override
                public void run() {
                    int paddingBottom = llProductInfoLand.getPaddingBottom();
                    int paddingTop = llProductInfoLand.getPaddingTop();
                    int paddingRight = llProductInfoLand.getPaddingRight();
                    int paddingLeft = llProductInfoLand.getPaddingLeft();
                    llProductInfoLand.setBackgroundDrawable(layerDrawableLand);
                    llProductInfoLand.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
                }
            });
    }

    public static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public static final Map<String, String> CURRENCY_MAP = new HashMap<String, String>() {
        {
            put("ALL", "Lek");
            put("AFN", "؋");
            put("ARS", "$");
            put("AWG", "ƒ");
            put("AUD", "$");
            put("AZN", "ман");
            put("BSD", "$");
            put("BBD", "$");
            put("BYR", "p.");
            put("BZD", "BZ$");
            put("BMD", "$");
            put("BOB", "$b");
            put("BAM", "KM");
            put("BWP", "P");
            put("BGN", "лв");
            put("BRL", "R$");
            put("BND", "$");
            put("KHR", "៛");
            put("CAD", "$");
            put("KYD", "$");
            put("CLP", "$");
            put("CNY", "¥");
            put("COP", "$");
            put("CRC", "₡");
            put("HRK", "kn");
            put("CUP", "₱");
            put("CZK", "Kč");
            put("DKK", "kr");
            put("DOP", "RD$");
            put("XCD", "$");
            put("EGP", "£");
            put("SVC", "$");
            put("EEK", "kr");
            put("EUR", "€");
            put("FKP", "£");
            put("FJD", "$");

            put("GHC", "¢");
            put("GIP", "£");
            put("GTQ", "Q");
            put("GGP", "£");
            put("GYD", "$");
            put("HNL", "L");
            put("HKD", "$");
            put("HUF", "Ft");
            put("ISK", "kr");
            put("INR", "₹");
            put("IDR", "Rp");
            put("IRR", "﷼");
            put("IMP", "£");
            put("ILS", "₪");
            put("JMD", "J$");
            put("JPY", "¥");
            put("JEP", "£");
            put("KZT", "лв");
            put("KPW", "₩");
            put("KRW", "₩");
            put("KGS", "лв");
            put("LAK", "₭");
            put("LVL", "Ls");
            put("LBP", "£");
            put("LRD", "$");
            put("LTL", "Lt");
            put("MKD", "ден");
            put("MYR", "RM");
            put("MUR", "₨");
            put("MXN", "$");
            put("MNT", "₮");
            put("MZN", "MT");
            put("NAD", "$");
            put("NPR", "₨");
            put("ANG", "ƒ");
            put("NZD", "$");
            put("NIO", "C$");
            put("NGN", "₦");
            put("NOK", "kr");
            put("OMR", "﷼");
            put("PKR", "₨");
            put("PAB", "B/.");
            put("PYG", "Gs");
            put("PEN", "S/.");
            put("PHP", "₱");
            put("PLN", "zł");
            put("QAR", "﷼");
            put("RON", "lei");
            put("RUB", "руб");

            put("SHP", "£");
            put("SAR", "﷼");
            put("RSD", "Дин.");
            put("SCR", "₨");
            put("SGD", "$");
            put("SBD", "$");
            put("SOS", "S");
            put("ZAR", "S");
            put("LKR", "₨");
            put("SEK", "kr");
            put("CHF", "CHF");
            put("SRD", "$");
            put("SYP", "£");
            put("TWD", "NT$");
            put("THB", "฿");
            put("TTD", "TT$");
            put("TRL", "₤");
            put("TVD", "$");
            put("UAH", "₴");
            put("GBP", "£");
            put("USD", "$");
            put("UYU", "$U");
            put("UZS", "лв");
            put("VEF", "Bs");
            put("VND", "₫");
            put("YER", "﷼");
            put("ZWD", "Z$");

        }
    };

}
