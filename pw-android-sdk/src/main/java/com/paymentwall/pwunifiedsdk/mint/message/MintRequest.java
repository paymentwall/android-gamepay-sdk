package com.paymentwall.pwunifiedsdk.mint.message;


import android.os.Parcel;
import android.os.Parcelable;

import com.paymentwall.pwunifiedsdk.mint.utils.Const;
import com.paymentwall.pwunifiedsdk.util.MiscUtils;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

//import org.apache.http.NameValuePair;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.message.BasicNameValuePair;

public class MintRequest implements Parcelable {
    private static final long serialVersionUID = -2852504747591386429L;
    private String name;
    private Double amount;
    private String appKey;
    private String currency;
    private List<String> ePin;
    private String userId;
    private String itemUrl;
    private File itemFile;
    private int itemResID;
    private String itemContentProvider;

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

    public List<String> getePin() {
        return ePin;
    }

    public void setePin(List<String> ePin) throws ParseException {
        for (String pin : ePin) {

            if (!pin.matches(Const.MINT.REGEX.PIN)) {
                throw new ParseException("Mint PINs must have 16 digits. Pin = " + pin, 0);
            }
        }

        this.ePin = ePin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public MintRequest() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void addEPin(String pin) throws ParseException {
        if (ePin == null) ePin = new ArrayList<String>();
        if (!pin.matches(Const.MINT.REGEX.PIN) || pin == null)
            throw new ParseException("Mint PINs must have 16 digits. Pin = " + pin, 0);
        ePin.add(pin);
    }

    public Map<String, String> toParametersMap() {
        TreeMap<String, String> parametersMap = new TreeMap<String, String>();
        if (amount != null) {
            parametersMap.put("amount", String.valueOf(amount));
        }
        if (appKey != null) {
            parametersMap.put("app_key", appKey);
        }
        if (currency != null) {
            parametersMap.put("currency", currency);
        }
        if (ePin != null && !ePin.isEmpty()) {
            for (int i = 0; i < ePin.size(); i++) {
                parametersMap.put("epin[" + i + "]", ePin.get(i));
            }
        }
        if (userId != null) {
            parametersMap.put("user_id", userId);
        }
        return parametersMap;
    }

    public boolean validate() {
        if (appKey == null || appKey.length() == 0) return false;
        if (amount == null || amount < 0) return false;
        if (currency == null || !MiscUtils.isValidCurrency(currency)) return false;
        if (userId == null || userId.length() == 0) return false;
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
        dest.writeStringList(this.ePin);
        dest.writeString(this.userId);
        dest.writeString(this.itemUrl);
        dest.writeSerializable(this.itemFile);
        dest.writeInt(this.itemResID);
        dest.writeString(this.itemContentProvider);
    }

    protected MintRequest(Parcel in) {
        this.name = in.readString();
        this.amount = (Double) in.readValue(Double.class.getClassLoader());
        this.appKey = in.readString();
        this.currency = in.readString();
        this.ePin = in.createStringArrayList();
        this.userId = in.readString();
        this.itemUrl = in.readString();
        this.itemFile = (File) in.readSerializable();
        this.itemResID = in.readInt();
        this.itemContentProvider = in.readString();
    }

    public static final Creator<MintRequest> CREATOR = new Creator<MintRequest>() {
        @Override
        public MintRequest createFromParcel(Parcel source) {
            return new MintRequest(source);
        }

        @Override
        public MintRequest[] newArray(int size) {
            return new MintRequest[size];
        }
    };
}
