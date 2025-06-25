package com.paymentwall.pwunifiedsdk.brick.message;

import android.os.Parcel;
import android.os.Parcelable;

import com.paymentwall.pwunifiedsdk.util.MiscUtils;

import java.io.File;
import java.io.Serializable;

//import org.apache.http.NameValuePair;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.message.BasicNameValuePair;

/**
 * Created by nguyen.anh on 4/15/2016.
 */
public class BrickRequest implements Parcelable {

    private static final long serialVersionUID = -2852504747591386429L;
    private String name;
    private Double amount;
    private String appKey;
    private String currency;
    private String itemUrl;
    private File itemFile;
    private int itemResID;
    private String itemContentProvider;
    private int timeout;
    private String merchantName = "";

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public File getItemFile() {
        return itemFile;
    }

    public void setItemFile(File itemFile) {
        this.itemFile = itemFile;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public int getItemResID() {
        return itemResID;
    }

    public void setItemResID(int itemResID) {
        this.itemResID = itemResID;
    }

    public String getItemContentProvider() {
        return itemContentProvider;
    }

    public void setItemContentProvider(String itemContentProvider) {
        this.itemContentProvider = itemContentProvider;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public BrickRequest() {
        super();
        // TODO Auto-generated constructor stub
    }

    public boolean validate() {
        if (appKey == null || appKey.length() == 0) return false;
        if (amount == null || amount < 0) return false;
        if (currency == null || !MiscUtils.isValidCurrency(currency)) return false;
        return true;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeValue(this.amount);
        dest.writeString(this.appKey);
        dest.writeString(this.currency);
        dest.writeString(this.itemUrl);
        dest.writeSerializable(this.itemFile);
        dest.writeInt(this.itemResID);
        dest.writeString(this.itemContentProvider);
        dest.writeInt(this.timeout);
        dest.writeString(this.merchantName);
    }

    protected BrickRequest(Parcel in) {
        this.name = in.readString();
        this.amount = (Double) in.readValue(Double.class.getClassLoader());
        this.appKey = in.readString();
        this.currency = in.readString();
        this.itemUrl = in.readString();
        this.itemFile = (File) in.readSerializable();
        this.itemResID = in.readInt();
        this.itemContentProvider = in.readString();
        this.timeout = in.readInt();
        this.merchantName = in.readString();
    }

    public static final Creator<BrickRequest> CREATOR = new Creator<BrickRequest>() {
        @Override
        public BrickRequest createFromParcel(Parcel source) {
            return new BrickRequest(source);
        }

        @Override
        public BrickRequest[] newArray(int size) {
            return new BrickRequest[size];
        }
    };
}
