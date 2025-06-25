package com.paymentwall.pwunifiedsdk.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by nguyen.anh on 4/21/2017.
 */

public class ProximaNovaLightTextView extends TextView {

    public ProximaNovaLightTextView(Context context) {
        super(context);
        setFont(context);
    }

    public ProximaNovaLightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    public ProximaNovaLightTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont(context);
    }

    private void setFont(Context context) {
//        Typeface face = Typeface.createFromAsset(context.getAssets(),
//                "fonts/ProximaNova-Light.otf");
//        this.setTypeface(face);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
