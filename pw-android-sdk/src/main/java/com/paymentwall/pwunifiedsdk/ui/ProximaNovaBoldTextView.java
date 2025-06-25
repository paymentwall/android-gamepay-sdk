package com.paymentwall.pwunifiedsdk.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by nguyen.anh on 4/21/2017.
 */

public class ProximaNovaBoldTextView extends AppCompatTextView {

    public ProximaNovaBoldTextView(Context context) {
        super(context);
        setFont(context);
    }

    public ProximaNovaBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    public ProximaNovaBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont(context);
    }

    private void setFont(Context context) {
//        Typeface face = Typeface.createFromAsset(context.getAssets(),
//                "fonts/ProximaNova-Bold.otf");
//        this.setTypeface(face);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
