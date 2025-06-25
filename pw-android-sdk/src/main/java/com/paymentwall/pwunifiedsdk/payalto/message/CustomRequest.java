package com.paymentwall.pwunifiedsdk.payalto.message;

import android.os.Parcel;
import android.os.Parcelable;

import com.paymentwall.pwunifiedsdk.util.SmartLog;
import com.paymentwall.pwunifiedsdk.payalto.utils.Const;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by paymentwall on 14/09/15.
 */
public class CustomRequest implements Parcelable {
    private int signVersion = 0;
    private String secretKey;
    private boolean autoSigned = false;

    protected String mobileDownloadLink;

    public String getMobileDownloadLink() {
        return mobileDownloadLink;
    }

    public void setMobileDownloadLink(String mobileDownloadLink) {
        this.mobileDownloadLink = mobileDownloadLink;
    }

    private Map<String, String> parameters = new TreeMap<String, String>();

    public boolean isAutoSigned() {
        return autoSigned;
    }

    public void setAutoSigned(boolean autoSigned) {
        this.autoSigned = autoSigned;
    }

    public CustomRequest() {
        put(Const.P.SUCCESS_URL, Const.DEFAULT_SUCCESS_URL);
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        autoSigned = secretKey !=null && secretKey.length() > 0;
    }

    public int getSignVersion() {
        return signVersion;
    }

    public void setSignVersion(int signVersion) {
        this.signVersion = signVersion;
        put(Const.P.SIGN_VERSION, String.valueOf(signVersion));
    }

    public void putAll(Map<String, String> additionalParameters) {
        if (additionalParameters == null || additionalParameters.isEmpty()) return;
        parameters.putAll(additionalParameters);
        /*Set<Map.Entry<String, String>> entrySet = additionalParameters.entrySet();
        Iterator<Map.Entry<String, String>> entryIterator = entrySet.iterator();
        while (entryIterator.hasNext()) {
            final Map.Entry<String, String> entry = entryIterator.next();
            parameters.add(new Parameter(entry.getKey(), entry.getValue()));
        }*/
    }

    public String put(String key, String value) {
        if (key == null) throw new NullPointerException("key is null");
        return parameters.put(key, value);
    }

    public String get(String key) {
        if (key == null) throw new NullPointerException("key is null");
        return parameters.get(key);
    }

    public boolean containsKey(String key) {
        return parameters.containsKey(key);
    }

    public String remove(String key) {
        return parameters.remove(key);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String getUrlParam() {
        String queryString = "";

        if (autoSigned && !containsKey(Const.P.SIGN)) {
            queryString = PWSDKRequest.generateUrlParam(this.getParameters(), secretKey, signVersion);
        } else {
            queryString = PWSDKRequest.generateUrlParam(this.getParameters());
        }
        SmartLog.i("Query String: " + queryString);
        return queryString;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.signVersion);
        dest.writeString(this.secretKey);
        dest.writeByte(this.autoSigned ? (byte) 1 : (byte) 0);
        dest.writeString(this.mobileDownloadLink);
        dest.writeInt(this.parameters.size());
        for (Map.Entry<String, String> entry : this.parameters.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    protected CustomRequest(Parcel in) {
        this.signVersion = in.readInt();
        this.secretKey = in.readString();
        this.autoSigned = in.readByte() != 0;
        this.mobileDownloadLink = in.readString();
        int parametersSize = in.readInt();
        this.parameters = new TreeMap<String, String>();
        for (int i = 0; i < parametersSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.parameters.put(key, value);
        }
    }

    public static final Creator<CustomRequest> CREATOR = new Creator<CustomRequest>() {
        @Override
        public CustomRequest createFromParcel(Parcel source) {
            return new CustomRequest(source);
        }

        @Override
        public CustomRequest[] newArray(int size) {
            return new CustomRequest[size];
        }
    };
}
