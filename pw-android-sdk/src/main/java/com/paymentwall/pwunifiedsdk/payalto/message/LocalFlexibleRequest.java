package com.paymentwall.pwunifiedsdk.payalto.message;

import android.os.Parcel;
import android.os.Parcelable;

import com.paymentwall.pwunifiedsdk.payalto.utils.Const;
import com.paymentwall.pwunifiedsdk.payalto.utils.Const.P;
import com.paymentwall.pwunifiedsdk.payalto.utils.PayAltoMiscUtils;

import java.io.Serializable;
import java.math.BigDecimal;

public class LocalFlexibleRequest extends LocalRequest implements Serializable, Parcelable {
    /**
     *
     */
    private static final long serialVersionUID = 6668201868364303348L;
    private String agExternalId;
    private String agName;
    private Integer agPeriodLength;
    private String agPeriodType;
    private String agPostTrialExternalId;
    private String agPostTrialName;
    private Integer agPostTrialPeriodLength;
    private String agPostTrialPeriodType;
    private String agPromo;
    private Integer agRecurring;
    private Integer agTrial;
    private String agType;
    private String currencycode;
    private Integer hidePostTrialGood;
    private String postTrialAmount;
    private String postTrialCurrencycode;
    private Integer showPostTrialNonRecurring = 1;
    private Integer showPostTrialRecurring = 1;
    private Integer showTrialNonRecurring = 0;
    private Integer showTrialRecurring = 0;
    private BigDecimal amount;

    public LocalFlexibleRequest() {
        setSuccessUrl(Const.DEFAULT_SUCCESS_URL);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        addParameter(P.AMOUNT, amount.toPlainString());
        this.amount = amount;
    }

    public void setAmount(Float amount) {
        this.amount = BigDecimal.valueOf(amount);
        addParameter(P.AMOUNT, this.amount.toPlainString());
    }

    public void setAmount(Double amount) {
        this.amount = BigDecimal.valueOf(amount);
        addParameter(P.AMOUNT, this.amount.toPlainString());
    }

    public String getAgExternalId() {
        return agExternalId;
    }

    public void setAgExternalId(String agExternalId) {
        addParameter(P.AG_EXTERNAL_ID, agExternalId);
        this.agExternalId = agExternalId;
    }

    public String getAgName() {
        return agName;
    }

    public void setAgName(String agName) {
        addParameter(P.AG_NAME, agName);
        this.agName = agName;
    }

    public Integer getAgPeriodLength() {
        return agPeriodLength;
    }

    public void setAgPeriodLength(Integer agPeriodLength) {
        addParameter(P.AG_PERIOD_LENGTH, agPeriodType.toString());
        this.agPeriodLength = agPeriodLength;
    }

    public String getAgPeriodType() {
        return agPeriodType;
    }

    public void setAgPeriodType(String agPeriodType) {
        addParameter(P.AG_PERIOD_TYPE, agPeriodType);
        this.agPeriodType = agPeriodType;
    }

    public String getAgPostTrialExternalId() {
        return agPostTrialExternalId;
    }

    public void setAgPostTrialExternalId(String agPostTrialExternalId) {
        addParameter(P.AG_POST_TRIAL_EXTERNAL_ID, agPostTrialExternalId);
        this.agPostTrialExternalId = agPostTrialExternalId;
    }

    public String getAgPostTrialName() {
        return agPostTrialName;
    }

    public void setAgPostTrialName(String agPostTrialName) {
        addParameter(P.AG_POST_TRIAL_NAME, agPostTrialName);
        this.agPostTrialName = agPostTrialName;
    }

    public Integer getAgPostTrialPeriodLength() {
        return agPostTrialPeriodLength;
    }

    public void setAgPostTrialPeriodLength(Integer agPostTrialPeriodLength) {
        addParameter(P.AG_POST_TRIAL_PERIOD_LENGTH, agPostTrialPeriodLength.toString());
        this.agPostTrialPeriodLength = agPostTrialPeriodLength;
    }

