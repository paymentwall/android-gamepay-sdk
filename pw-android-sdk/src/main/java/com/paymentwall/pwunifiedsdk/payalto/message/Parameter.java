package com.paymentwall.pwunifiedsdk.payalto.message;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by paymentwall on 14/09/15.
 */
class Parameter implements Parcelable {
    private String key;
    private String value;

    public Parameter() {
    }

    public Parameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.value);
    }

    protected Parameter(Parcel in) {
        this.key = in.readString();
        this.value = in.readString();
    }

    public static final Parcelable.Creator<Parameter> CREATOR = new Parcelable.Creator<Parameter>() {
        public Parameter createFromParcel(Parcel source) {
            return new Parameter(source);
        }

        public Parameter[] newArray(int size) {
            return new Parameter[size];
        }
    };
}
