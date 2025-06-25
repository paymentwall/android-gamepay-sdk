package com.paymentwall.pwunifiedsdk.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by nguyen.anh on 4/21/2017.
 */

public class ProximaNovaRegularTextView extends AppCompatTextView {

    public ProximaNovaRegularTextView(Context context) {
        super(context);
        setFont(context);

    }

    public ProximaNovaRegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    public ProximaNovaRegularTextView(Context context, AttributeSet attrs, int defStyle) {
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