    public String getAgPostTrialPeriodType() {
        return agPostTrialPeriodType;
    }

    public void setAgPostTrialPeriodType(String agPostTrialPeriodType) {
        addParameter(P.AG_POST_TRIAL_PERIOD_TYPE, agPostTrialPeriodType);
        this.agPostTrialPeriodType = agPostTrialPeriodType;
    }

    public String getAgPromo() {
        return agPromo;
    }

    public void setAgPromo(String agPromo) {
        addParameter(P.AG_PROMO, agPromo);
        this.agPromo = agPromo;
    }

    public Integer getAgRecurring() {
        return agRecurring;
    }

    public void setAgRecurring(Integer agRecurring) {
        addParameter(P.AG_RECURRING, agRecurring.toString());
        this.agRecurring = agRecurring;
    }

    public Integer getAgTrial() {
        return agTrial;
    }

    public void setAgTrial(Integer agTrial) {
        addParameter(P.AG_TRIAL, agTrial.toString());
        this.agTrial = agTrial;
    }

    public String getAgType() {
        return agType;
    }

    public void setAgType(String agType) {
        addParameter(P.AG_TYPE, agType);
        this.agType = agType;
    }

    public String getCurrencycode() {
        return currencycode;
    }

    public void setCurrencycode(String currencycode) {
        addParameter(P.CURRENCYCODE, currencycode);
        this.currencycode = currencycode;
    }

    public Integer getHidePostTrialGood() {
        return hidePostTrialGood;
    }

    public void setHidePostTrialGood(Integer hidePostTrialGood) {
        addParameter(P.HIDE_POST_TRIAL_GOOD, hidePostTrialGood.toString());
        this.hidePostTrialGood = hidePostTrialGood;
    }

    public String getPostTrialAmount() {
        return postTrialAmount;
    }

    public void setPostTrialAmount(String postTrialAmount) {
        addParameter(P.POST_TRIAL_AMOUNT, postTrialAmount);
        this.postTrialAmount = postTrialAmount;
    }

    public String getPostTrialCurrencycode() {
        return postTrialCurrencycode;
    }

    public void setPostTrialCurrencycode(String postTrialCurrencycode) {
        addParameter(P.POST_TRIAL_CURRENCYCODE, postTrialCurrencycode);
        this.postTrialCurrencycode = postTrialCurrencycode;
    }

    public Integer getShowPostTrialNonRecurring() {
        return showPostTrialNonRecurring;
    }

    public void setShowPostTrialNonRecurring(Integer showPostTrialNonRecurring) {
        addParameter(P.SHOW_POST_TRIAL_NON_RECURRING, showPostTrialNonRecurring.toString());
        this.showPostTrialNonRecurring = showPostTrialNonRecurring;
    }

    public Integer getShowPostTrialRecurring() {
        return showPostTrialRecurring;
    }

    public void setShowPostTrialRecurring(Integer showPostTrialRecurring) {
        addParameter(P.SHOW_POST_TRIAL_RECURRING, showPostTrialRecurring.toString());
        this.showPostTrialRecurring = showPostTrialRecurring;
    }

    public Integer getShowTrialNonRecurring() {
        return showTrialNonRecurring;
    }

    public void setShowTrialNonRecurring(Integer showTrialNonRecurring) {
        addParameter(P.SHOW_TRIAL_NON_RECURRING, showTrialNonRecurring.toString());
        this.showTrialNonRecurring = showTrialNonRecurring;
    }

    public Integer getShowTrialRecurring() {
        return showTrialRecurring;
    }

    public void setShowTrialRecurring(Integer showTrialRecurring) {
        addParameter(P.SHOW_TRIAL_RECURRING, showTrialRecurring.toString());
        this.showTrialRecurring = showTrialRecurring;
    }

