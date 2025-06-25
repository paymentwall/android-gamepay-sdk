package com.paymentwall.pwunifiedsdk.brick.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by nguyen.anh on 5/9/2016.
 */
public class RobotoRegularEditText extends EditText {

    public RobotoRegularEditText(Context context) {
        super(context);
        setFont(context);
    }

    public RobotoRegularEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    public RobotoRegularEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont(context);
    }

    private void setFont(Context context) {
//        Typeface face = Typeface.createFromAsset(context.getAssets(),
//                "fonts/Roboto-Regular.ttf");
//        this.setTypeface(face);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

}
