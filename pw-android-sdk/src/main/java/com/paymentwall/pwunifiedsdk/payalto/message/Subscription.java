package com.paymentwall.pwunifiedsdk.payalto.message;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by paymentwall on 20/08/15.
 */
public class Subscription implements Parcelable {
    static final String K_OBJECT = "object";
    static final String K_ID = "id";
    static final String K_PERIOD = "period";
    static final String K_PERIOD_DURATION = "period_duration";
    static final String K_PAYMENTS_LIMIT = "payments_limit";
    static final String K_IS_TRIAL = "is_trial";
    static final String K_STARTED = "started";
    static final String K_EXPIRED = "expired";
    static final String K_ACTIVE = "active";
    static final String K_DATE_STARTED= "date_started";
    static final String K_DATE_NEXT = "date_next";
    /*
    "subscription":{
        "object":"subscription",
        "id":"VCBZT392SW",
        "period":"day",
        "period_duration":3,
        "payments_limit":122,
        "is_trial":0,
        "started":1,
        "expired":0,
        "active":1,
        "date_started":1419438832,
        "date_next":1419698032
    }
    */
    private String object;
    private String id;
    private String period;
    private Integer periodDuration;
    private Long paymentLimit;
    private Integer trialVal;
    private Integer startedVal;
    private Integer expiredVal;
    private Integer activeVal;
    private Long dateStarted;
    private Long dateNext;

    public boolean isActive() {
        if(activeVal!=null && activeVal > 0) return true;
        else return false;
    }

    Integer getActiveVal() {
        return activeVal;
    }

    void setActiveVal(Integer activeVal) {
        this.activeVal = activeVal;
    }

    public Long getDateNext() {
        return dateNext;
    }

    void setDateNext(Long dateNext) {
        this.dateNext = dateNext;
    }

    public Long getDateStarted() {
        return dateStarted;
    }

    void setDateStarted(Long dateStarted) {
        this.dateStarted = dateStarted;
    }

    public boolean isExpired() {
        if(expiredVal!=null && expiredVal > 0) return true;
        else return false;
    }

    Integer getExpiredVal() {
        return expiredVal;
    }

    void setExpiredVal(Integer expiredVal) {
        this.expiredVal = expiredVal;
    }

    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    void setObject(String object) {
        this.object = object;
    }

    public Long getPaymentLimit() {
        return paymentLimit;
    }

    void setPaymentLimit(Long paymentLimit) {
        this.paymentLimit = paymentLimit;
    }

    public String getPeriod() {
        return period;
    }

    void setPeriod(String period) {
        this.period = period;
    }

    public Integer getPeriodDuration() {
        return periodDuration;
    }

    void setPeriodDuration(Integer periodDuration) {
        this.periodDuration = periodDuration;
    }

    public boolean isStarted() {
        if(startedVal!=null && startedVal > 0) return true;
        else return false;
    }

    Integer getStartedVal() {
        return startedVal;
    }

    void setStartedVal(Integer startedVal) {
        this.startedVal = startedVal;
    }

    public boolean isTrial() {
        if(trialVal!=null && trialVal>0) return true;
        else return false;
    }

    Integer getTrialVal() {
        return trialVal;
    }

    void setTrialVal(Integer trialVal) {
        this.trialVal = trialVal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.object);
        dest.writeString(this.id);
        dest.writeString(this.period);
        dest.writeValue(this.periodDuration);
        dest.writeValue(this.paymentLimit);
        dest.writeValue(this.trialVal);
        dest.writeValue(this.startedVal);
        dest.writeValue(this.expiredVal);
        dest.writeValue(this.activeVal);
        dest.writeValue(this.dateStarted);
        dest.writeValue(this.dateNext);
    }

    public Subscription() {
    }

    protected Subscription(Parcel in) {
        this.object = in.readString();
        this.id = in.readString();
        this.period = in.readString();
        this.periodDuration = (Integer) in.readValue(Integer.class.getClassLoader());
        this.paymentLimit = (Long) in.readValue(Long.class.getClassLoader());
        this.trialVal = (Integer) in.readValue(Integer.class.getClassLoader());
        this.startedVal = (Integer) in.readValue(Integer.class.getClassLoader());
        this.expiredVal = (Integer) in.readValue(Integer.class.getClassLoader());
        this.activeVal = (Integer) in.readValue(Integer.class.getClassLoader());
        this.dateStarted = (Long) in.readValue(Long.class.getClassLoader());
        this.dateNext = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<Subscription> CREATOR = new Parcelable.Creator<Subscription>() {
        public Subscription createFromParcel(Parcel source) {
            return new Subscription(source);
        }

        public Subscription[] newArray(int size) {
            return new Subscription[size];
        }
    };

    @Override
    public String toString() {
        return "Subscription{" +
                "activeVal=" + activeVal +
                ", object='" + object + '\'' +
                ", id='" + id + '\'' +
                ", period='" + period + '\'' +
                ", periodDuration=" + periodDuration +
                ", paymentLimit=" + paymentLimit +
                ", trialVal=" + trialVal +
                ", startedVal=" + startedVal +
                ", expiredVal=" + expiredVal +
                ", dateStarted=" + dateStarted +
                ", dateNext=" + dateNext +
                '}';
    }
}