    @Override
    public String toString() {
        return "LocalFlexibleRequest{" +
                "agExternalId='" + agExternalId + '\'' +
                ", agName='" + agName + '\'' +
                ", agPeriodLength=" + agPeriodLength +
                ", agPeriodType='" + agPeriodType + '\'' +
                ", agPostTrialExternalId='" + agPostTrialExternalId + '\'' +
                ", agPostTrialName='" + agPostTrialName + '\'' +
                ", agPostTrialPeriodLength=" + agPostTrialPeriodLength +
                ", agPostTrialPeriodType='" + agPostTrialPeriodType + '\'' +
                ", agPromo='" + agPromo + '\'' +
                ", agRecurring=" + agRecurring +
                ", agTrial=" + agTrial +
                ", agType='" + agType + '\'' +
                ", currencycode='" + currencycode + '\'' +
                ", hidePostTrialGood=" + hidePostTrialGood +
                ", postTrialAmount='" + postTrialAmount + '\'' +
                ", postTrialCurrencycode='" + postTrialCurrencycode + '\'' +
                ", showPostTrialNonRecurring=" + showPostTrialNonRecurring +
                ", showPostTrialRecurring=" + showPostTrialRecurring +
                ", showTrialNonRecurring=" + showTrialNonRecurring +
                ", showTrialRecurring=" + showTrialRecurring +
                ", amount=" + amount +
                "} " + super.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(autoSigned ? 1 : 0);
        dest.writeString(this.key);
        dest.writeString(this.sign);
        dest.writeValue(this.signVersion);
        dest.writeValue(this.timeStamp);
        dest.writeString(this.uid);
        dest.writeString(this.secretKey);
        dest.writeBundle(PayAltoMiscUtils.stringMapToBundle(this.parameters));
//        dest.writeSerializable(this.parameters);
        dest.writeString(this.email);
        dest.writeValue(this.evaluation);
        dest.writeString(this.firstname);
        dest.writeString(this.lang);
        dest.writeString(this.lastname);
        dest.writeString(this.locationAddress);
        dest.writeString(this.locationCity);
        dest.writeString(this.locationCountry);
        dest.writeString(this.locationState);
        dest.writeString(this.locationZip);
        dest.writeString(this.pingbackUrl);
        dest.writeString(this.paymentSystem);
        dest.writeString(this.sex);
        dest.writeString(this.successUrl);
        dest.writeString(this.widget);
        dest.writeString(this.birthday);
        dest.writeString(this.countryCode);
        dest.writeString(this.apiType);
        dest.writeBundle(PayAltoMiscUtils.intMapToBundle(externalIds));
        dest.writeBundle(PayAltoMiscUtils.pricesToBundle(prices));
        dest.writeBundle(PayAltoMiscUtils.intMapToBundle(currencies));
//        dest.writeParcelable(this.externalIds, flags);
//        dest.writeParcelable(this.prices, flags);
//        dest.writeParcelable(this.currencies, flags);
        dest.writeString(this.agExternalId);
        dest.writeString(this.agName);
        dest.writeValue(this.agPeriodLength);
        dest.writeString(this.agPeriodType);
        dest.writeString(this.agPostTrialExternalId);
        dest.writeString(this.agPostTrialName);
        dest.writeValue(this.agPostTrialPeriodLength);
        dest.writeString(this.agPostTrialPeriodType);
        dest.writeString(this.agPromo);
        dest.writeValue(this.agRecurring);
        dest.writeValue(this.agTrial);
        dest.writeString(this.agType);
        dest.writeString(this.currencycode);
        dest.writeValue(this.hidePostTrialGood);
        dest.writeString(this.postTrialAmount);
        dest.writeString(this.postTrialCurrencycode);
        dest.writeValue(this.showPostTrialNonRecurring);
        dest.writeValue(this.showPostTrialRecurring);
        dest.writeValue(this.showTrialNonRecurring);
        dest.writeValue(this.showTrialRecurring);
//        dest.writeValue(this.amount);
        dest.writeSerializable(amount);
        dest.writeParcelable(this.userProfile, flags);
        dest.writeString(this.mobileDownloadLink);
    }

