package com.paymentwall.pwunifiedsdk.mobiamo.payment;

import com.paymentwall.pwunifiedsdk.mobiamo.utils.Const;
import com.paymentwall.pwunifiedsdk.util.MiscUtils;
import com.paymentwall.pwunifiedsdk.util.SmartLog;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public abstract class PWSDKRequest implements Serializable {
    private static final long serialVersionUID = 3126605186188419173L;
    private static final String TAG = "PWSDKRequest";
    protected String key;
    protected String sign;
    protected static Integer signVersion;
    protected Long timeStamp;
    protected String uid;
    protected String urlParameters;
    protected String secretKey;

    TreeMap<String, String> parameters = new TreeMap<String, String>();

    public abstract String generateParameterString(int signatureVersion);

    public abstract String signatureCalculate();

    public abstract String signatureCalculate(int version);

    public String getUrl(String rootUrl, String secret, int version) {
        if (secret != null && secret.length() > 0
                && (version == 2 || version == 3)) {
            return rootUrl + generateUrlParam(parameters, secret, version);
        } else {
            return rootUrl + generateUrlParam(parameters, "", 0);
        }
    }

    public String getUrl(String rootUrl) {
        return getUrl(rootUrl, this.secretKey, PWSDKRequest.signVersion);
    }

    public static String signatureCalculate(Map<String, String> params,
                                            String secret, int version) {
        if (version == 2 || version == 3) {
            String joinedString = "";
            if (!(params instanceof TreeMap<?, ?>)) {
                params = MiscUtils.sortMap(params);
            }
            for (Entry<String, String> entry : params.entrySet()) {
                joinedString = joinedString + entry.getKey() + "="
                        + entry.getValue();
            }
            joinedString = joinedString + secret;
            if (version == 2)
                return MiscUtils.md5(joinedString);
            else
                return MiscUtils.sha256(joinedString);
        } else
            return "";
    }

    public static String signatureCalculateValueOnly(Map<String, String> params,
                                                     String secret, int version) {
        if (version == 2 || version == 3) {
            String joinedString = "";
            if (!(params instanceof TreeMap<?, ?>)) {
                params = MiscUtils.sortMap(params);
            }
            for (Entry<String, String> entry : params.entrySet()) {
                joinedString = joinedString + entry.getValue();
            }
            joinedString = joinedString + secret;
//            SmartLog.i("SIGN: joinedString", joinedString);
            if (version == 2)
                return MiscUtils.md5(joinedString);
            else
//				return MiscUtils.sha256(joinedString);
                return MiscUtils.calculateHmacSha256(secret, joinedString);
        } else
            return "";
    }

    public static String generateUrlParam(Map<String, String> params,
                                          String secret, int version) {

        String urlParam = "?";
        for (Entry<String, String> entry : params.entrySet()) {
            urlParam = urlParam + MiscUtils.urlStringEncode(entry.getKey())
                    + "=" + MiscUtils.urlStringEncode(entry.getValue()) + "&";
        }
        if (version == 2 || version == 3) {
            String signature = signatureCalculate(params, secret, version);
            urlParam = urlParam + Const.P.SIGN + "=" + signature;
        } else if (urlParam.endsWith("&")) {
            urlParam = urlParam.substring(0, urlParam.length() - 1);
        }
        SmartLog.d(TAG,
                "String generateUrlParam param=" + MiscUtils.printPwMap(params));
        SmartLog.d(TAG, "String generateUrlParam URL=" + urlParam);
        return urlParam;
    }

    public abstract boolean validateData();

    public Integer getSignVersion() {
        return signVersion;
    }

    public void setSignVersion(Integer signVersion) {
        if (signVersion != null) {
            parameters.put(Const.P.SIGN_VERSION, signVersion.toString());
        }
        PWSDKRequest.signVersion = signVersion;

    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) throws Exception {
        if (key != null && key.matches(Const.REGEX.HEXADECIMAL32)) {
            this.key = key;
            parameters.put(Const.P.KEY, key);
        } else {
            throw new Exception();
        }
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        if (timeStamp != null) {
            parameters.put(Const.P.TS, timeStamp.toString());
        }
        this.timeStamp = timeStamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        if (uid != null) {
            parameters.put(Const.P.UID, uid);
        }
        this.uid = uid;
    }

    public String getUrlParameters() {
        return urlParameters;
    }

    public void setUrlParameters(String urlParameters) {
        this.urlParameters = urlParameters;
    }

    public TreeMap<String, String> getParameter() {
        return parameters;
    }

    public void setParameter(TreeMap<String, String> parameter) {
        this.parameters = parameter;
    }

}
