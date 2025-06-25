package com.paymentwall.pwunifiedsdk.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.paymentwall.pwunifiedsdk.R;
import com.paymentwall.pwunifiedsdk.brick.message.BrickRequest;
import com.paymentwall.pwunifiedsdk.mint.message.MintRequest;
import com.paymentwall.pwunifiedsdk.mobiamo.core.MobiamoPayment;
import com.paymentwall.pwunifiedsdk.object.ExternalPs;
import com.paymentwall.pwunifiedsdk.payalto.message.CustomRequest;
import com.paymentwall.pwunifiedsdk.payalto.utils.Const;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


/**
 * Created by nguyen.anh on 4/8/2016.
 */
public class UnifiedRequest implements Parcelable {

    private String pwProjectKey, pwSecretKey;
    private boolean brickEnabled, mobiamoEnabled, mintEnabled, payAltoEnabled, googlePayEnable;
    private String itemName;
    private String merchantName = "";
    private Double amount;
    private String currency;
    private String userId;
    private String userEmail;
    private int signVersion;
    private String itemId;
    private String itemUrl;
    private File itemFile;
    private int itemResID;
    private String itemContentProvider;
    private int timeout;
    private boolean footerEnabled;
    private boolean selectionSkipped;
    private String psId;
    private boolean testMode;
    private BrickRequest brickRequest;
    private MobiamoPayment mobiamoRequest;
    private MintRequest mintRequest;
    private ArrayList<ExternalPs> externalPsList;
    private Map<String, String> bundle;
    private Map<String, String> customParams;
    private String uiStyle;

    private CustomRequest payAltoRequest;

    private final String KEY_PW_PROJECT_KEY = "PW_PROJECT_KEY";
    private final String KEY_PW_PROJECT_SECRET = "PW_PROJECT_SECRET";
    private final String KEY_ITEM_NAME = "ITEM_NAME";
    private final String KEY_AMOUNT = "AMOUNT";
    private final String KEY_CURRENCY = "CURRENCY";
    private final String KEY_USER_ID = "USER_ID";
    private final String KEY_USER_EMAIL = "USER_ID";
    private final String KEY_ITEM_ID = "ITEM_ID";
    private final String KEY_SIGN_VERSION = "SIGN_VERSION";
    private final String KEY_TEST_MODE = "TEST_MODE";

    public UnifiedRequest() {
        this.externalPsList = new ArrayList<>();
        this.bundle = new TreeMap<>();
        this.customParams = new TreeMap<>();
        bundle.put(KEY_TEST_MODE, false + "");
    }

    public String getPwProjectKey() {
        return pwProjectKey;
    }

    public void setPwProjectKey(String pwProjectKey) {
        this.pwProjectKey = pwProjectKey;
        bundle.put(KEY_PW_PROJECT_KEY, pwProjectKey);
    }

    public String getPwSecretKey() {
        return pwSecretKey;
    }

    public void setPwSecretKey(String pwSecretKey) {
        this.pwSecretKey = pwSecretKey;
        bundle.put(KEY_PW_PROJECT_SECRET, pwSecretKey);
    }

    public boolean isBrickEnabled() {
        return brickEnabled;
    }

    public void addBrick() {
        this.brickEnabled = true;
        this.brickRequest = new BrickRequest();
        brickRequest.setAmount(getAmount());
        brickRequest.setCurrency(getCurrency());
        brickRequest.setAppKey(getPwProjectKey());
        brickRequest.setName(getItemName());
        brickRequest.setMerchantName(getMerchantName());
        brickRequest.setItemResID(getItemResID());
        brickRequest.setItemUrl(getItemUrl());
        brickRequest.setItemFile(getItemFile());
        brickRequest.setItemContentProvider(getItemContentProvider());
        brickRequest.setTimeout(30000);
    }

    public boolean isMobiamoEnabled() {
        return mobiamoEnabled;
    }

    public void addMobiamo() {
        this.mobiamoEnabled = true;
        this.mobiamoRequest = new MobiamoPayment();
        mobiamoRequest.setApplicationKey(getPwProjectKey());
        mobiamoRequest.setProductId(getItemId());
        mobiamoRequest.setProductName(getItemName());
        mobiamoRequest.setUid(getUserId());
        mobiamoRequest.setItemResID(getItemResID());
        mobiamoRequest.setItemContentProvider(getItemContentProvider());
        mobiamoRequest.setItemFile(getItemFile());
        mobiamoRequest.setItemUrl(getItemUrl());
        mobiamoRequest.setAmount(getAmount() + "");
//        mobiamoRequest.setAmount(10000 + "");
//        mobiamoRequest.setCurrency("VND");
//        10/20/50 php
//        5000/10000/15000 vnd
    }

    public boolean isMintEnabled() {
        return mintEnabled;
    }

    public void addMint() {
        this.mintEnabled = true;
        this.mintRequest = new MintRequest();
        mintRequest.setAmount(getAmount());
        mintRequest.setCurrency(getCurrency());
        mintRequest.setUserId(getUserId());
        mintRequest.setAppKey(getPwProjectKey());
        mintRequest.setName(getItemName());
        mintRequest.setItemResID(getItemResID());
        mintRequest.setItemFile(getItemFile());
        mintRequest.setItemUrl(getItemUrl());
        mintRequest.setItemContentProvider(getItemContentProvider());
    }