    protected LocalFlexibleRequest(Parcel in) {
        this.autoSigned = in.readInt() != 0;
        this.key = in.readString();
        this.sign = in.readString();
        this.signVersion = (Integer) in.readValue(Integer.class.getClassLoader());
        this.timeStamp = (Long) in.readValue(Long.class.getClassLoader());
        this.uid = in.readString();
        this.secretKey = in.readString();
        this.parameters = PayAltoMiscUtils.bundleToStringTreeMap(in.readBundle());
//        this.parameters = (TreeMap<String, String>) in.readSerializable();
        this.email = in.readString();
        this.evaluation = (Integer) in.readValue(Integer.class.getClassLoader());
        this.firstname = in.readString();
        this.lang = in.readString();
        this.lastname = in.readString();
        this.locationAddress = in.readString();
        this.locationCity = in.readString();
        this.locationCountry = in.readString();
        this.locationState = in.readString();
        this.locationZip = in.readString();
        this.pingbackUrl = in.readString();
        this.paymentSystem = in.readString();
        this.sex = in.readString();
        this.successUrl = in.readString();
        this.widget = in.readString();
        this.birthday = in.readString();
        this.countryCode = in.readString();
        this.apiType = in.readString();
        this.externalIds = PayAltoMiscUtils.bundleToIntegerTreeMap(in.readBundle());
        this.prices = PayAltoMiscUtils.bundleToPrices(in.readBundle());
        this.currencies = PayAltoMiscUtils.bundleToIntegerTreeMap(in.readBundle());
//        this.externalIds = in.readParcelable(Map<Integer, String>.class.getClassLoader());
//        this.prices = in.readParcelable(Map<Integer, Float>.class.getClassLoader());
//        this.currencies = in.readParcelable(Map<Integer, String>.class.getClassLoader());
        this.agExternalId = in.readString();
        this.agName = in.readString();
        this.agPeriodLength = (Integer) in.readValue(Integer.class.getClassLoader());
        this.agPeriodType = in.readString();
        this.agPostTrialExternalId = in.readString();
        this.agPostTrialName = in.readString();
        this.agPostTrialPeriodLength = (Integer) in.readValue(Integer.class.getClassLoader());
        this.agPostTrialPeriodType = in.readString();
        this.agPromo = in.readString();
        this.agRecurring = (Integer) in.readValue(Integer.class.getClassLoader());
        this.agTrial = (Integer) in.readValue(Integer.class.getClassLoader());
        this.agType = in.readString();
        this.currencycode = in.readString();
        this.hidePostTrialGood = (Integer) in.readValue(Integer.class.getClassLoader());
        this.postTrialAmount = in.readString();
        this.postTrialCurrencycode = in.readString();
        this.showPostTrialNonRecurring = (Integer) in.readValue(Integer.class.getClassLoader());
        this.showPostTrialRecurring = (Integer) in.readValue(Integer.class.getClassLoader());
        this.showTrialNonRecurring = (Integer) in.readValue(Integer.class.getClassLoader());
        this.showTrialRecurring = (Integer) in.readValue(Integer.class.getClassLoader());
        this.amount = (BigDecimal) in.readSerializable();
        this.userProfile = in.readParcelable(UserProfile.class.getClassLoader());
        this.mobileDownloadLink = in.readString();
    }

    public static final Creator<LocalFlexibleRequest> CREATOR = new Creator<LocalFlexibleRequest>() {
        public LocalFlexibleRequest createFromParcel(Parcel source) {
            return new LocalFlexibleRequest(source);
        }

        public LocalFlexibleRequest[] newArray(int size) {
            return new LocalFlexibleRequest[size];
        }
    };
}
