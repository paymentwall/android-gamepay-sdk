package com.paymentwall.pwunifiedsdk.payalto.message;

import android.os.Parcel;
import android.os.Parcelable;

import com.paymentwall.pwunifiedsdk.payalto.utils.Const;
import com.paymentwall.pwunifiedsdk.payalto.utils.Const.P;
import com.paymentwall.pwunifiedsdk.payalto.utils.PayAltoMiscUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class LocalDefaultRequest extends LocalRequest implements Serializable, Parcelable {

    /**
     *
     */
    private static final long serialVersionUID = 58099064679709136L;
    private String defaultGoodsId;
    private Map<Integer, String> displayGoods;
    private Map<Integer, String> hideGoods;

    public LocalDefaultRequest() {
        setSuccessUrl(Const.DEFAULT_SUCCESS_URL);
    }

    public String getDefaultGoodsId() {
        return defaultGoodsId;
    }

    public void setDefaultGoodsId(String defaultGoodsId) {
        addParameter(P.DEFAULT_GOODSID, defaultGoodsId);
        this.defaultGoodsId = defaultGoodsId;
    }

    public Map<Integer, String> getDisplayGoods() {
        return displayGoods;
    }

    public void setDisplayGoods(Map<Integer, String> displayGoods) {
        if (displayGoods != null && displayGoods.size() > 1) {
            for (Entry<Integer, String> entry : displayGoods.entrySet()) {
                addParameter(P.DISPLAY_GOODSID + P.i(entry.getKey()), entry.getValue());
            }
        } else if (displayGoods != null && displayGoods.size() == 1) {
            for (Entry<Integer, String> entry : displayGoods.entrySet()) {
                addParameter(P.DISPLAY_GOODSID, entry.getValue());
            }
        }
        this.displayGoods = displayGoods;
    }

    public void setSingleDisplayGoods(String displayGoods) {
        addParameter(P.DISPLAY_GOODSID, displayGoods);
        this.displayGoods = new TreeMap<Integer, String>();
        this.displayGoods.put(0, displayGoods);

    }

    public Map<Integer, String> getHideGoods() {
        return hideGoods;
    }

    public void setHideGoods(Map<Integer, String> hideGoods) {
        for (Entry<Integer, String> entry : hideGoods.entrySet()) {
            addParameter(P.HIDE_GOODSID + P.i(entry.getKey()), entry.getValue());
        }
        this.hideGoods = hideGoods;
    }

    @Override
    public String toString() {
        return "LocalDefaultRequest{" +
                "defaultGoodsId='" + defaultGoodsId + '\'' +
                ", displayGoods=" + displayGoods +
                ", hideGoods=" + hideGoods +
                "} " + super.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(autoSigned? 1:0);
        dest.writeString(this.key);
        dest.writeString(this.sign);
        dest.writeValue(this.signVersion);
        dest.writeValue(this.timeStamp);
        dest.writeString(this.uid);
        dest.writeString(this.secretKey);
        dest.writeBundle(PayAltoMiscUtils.stringMapToBundle(this.parameters));
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
        dest.writeBundle(PayAltoMiscUtils.intMapToBundle(this.externalIds));
        dest.writeBundle(PayAltoMiscUtils.pricesToBundle(this.prices));
        dest.writeBundle(PayAltoMiscUtils.intMapToBundle(this.currencies));
        dest.writeString(this.defaultGoodsId);
        dest.writeBundle(PayAltoMiscUtils.intMapToBundle(this.displayGoods));
        dest.writeBundle(PayAltoMiscUtils.intMapToBundle(this.hideGoods));
        dest.writeParcelable(this.userProfile, flags);
        dest.writeString(this.mobileDownloadLink);
    }

    protected LocalDefaultRequest(Parcel in) {
        this.autoSigned = in.readInt() != 0;
        this.key = in.readString();
        this.sign = in.readString();
        this.signVersion = (Integer) in.readValue(Integer.class.getClassLoader());
        this.timeStamp = (Long) in.readValue(Long.class.getClassLoader());
        this.uid = in.readString();
        this.secretKey = in.readString();
        this.parameters = PayAltoMiscUtils.bundleToStringTreeMap(in.readBundle());
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
        this.defaultGoodsId = in.readString();
        this.displayGoods = PayAltoMiscUtils.bundleToIntegerTreeMap(in.readBundle());
        this.hideGoods = PayAltoMiscUtils.bundleToIntegerTreeMap(in.readBundle());
        this.userProfile = in.readParcelable(UserProfile.class.getClassLoader());
        this.mobileDownloadLink = in.readString();
    }

    public static final Parcelable.Creator<LocalDefaultRequest> CREATOR = new Parcelable.Creator<LocalDefaultRequest>() {
        public LocalDefaultRequest createFromParcel(Parcel source) {
            return new LocalDefaultRequest(source);
        }

        public LocalDefaultRequest[] newArray(int size) {
            return new LocalDefaultRequest[size];
        }
    };

    public static class Builder {

    }


}