    public void addPayAlto() {
        if(isSelectionSkipped()) return;
        this.payAltoEnabled = true;
        ExternalPs payAlto = new ExternalPs("pwlocal", "", R.drawable.others, null);
        this.externalPsList.add(0, payAlto);

        if (payAltoRequest == null) payAltoRequest = new CustomRequest();

        payAltoRequest.put(Const.P.KEY, getPwProjectKey());
        payAltoRequest.put(Const.P.UID, getUserId());
        payAltoRequest.put(Const.P.AG_EXTERNAL_ID, getItemId());
        payAltoRequest.put(Const.P.AG_NAME, getItemName());
        payAltoRequest.put(Const.P.AG_TYPE, "fixed");
        payAltoRequest.put(Const.P.CURRENCYCODE, getCurrency());
        payAltoRequest.put(Const.P.AMOUNT, getAmount() + "");
        payAltoRequest.put(Const.P.VERSION, "1.2");
        if (customParams != null) payAltoRequest.putAll(customParams);
        payAltoRequest.setSecretKey(getPwSecretKey());
        payAltoRequest.setSignVersion(getSignVersion());
    }

    public boolean isGooglePayEnable() {
        return googlePayEnable;
    }
    public void addGooglePay() {
        this.googlePayEnable = true;
    }

    public ArrayList<ExternalPs> getExternalPsList() {
        return externalPsList;
    }

    public BrickRequest getBrickRequest() {
        return brickRequest;
    }

    public MobiamoPayment getMobiamoRequest() {
        return mobiamoRequest;
    }


    public MintRequest getMintRequest() {
        return mintRequest;
    }

