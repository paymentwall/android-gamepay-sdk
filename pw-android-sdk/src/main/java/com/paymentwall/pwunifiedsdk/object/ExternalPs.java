package com.paymentwall.pwunifiedsdk.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.paymentwall.pwunifiedsdk.util.PwUtils;

import java.io.Serializable;

/**
 * Created by nguyen.anh on 9/9/2016.
 */

public class ExternalPs implements Parcelable {

    private String id;
    private String displayName;
    private int iconResId;
    private Parcelable params;

    public ExternalPs(String id, String displayName, int iconResId, Parcelable params) {
        this.id = id;
        this.displayName = displayName;
        this.iconResId = iconResId;
        this.params = params;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public Parcelable getParams() {
        return params;
    }

    public void setParams(Parcelable params) {
        this.params = params;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.displayName);
        dest.writeInt(this.iconResId);
        if (!this.id.equalsIgnoreCase("pwlocal")) {
            dest.writeParcelable(this.params, flags);
        }

    }

    protected ExternalPs(Parcel in) {
        this.id = in.readString();
        this.displayName = in.readString();
        this.iconResId = in.readInt();
        if (this.id.equalsIgnoreCase("pwlocal")) {
            this.params = null;
        } else {
            try {
                Class<?> PsClass = Class.forName("com.paymentwall." + (this.id + "Adapter.").toLowerCase() + "Ps" + PwUtils.capitalize(this.id));
                this.params = in.readParcelable(PsClass.getClassLoader());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static final Creator<ExternalPs> CREATOR = new Creator<ExternalPs>() {
        @Override
        public ExternalPs createFromParcel(Parcel source) {
            return new ExternalPs(source);
        }

        @Override
        public ExternalPs[] newArray(int size) {
            return new ExternalPs[size];
        }
    };
}
