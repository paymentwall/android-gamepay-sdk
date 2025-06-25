package com.paymentwall.pwunifiedsdk.brick.core;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by paymentwall on 28/05/15.
 */
public class BrickError extends Exception{
    private BrickResponse response;

    public enum Kind {
        INVALID,
        NETWORK,
        HTTP,
        REJECTED,
        SERVER,
        UNEXPECTED,
    }
    private Kind kind = Kind.INVALID;

    public BrickResponse getResponse() {
        return response;
    }

    public void setResponse(BrickResponse response) {
        this.response = response;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public BrickError(Kind kind) {
        this.kind = kind;
    }

    public BrickError(String detailMessage, Kind kind) {
        super(detailMessage);
        this.kind = kind;
    }
}