    public String getItemName() {
        return itemName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public void setItemName(String name) {
        this.itemName = name;
        bundle.put(KEY_ITEM_NAME, name);
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
        bundle.put(KEY_AMOUNT, amount + "");
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
        bundle.put(KEY_CURRENCY, currency);
    }

    public int getSignVersion() {
        return signVersion;
    }

    public void setSignVersion(int signVersion) {
        this.signVersion = signVersion;
        bundle.put(KEY_SIGN_VERSION, signVersion + "");
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public File getItemFile() {
        return itemFile;
    }

    public void setItemFile(File itemFile) {
        this.itemFile = itemFile;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        bundle.put(KEY_USER_ID, userId);
    }

    public String getUserEmail() {
        if (userEmail == null) return "t3_sdk@paymentwall.com";
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        bundle.put(KEY_USER_EMAIL, userEmail);
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
        bundle.put(KEY_ITEM_ID, itemId);
    }

    public Parcelable getPayAltoRequest() {
        return payAltoRequest;
    }

    public boolean isPayAltoEnabled() {
        return payAltoEnabled;
    }

    public void setPayAltoEnabled(boolean payAltoEnabled) {
        this.payAltoEnabled = payAltoEnabled;
    }

    public boolean isTestMode() {
        return testMode;
    }

    public String getUiStyle() {
        return uiStyle;
    }

    public void setUiStyle(String uiStyle) {
        this.uiStyle = uiStyle;
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
        bundle.put(KEY_TEST_MODE, testMode + "");
    }

    public boolean isFooterEnabled() {
        return footerEnabled;
    }

    public void enableFooter() {
        this.footerEnabled = true;
    }

    public boolean isSelectionSkipped() {
        return selectionSkipped;
    }

    public void skipSelection(String psId) {
        addPayAlto();
        this.payAltoRequest.put(Const.P.PS, psId);
        this.selectionSkipped = true;
        this.psId = psId;
    }

    public String getPsId() {
        return psId;
    }

    public Map<String, String> getBundle() {
        return bundle;
    }

    public void setBundle(Map<String, String> bundle) {
        this.bundle = bundle;
    }

    public Map<String, String> getCustomParams() {
        return customParams;
    }

    public void addCustomParam(String key, String value) {
        if(key == null || value == null) return;
        if(key.isEmpty() || value.isEmpty()) return;
        if (customParams == null) {
            customParams = new TreeMap<>();
        }
        customParams.put(key, value);
        if (payAltoRequest != null) payAltoRequest.put(key, value);
    }

    public void addPayAltoParams(String key, String value) {
        if(key == null || value == null) return;
        if(key.isEmpty() || value.isEmpty()) return;
        if (this.payAltoRequest == null) {
            this.payAltoRequest = new CustomRequest();
        }
        this.payAltoRequest.put(key, value);
    }

    public void add(ExternalPs... externalPss) {
        for (ExternalPs ps : externalPss) {
            getExternalPsList().add(ps);
        }
    }

    public boolean isRequestValid() {
        if (getPwProjectKey() == null) {
            return false;
        }
//        if (getPwSecretKey() == null) {
//            return false;
//        }
        if (getItemName() == null) {
            return false;
        }
        if (getAmount() == 0d) {
            return false;
        }
        if (getCurrency() == null) {
            return false;
        }
        if (getItemId() == null) {
            return false;
        }
        if (getUserId() == null) {
            return false;
        }
        if (brickRequest != null) {
            if (getTimeout() == 0) {
                return false;
            }
        }
        if (userEmail != null && !isValidEmail(userEmail)) {
            return false;
        }
        return true;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pwProjectKey);
        dest.writeString(this.pwSecretKey);
        dest.writeByte(this.brickEnabled ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mobiamoEnabled ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mintEnabled ? (byte) 1 : (byte) 0);
        dest.writeByte(this.payAltoEnabled ? (byte) 1 : (byte) 0);
        dest.writeByte(this.googlePayEnable ? (byte) 1 : (byte) 0);
        dest.writeString(this.itemName);
        dest.writeValue(this.amount);
        dest.writeString(this.currency);
        dest.writeString(this.userId);
        dest.writeString(this.userEmail);
        dest.writeInt(this.signVersion);
        dest.writeString(this.itemId);
        dest.writeString(this.itemUrl);
        dest.writeSerializable(this.itemFile);
        dest.writeInt(this.itemResID);
        dest.writeString(this.itemContentProvider);
        dest.writeInt(this.timeout);
        dest.writeByte(this.footerEnabled ? (byte) 1 : (byte) 0);
        dest.writeByte(this.selectionSkipped ? (byte) 1 : (byte) 0);
        dest.writeString(this.psId);
        dest.writeByte(this.testMode ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.brickRequest, flags);
        dest.writeSerializable(this.mobiamoRequest);
        dest.writeParcelable(this.mintRequest, flags);
        dest.writeList(this.externalPsList);
        dest.writeInt(this.bundle.size());
        for (Map.Entry<String, String> entry : this.bundle.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
        dest.writeInt(this.customParams.size());
        for (Map.Entry<String, String> entry : this.customParams.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
        dest.writeString(this.uiStyle);
        dest.writeParcelable(this.payAltoRequest, flags);
//        dest.writeString(this.KEY_PW_PROJECT_KEY);
//        dest.writeString(this.KEY_PW_PROJECT_SECRET);
//        dest.writeString(this.KEY_ITEM_NAME);
//        dest.writeString(this.KEY_AMOUNT);
//        dest.writeString(this.KEY_CURRENCY);
//        dest.writeString(this.KEY_USER_ID);
//        dest.writeString(this.KEY_ITEM_ID);
//        dest.writeString(this.KEY_SIGN_VERSION);
//        dest.writeString(this.KEY_TEST_MODE);
    }

    protected UnifiedRequest(Parcel in) {
        this.pwProjectKey = in.readString();
        this.pwSecretKey = in.readString();
        this.brickEnabled = in.readByte() != 0;
        this.mobiamoEnabled = in.readByte() != 0;
        this.mintEnabled = in.readByte() != 0;
        this.payAltoEnabled = in.readByte() != 0;
        this.googlePayEnable = in.readByte() != 0;
        this.itemName = in.readString();
        this.amount = (Double) in.readValue(Double.class.getClassLoader());
        this.currency = in.readString();
        this.userId = in.readString();
        this.userEmail = in.readString();
        this.signVersion = in.readInt();
        this.itemId = in.readString();
        this.itemUrl = in.readString();
        this.itemFile = (File) in.readSerializable();
        this.itemResID = in.readInt();
        this.itemContentProvider = in.readString();
        this.timeout = in.readInt();
        this.footerEnabled = in.readByte() != 0;
        this.selectionSkipped = in.readByte() != 0;
        this.psId = in.readString();
        this.testMode = in.readByte() != 0;
        this.brickRequest = in.readParcelable(BrickRequest.class.getClassLoader());
        this.mobiamoRequest = (MobiamoPayment) in.readSerializable();
        this.mintRequest = in.readParcelable(MintRequest.class.getClassLoader());
        this.externalPsList = new ArrayList<ExternalPs>();
        in.readList(this.externalPsList, ExternalPs.class.getClassLoader());
        int bundleSize = in.readInt();
        this.bundle = new TreeMap<String, String>();
        for (int i = 0; i < bundleSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.bundle.put(key, value);
        }
        int customParamsSize = in.readInt();
        this.customParams = new TreeMap<String, String>();
        for (int i = 0; i < customParamsSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.customParams.put(key, value);
        }
        this.uiStyle = in.readString();
        this.payAltoRequest = in.readParcelable(CustomRequest.class.getClassLoader());
    }

    public static final Creator<UnifiedRequest> CREATOR = new Creator<UnifiedRequest>() {
        @Override
        public UnifiedRequest createFromParcel(Parcel source) {
            return new UnifiedRequest(source);
        }

        @Override
        public UnifiedRequest[] newArray(int size) {
            return new UnifiedRequest[size];
        }
    };

    // region Helper
    /**
     * Validates email format using Android's built-in email pattern
     * @param email the email string to validate
     * @return true if email is in valid format, false otherwise
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
    }

    // Alternative implementation using regex pattern (if you prefer custom validation)
    private boolean isValidEmailRegex(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.trim().matches(emailPattern);
    }
    // endregion
}
