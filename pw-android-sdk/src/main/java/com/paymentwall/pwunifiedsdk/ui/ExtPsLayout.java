package com.paymentwall.pwunifiedsdk.ui;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.io.Serializable;

/**
 * Created by nguyen.anh on 9/9/2016.
 */

public class ExtPsLayout extends LinearLayout {

    private String psId;
    private Parcelable params;

    public ExtPsLayout(Context context) {
        super(context);
    }

    public ExtPsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtPsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String getPsId() {
        return psId;
    }

    public void setPsId(String psId) {
        this.psId = psId;
    }

    public Parcelable getParams() {
        return params;
    }

    public void setParams(Parcelable params) {
        this.params = params;
    }
}
