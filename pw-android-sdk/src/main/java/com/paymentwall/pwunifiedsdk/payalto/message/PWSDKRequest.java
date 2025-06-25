package com.paymentwall.pwunifiedsdk.payalto.message;

import android.text.TextUtils;

import com.paymentwall.pwunifiedsdk.payalto.utils.Const.P;
import com.paymentwall.pwunifiedsdk.payalto.utils.PayAltoMiscUtils;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

abstract class PWSDKRequest implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 3890965502865690245L;
    private static final String TAG = "PWSDKRequest";
    protected String key;
    protected String sign;
    protected Integer signVersion = 0;
    protected Long timeStamp;
    protected String uid;
    protected String secretKey;
    protected boolean autoSigned = false;

    protected TreeMap<String, String> parameters = new TreeMap<String, String>();

    /*public abstract String generateParameterString(int signatureVersion);

    public abstract String signatureCalculate();

    public abstract String signatureCalculate(int version);*/
    public String getUrl(String rootUrl, String secret, int version) {
        if (secret != null && secret.length() > 0 && (version == 2 || version == 3)) {
            return rootUrl + generateUrlParam(parameters, secret, version);
        } else {
            return rootUrl + generateUrlParam(parameters, "", 0);
        }
    }

    public String getUrlNoSign(String rootUrl) {
        return rootUrl + generateUrlParam(parameters);
    }

    public String getUrl(String rootUrl) {
        if (!autoSigned) {
            return getUrlNoSign(rootUrl);
        } else if (sign == null) {
            return getUrl(rootUrl, this.secretKey, this.signVersion);
        } else {
            return getUrl(rootUrl, "", 0);
        }
    }

    public static String signatureCalculate(Map<String, String> params, String secret, int version) {
        if (version == 2 || version == 3) {
            StringBuilder stringBuilder = new StringBuilder();
//            String joinedString = "";
            if (!(params instanceof TreeMap<?, ?>)) {
                params = PayAltoMiscUtils.sortMap(params);
            }
            for (Entry<String, String> entry : params.entrySet()) {
//                joinedString = joinedString + entry.getKey() + "=" + entry.getValue();
                stringBuilder.append(entry.getKey());
                stringBuilder.append('=');
                stringBuilder.append(entry.getValue());
            }
            stringBuilder.append(secret);
            if (version == 2) return PayAltoMiscUtils.md5(stringBuilder.toString());
            else return PayAltoMiscUtils.sha256(stringBuilder.toString());
        } else return "";
    }

    public static String generateUrlParam(Map<String, String> params) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append('?');
        final Set<Entry<String, String>> entrySet = params.entrySet();
        Iterator<Entry<String, String>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Entry<String, String> entry = iterator.next();
            if (!TextUtils.isEmpty(entry.getKey()) && !TextUtils.isEmpty(entry.getValue())) {
                urlBuilder.append(PayAltoMiscUtils.urlStringEncode(entry.getKey()));
                urlBuilder.append('=');
                urlBuilder.append(PayAltoMiscUtils.urlStringEncode(entry.getValue()));
                if (iterator.hasNext()) {
                    urlBuilder.append('&');
                }
            }
        }
        return urlBuilder.toString();
    }

    public static String generateUrlParam(Map<String, String> params, String secret, int version) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append('?');
        final Set<Entry<String, String>> entrySet = params.entrySet();
        Iterator<Entry<String, String>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Entry<String, String> entry = iterator.next();
            urlBuilder.append(PayAltoMiscUtils.urlStringEncode(entry.getKey()));
            urlBuilder.append('=');
            urlBuilder.append(PayAltoMiscUtils.urlStringEncode(entry.getValue()));
            if (iterator.hasNext()) {
                urlBuilder.append('&');
            }
        }
        if (version == 2 || version == 3) {
            urlBuilder.append('&');
            urlBuilder.append(P.SIGN);
            urlBuilder.append('=');
            urlBuilder.append(signatureCalculate(params, secret, version));
        }

        return urlBuilder.toString();
    }


//    public abstract boolean validateData();

    public Integer getSignVersion() {
        return signVersion;
    }

    public void setSignVersion(Integer signVersion) {
        addParameter(P.SIGN_VERSION, signVersion.toString());
        this.signVersion = signVersion;

    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        autoSigned = (secretKey != null && secretKey.length() > 0);

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        addParameter(P.KEY, key);
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
        addParameter(P.SIGN, sign);
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        addParameter(P.TS, timeStamp.toString());
        this.timeStamp = timeStamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        addParameter(P.UID, uid);
        this.uid = uid;
    }

    public TreeMap<String, String> getParameter() {
        return parameters;
    }

    public void setParameter(TreeMap<String, String> parameter) {
        this.parameters = parameter;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PWSDKRequest{");
        sb.append("key='").append(key).append('\'');
        sb.append(", sign='").append(sign).append('\'');
        sb.append(", signVersion=").append(signVersion);
        sb.append(", timeStamp=").append(timeStamp);
        sb.append(", uid='").append(uid).append('\'');
        sb.append(", parameters=").append(parameters);
        sb.append('}');
        return sb.toString();
    }

    public void addParameter(String key, String value) {
        if (value != null && key != null) {
            if (parameters == null) parameters = new TreeMap<String, String>();
            parameters.put(key, value);
        }
    }

    public void removeParameter(String key) {
        if (parameters != null) parameters.remove(key);
    }

    public String findParameter(String key) {
        if (parameters != null) return parameters.get(key);
        else return null;
    }

    public boolean isAutoSigned() {
        return autoSigned;
    }

    public void setAutoSigned(boolean autoSigned) {
        this.autoSigned = autoSigned;
    }
}
