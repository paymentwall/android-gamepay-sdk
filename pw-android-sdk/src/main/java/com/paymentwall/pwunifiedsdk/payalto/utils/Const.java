package com.paymentwall.pwunifiedsdk.payalto.utils;

public class Const {
    public static final String SDK_MESSAGE = "sdk_message";
    //	public static final String DEFAULT_SUCCESS_URL = "http://example.com/?ref=$ref";
    public static final String DEFAULT_SUCCESS_URL = "pwlocal://paymentsuccessful";

    public static final String USER_AGENT_VERSION = "X-PW-SDK_v1.1.0";

    public static final class PW_LOCAL_REQUEST_CODE {
        public static final int NULL = -1;
        public static final int DEFAULT = 0;
        public static final int FLEXIBLE = 1;
    }

    public static final class PW_URL {
        public static final String PS = "https://api.paymentwall.com/api/ps/";
        public static final String SUBSCRIPTION = "https://api.paymentwall.com/api/subscription/";
        public static final String CART = "https://api.paymentwall.com/api/cart/";
//		public static final String PS = "http://feature-id-151.wallapi.bamboo.stuffio.com/api/ps/";
//		public static final String SUBSCRIPTION = "http://feature-id-151.wallapi.bamboo.stuffio.com/api/subscription/";
//		public static final String CART = "http://feature-id-151.wallapi.bamboo.stuffio.com/api/cart/";
    }

    public static final class PW_LOCAL_WIDGET_TYPE {
        public static final int DEFAULT = 0;
        public static final int FLEXIBLE = 1;
    }

    public static final class GENDER {
        public static final int MALE = 1;
        public static final int FEMALE = 0;
    }

    public static final class P {
        //		public static final String HISTORY_MOBILE_PACKAGE_SIGNATURE = "history[mobile_package_signature]";
        public static final String HISTORY_MOBILE_PACKAGE_NAME = "HTTP_X_REQUESTED_WITH";
        public static final String HISTORY_MOBILE_APP_NAME = "HTTP_X_APP_NAME";
        //		public static final String HISTORY_MOBILE_APP_VERSION = "history[mobile_app_version]";
        public static final String HISTORY_MOBILE_DOWNLOAD_LINK = "HTTP_X_DOWNLOAD_LINK";
        public static final String BIRTHDAY = "birthday";
        public static final String COUNTRY_CODE = "country_code";
        public static final String DEFAULT_GOODSID = "default_goodsid";
        public static final String DISPLAY_GOODSID = "display_goodsid";
        public static final String EMAIL = "email";
        public static final String EVALUATION = "evaluation";
        public static final String FIRSTNAME = "firstname";
        public static final String HIDE_GOODSID = "hide_goodsid";
        public static final String KEY = "key";
        public static final String LANG = "lang";
        public static final String LASTNAME = "lastname";
        public static final String LOCATION_ADDRESS = "location[address]";
        public static final String LOCATION_CITY = "location[city]";
        public static final String LOCATION_COUNTRY = "location[country]";
        public static final String LOCATION_STATE = "location[state]";
        public static final String LOCATION_ZIP = "location[zip]";
        public static final String PINGBACK_URL = "pingback_url";
        public static final String PS = "ps";
        public static final String SEX = "sex";
        public static final String SIGN = "sign";
        public static final String SIGN_VERSION = "sign_version";
        public static final String SUCCESS_URL = "success_url";
        public static final String TS = "ts";
        public static final String UID = "uid";
        public static final String WIDGET = "widget";
        public static final String AG_EXTERNAL_ID = "ag_external_id";
        public static final String AG_NAME = "ag_name";
        public static final String AG_PERIOD_LENGTH = "ag_period_length";
        public static final String AG_PERIOD_TYPE = "ag_period_type";
        public static final String AG_POST_TRIAL_EXTERNAL_ID = "ag_post_trial_external_id";
        public static final String AG_POST_TRIAL_NAME = "ag_post_trial_name";
        public static final String AG_POST_TRIAL_PERIOD_LENGTH = "ag_post_trial_period_length";
        public static final String AG_POST_TRIAL_PERIOD_TYPE = "ag_post_trial_period_type";
        public static final String AG_PROMO = "ag_promo";
        public static final String AG_RECURRING = "ag_recurring";
        public static final String AG_TRIAL = "ag_trial";
        public static final String AG_TYPE = "ag_type";
        public static final String AMOUNT = "amount";
        public static final String VERSION = "version";
        public static final String CURRENCYCODE = "currencyCode";
        public static final String HIDE_POST_TRIAL_GOOD = "hide_post_trial_good";
        public static final String POST_TRIAL_AMOUNT = "post_trial_amount";
        public static final String POST_TRIAL_CURRENCYCODE = "post_trial_currencyCode";
        public static final String SHOW_POST_TRIAL_NON_RECURRING = "show_post_trial_non_recurring";
        public static final String SHOW_POST_TRIAL_RECURRING = "show_post_trial_recurring";
        public static final String SHOW_TRIAL_NON_RECURRING = "show_trial_non_recurring";
        public static final String SHOW_TRIAL_RECURRING = "show_trial_recurring";
        public static final String PRODUCT_NAME = "product_name";
        public static final String PRODUCT_ID = "product_id";
        public static final String PRICE_ID = "price_id";
        public static final String CURRENCY = "currency";
        public static final String COUNTRY = "country";
        public static final String MNC = "mnc";
        public static final String MCC = "mcc";
        public static final String EXTERNAL_IDS = "external_ids";
        public static final String CURRENCIES = "currencies";
        public static final String PRICES = "prices";
        public static final String REF = "ref";

        public static String i(Integer indicator) {
            if (indicator != null) {
                return "[" + indicator + "]";
            } else {
                return "";
            }
        }
    }

    // ISO 639-1 languages
    public static final class LANGUAGE {
        public static final String ENGLISH = "en";
        public static final String FRENCH = "fr";
        public static final String GERMAN = "de";
        public static final String SPANISH = "es";
        public static final String ITALIAN = "it";
        public static final String PORTUGUESE = "pt";
        public static final String PORTUGUESE_BR = "pt_BR";
        public static final String TURKISH = "tr";
        public static final String SWEDISH = "sv";
        public static final String JAPANESE = "ja";
        public static final String FINNISH = "fi";
        public static final String RUSSIAN = "ru";
        public static final String UKRAINIAN = "uk";
        public static final String POLISH = "pl";
        public static final String GREEK = "el";
        public static final String CHINESE_TRADITIONAL = "zh_TW";
        public static final String CHINESE_SIMPLIFIED = "zh_CN";
        public static final String VIETNAMESE = "vi";
        public static final String HINDI = "hi";
        public static final String KOREAN = "ko";
        public static final String TAGALOG = "tl";
        public static final String THAI = "th";
        public static final String CROATIAN = "hr";
        public static final String LITHUANIAN = "lt";
        public static final String SLOVENIAN = "sl";
        public static final String SERBIAN = "sr";
        public static final String BULGARIAN = "bg";
        public static final String DUTCH = "nl";

    }
}
