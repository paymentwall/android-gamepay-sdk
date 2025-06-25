package com.paymentwall.pwunifiedsdk.mobiamo.core;


import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.paymentwall.pwunifiedsdk.mobiamo.payment.PWSDKResponse;

public class MobiamoResponse extends PWSDKResponse implements Parcelable {
    private static final long serialVersionUID = -5431404693006176298L;
    public static final String STATUS_COMPLETED = "completed";
    private int messageStatus;
    private String transactionId;
    private String shortcode;
    private String keyword;
    private String regulatoryText;
    private String sign;
    private int success;
    private String status;
    private float amount;
    private String currencyCode;
    private String message;
    private String price;
    private String productId;
    private String productName;

    private boolean isSendSms;

    public String getMessage() {
        return message;
    }

    public void setMessage(String mMessage) {
        this.message = mMessage;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public int getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getShortcode() {
        return shortcode;
    }

    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getRegulatoryText() {
        return regulatoryText;
    }

    public void setRegulatoryText(String regulatoryText) {
        this.regulatoryText = regulatoryText;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public boolean getSendSms() {
        return isSendSms;
    }

    public void setSendSms(boolean value) {
        isSendSms = value;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public boolean isCompleted() {
        if (status != null && status.equals(STATUS_COMPLETED)) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.messageStatus);
        dest.writeString(this.transactionId);
        dest.writeString(this.shortcode);
        dest.writeString(this.keyword);
        dest.writeString(this.regulatoryText);
        dest.writeString(this.sign);
        dest.writeInt(this.success);
        dest.writeString(this.status);
        dest.writeFloat(this.amount);
        dest.writeString(this.currencyCode);
        dest.writeString(this.message);
        dest.writeString(this.price);
        dest.writeString(this.productId);
        dest.writeString(this.productName);
        dest.writeByte(this.isSendSms ? (byte) 1 : (byte) 0);
    }

    public MobiamoResponse() {
    }

    protected MobiamoResponse(Parcel in) {
        this.messageStatus = in.readInt();
        this.transactionId = in.readString();
        this.shortcode = in.readString();
        this.keyword = in.readString();
        this.regulatoryText = in.readString();
        this.sign = in.readString();
        this.success = in.readInt();
        this.status = in.readString();
        this.amount = in.readFloat();
        this.currencyCode = in.readString();
        this.message = in.readString();
        this.price = in.readString();
        this.productId = in.readString();
        this.productName = in.readString();
        this.isSendSms = in.readByte() != 0;
    }

    public static final Parcelable.Creator<MobiamoResponse> CREATOR = new Parcelable.Creator<MobiamoResponse>() {
        @Override
        public MobiamoResponse createFromParcel(Parcel source) {
            return new MobiamoResponse(source);
        }

        @Override
        public MobiamoResponse[] newArray(int size) {
            return new MobiamoResponse[size];
        }
    };

    @Override
    public String toString() {
        return "MobiamoResponse{" +
                "messageStatus=" + messageStatus +
                ", transactionId='" + transactionId + '\'' +
                ", shortcode='" + shortcode + '\'' +
                ", keyword='" + keyword + '\'' +
                ", regulatoryText='" + regulatoryText + '\'' +
                ", sign='" + sign + '\'' +
                ", success=" + success +
                ", status='" + status + '\'' +
                ", amount=" + amount +
                ", currencyCode='" + currencyCode + '\'' +
                ", message='" + message + '\'' +
                ", price='" + price + '\'' +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", isSendSms=" + isSendSms +
                '}';
    }

    public Uri toUri() {
        Uri.Builder uriBuilder = new Uri.Builder().scheme("mobiamo")
                .appendQueryParameter("transaction_id",transactionId)
                .appendQueryParameter("shortcode",shortcode)
                .appendQueryParameter("keyword",keyword)
                ;
        return uriBuilder.build();
    }

    public static MobiamoResponse fromUri(Uri uri) {
        if(uri == null) throw new NullPointerException("Cannot create MobiamoResponse from null Uri");
        if(uri.getScheme().equals("mobiamo")) return null;

        MobiamoResponse mobiamoResponse = new MobiamoResponse();
        mobiamoResponse.setTransactionId(uri.getQueryParameter("transaction_id"));
        mobiamoResponse.setShortcode(uri.getQueryParameter("shortcode"));
        mobiamoResponse.setKeyword(uri.getQueryParameter("keyword"));
        return mobiamoResponse;
    }

}
