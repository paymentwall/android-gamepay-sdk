package com.paymentwall.pwunifiedsdk.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * Created by nguyen.anh on 4/24/2017.
 */

public class ProximaNovaRegularEditText extends AppCompatEditText {

    public ProximaNovaRegularEditText(Context context) {
        super(context);
        setFont(context);
    }

    public ProximaNovaRegularEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    public ProximaNovaRegularEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont(context);
    }

    private void setFont(Context context) {
//        Typeface typeface = ResourcesCompat.getFont(context, R.font.default_font);
//        this.setTypeface(typeface);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
