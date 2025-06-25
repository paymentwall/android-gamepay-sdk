package com.paymentwall.pwunifiedsdk.mint.ui.views;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by nguyen.anh on 3/10/2016.
 */
public class MintLogoHelp extends Drawable {

    Paint paintBGR, paintP1, paintP2, paintP3, paintP4, paintP5, paintP6;
    Path pathBGR, pathP1, pathP2, pathP3, pathP4, pathP5, pathP6;

    int w = -1;
    int h = -1;

    public MintLogoHelp(){
        init();
    }

    private void init(){
        paintBGR = new Paint();
        paintP1 = new Paint();
        paintP2 = new Paint();
        paintP3 = new Paint();
        paintP4 = new Paint();
        paintP5 = new Paint();
        paintP6 = new Paint();
        pathBGR = new Path();
        pathP1 = new Path();
        pathP2 = new Path();
        pathP3 = new Path();
        pathP4 = new Path();
        pathP5 = new Path();
        pathP6 = new Path();

        paintBGR.setColor(COLOR_BACKGROUND);
        paintBGR.setAlpha(255*28/100);
        paintP1.setColor(COLOR_P1);
        paintP1.setAlpha(255*28/100);
        paintP2.setColor(COLOR_P2);
        paintP2.setAlpha(255*28/100);
        paintP3.setColor(COLOR_P3);
        paintP3.setAlpha(255*28/100);
        paintP4.setColor(COLOR_P4);
        paintP4.setAlpha(255*28/100);
        paintP5.setColor(COLOR_P5);
        paintP5.setAlpha(255*28/100);
        paintP6.setColor(COLOR_P6);
        paintP6.setAlpha(255*28/100);

        paintBGR.setStyle(Paint.Style.FILL);
        paintBGR.setAntiAlias(true);
        paintP1.setStyle(Paint.Style.FILL);
        paintP1.setAntiAlias(true);
        paintP2.setStyle(Paint.Style.FILL);
        paintP2.setAntiAlias(true);
        paintP3.setStyle(Paint.Style.FILL);
        paintP3.setAntiAlias(true);
        paintP4.setStyle(Paint.Style.FILL);
        paintP4.setAntiAlias(true);
        paintP5.setStyle(Paint.Style.FILL);
        paintP5.setAntiAlias(true);
        paintP6.setStyle(Paint.Style.FILL);
        paintP6.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas) {
        pathBGR.moveTo(h1_x * w, h1_y * h);
        pathBGR.lineTo(h2_x * w, h2_y * h);
        pathBGR.lineTo(h3_x * w, h3_y * h);
        pathBGR.lineTo(h4_x * w, h4_y * h);
        pathBGR.lineTo(h5_x * w, h5_y * h);
        pathBGR.lineTo(h6_x * w, h6_y * h);
        pathBGR.close();

        canvas.drawPath(pathBGR, paintBGR);

        pathP5.moveTo(l31_x * w, l31_y * h);
        pathP5.lineTo(l32_x * w, l32_y * h);
        pathP5.lineTo(l33_x * w, l33_y * h);
        pathP5.lineTo(l23_x * w, l23_y * h);
        pathP5.close();

        canvas.drawPath(pathP5, paintP5);

        pathP6.moveTo(l33_x * w, l33_y * h);
        pathP6.lineTo(l34_x * w, l34_y * h);
        pathP6.lineTo(l35_x * w, l35_y * h);
        pathP6.lineTo(l23_x * w, l23_y * h);
        pathP6.close();

        canvas.drawPath(pathP6, paintP6);

        pathP3.moveTo(l21_x * w, l21_y * h);
        pathP3.lineTo(l22_x * w, l22_y * h);
        pathP3.lineTo(l23_x * w, l23_y * h);
        pathP3.lineTo(l13_x * w, l13_y * h);
        pathP3.close();

        canvas.drawPath(pathP3, paintP3);

        pathP4.moveTo(l23_x * w, l23_y * h);
        pathP4.lineTo(l24_x * w, l24_y * h);
        pathP4.lineTo(l25_x * w, l25_y * h);
        pathP4.lineTo(l13_x * w, l13_y * h);
        pathP4.close();

        canvas.drawPath(pathP4, paintP4);

        pathP1.moveTo(l11_x * w, l11_y * h);
        pathP1.lineTo(l12_x * w, l12_y * h);
        pathP1.lineTo(l13_x * w, l13_y * h);
        pathP1.lineTo(h4_x * w, h4_y * h);
        pathP1.close();

        canvas.drawPath(pathP1, paintP1);

        pathP2.moveTo(l13_x * w, l13_y * h);
        pathP2.lineTo(l14_x * w, l14_y * h);
        pathP2.lineTo(l15_x * w, l15_y * h);
        pathP2.lineTo(h4_x * w, h4_y * h);
        pathP2.close();

        canvas.drawPath(pathP2, paintP2);

    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        w = bounds.width();
        h = bounds.height();
    }

    public static final int COLOR_BACKGROUND = 0xffd9eac1;
    public static final int COLOR_P1 = 0xff6eae44;
    public static final int COLOR_P2 = 0xff589d44;
    public static final int COLOR_P3 = 0xff7fb542;
    public static final int COLOR_P4 = 0xff6eae44;
    public static final int COLOR_P5 = 0xff88c14f;
    public static final int COLOR_P6 = 0xff68b345;

    public static final float l11_x=0.17777f;
    public static final float l12_x=0.17777f;
    public static final float l13_x=0.5f;
    public static final float l14_x=0.82223f;
    public static final float l15_x=0.82223f;
    public static final float l21_x=0.23485f;
    public static final float l22_x=0.23485f;
    public static final float l23_x=0.5f;
    public static final float l24_x=0.76515f;
    public static final float l25_x=0.76515f;
    public static final float l31_x=0.28641f;
    public static final float l32_x=0.28641f;
    public static final float l33_x=0.5f;
    public static final float l34_x=0.71359f;
    public static final float l35_x=0.71359f;

    public static final float l11_y=0.81393f;
    public static final float l12_y=0.56386f;
    public static final float l13_y=0.75f;
    public static final float l14_y=0.56386f;
    public static final float l15_y=0.81393f;
    public static final float l21_y=0.59687f;
    public static final float l22_y=0.36198f;
    public static final float l23_y=0.50924f;
    public static final float l24_y=0.36198f;
    public static final float l25_y=0.59687f;
    public static final float l31_y=0.39605f;
    public static final float l32_y=0.2487f;
    public static final float l33_y=0.12476f;
    public static final float l34_y=0.2487f;
    public static final float l35_y=0.39605f;


    public static final float h1_x=0.5f;
    public static final float h2_x=0.93301f;
    public static final float h3_x=0.93301f;
    public static final float h4_x=0.5f;
    public static final float h5_x=0.06699f;
    public static final float h6_x=0.06699f;
    public static final float h1_y=0f;
    public static final float h2_y=0.25f;
    public static final float h3_y=0.75f;
    public static final float h4_y=1f;
    public static final float h5_y=0.75f;
    public static final float h6_y=0.25f;
}
