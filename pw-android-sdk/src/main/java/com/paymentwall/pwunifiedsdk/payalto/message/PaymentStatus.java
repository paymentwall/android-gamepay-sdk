package com.paymentwall.pwunifiedsdk.payalto.message;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by paymentwall on 20/08/15.
 */
public class PaymentStatus implements Parcelable {

    /*
    Subscription
    [{
        "object":"payment",
        "id":"b123456",
        "created":1419438832,
        "amount":"9.99",
        "currency":"USD",
        "refunded":false,
        "risk":"approved",
        "uid":"user_200255",
        "product_id":"product_100244",
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
    }]

    One time payment
    [{
        "object":"payment",
        "id":"b12345678",
        "created":1400000000,
        "amount":"0.5000",
        "currency":"USD",
        "refunded":false,
        "risk":"approved",
        "uid":"abc",
        "product_id":"xyz"
    }]
    */


    static final String K_OBJECT = "object";
    static final String K_ID = "id";
    static final String K_CREATED = "created";
    static final String K_AMOUNT = "amount";
    static final String K_CURRENCY = "currency";
    static final String K_REFUNDED = "refunded";
    static final String K_RISK = "risk";
    static final String K_UID = "uid";
    static final String K_PRODUCT_ID= "product_id";
    static final String K_SUBSCRIPTION = "subscription";

    private String object;
    private String id;
    private Long dateCreated;
    private Double amount;
    private String currency;
    private Boolean refunded = false;
    private String riskStatus;
    private String uid;
    private String productId;
    private Subscription subscription;

    protected PaymentStatus() {
    }

    public Double getAmount() {
        return amount;
    }

    void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getDateCreated() {
        return dateCreated;
    }

    void setDateCreated(Long dateCreated) {
        this.dateCreated = dateCreated;
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

    public String getProductId() {
        return productId;
    }

    void setProductId(String productId) {
        this.productId = productId;
    }

    public Boolean getRefunded() {
        return refunded;
    }

    void setRefunded(Boolean refunded) {
        this.refunded = refunded;
    }

    public String getRiskStatus() {
        return riskStatus;
    }

    void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public String getUid() {
        return uid;
    }

    void setUid(String uid) {
        this.uid = uid;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.object);
        dest.writeString(this.id);
        dest.writeValue(this.dateCreated);
        dest.writeValue(this.amount);
        dest.writeString(this.currency);
        dest.writeValue(this.refunded);
        dest.writeString(this.riskStatus);
        dest.writeString(this.uid);
        dest.writeString(this.productId);
        dest.writeParcelable(this.subscription, 0);
    }

    protected PaymentStatus(Parcel in) {
        this.object = in.readString();
        this.id = in.readString();
        this.dateCreated = (Long) in.readValue(Long.class.getClassLoader());
        this.amount = (Double) in.readValue(Double.class.getClassLoader());
        this.currency = in.readString();
        this.refunded = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.riskStatus = in.readString();
        this.uid = in.readString();
        this.productId = in.readString();
        this.subscription = in.readParcelable(Subscription.class.getClassLoader());
    }

    public static final Parcelable.Creator<PaymentStatus> CREATOR = new Parcelable.Creator<PaymentStatus>() {
        public PaymentStatus createFromParcel(Parcel source) {
            return new PaymentStatus(source);
        }
        public PaymentStatus[] newArray(int size) {
            return new PaymentStatus[size];
        }
    };

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PaymentStatus{");
        sb.append("amount=").append(amount);
        sb.append(", object='").append(object).append('\'');
        sb.append(", id='").append(id).append('\'');
        sb.append(", dateCreated=").append(dateCreated);
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", refunded=").append(refunded);
        sb.append(", riskStatus='").append(riskStatus).append('\'');
        sb.append(", uid='").append(uid).append('\'');
        sb.append(", productId='").append(productId).append('\'');
        sb.append(", subscription=").append(subscription);
        sb.append('}');
        return sb.toString();
    }
}
