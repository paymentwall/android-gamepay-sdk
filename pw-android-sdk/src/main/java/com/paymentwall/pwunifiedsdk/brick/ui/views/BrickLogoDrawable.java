package com.paymentwall.pwunifiedsdk.brick.ui.views;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by nguyen.anh on 3/4/2016.
 */
public class BrickLogoDrawable extends Drawable {

    Paint paintP1, paintP2, paintP3, paintP4, paintP5, paintP6;
    Path pathP1, pathP2, pathP3, pathP4, pathP5, pathP6;

    int w = -1;
    int h = -1;

    public BrickLogoDrawable() {
        init();
    }

    private void init() {
        paintP1 = new Paint();
        paintP2 = new Paint();
        paintP3 = new Paint();
        paintP4 = new Paint();
        paintP5 = new Paint();
        paintP6 = new Paint();
        pathP1 = new Path();
        pathP2 = new Path();
        pathP3 = new Path();
        pathP4 = new Path();
        pathP5 = new Path();
        pathP6 = new Path();

        paintP1.setColor(MID_YELLOW);
        paintP2.setColor(LIGHT_YELLOW);
        paintP3.setColor(HEAVY_YELLOW);
        paintP4.setColor(HEAVY_YELLOW);
        paintP5.setColor(MID_YELLOW);
        paintP6.setColor(LIGHT_YELLOW);

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
        Log.i("LOGO", "Drawing logo");
        //draw Path1
        pathP1.moveTo(h5_x * w, h5_y * h);
        pathP1.lineTo(h6_x * w, h6_y * h);
        pathP1.lineTo(m6_x * w, m6_y * h);
        pathP1.lineTo(m5_x * w, m5_y * h);
        pathP1.lineTo(m4_x * w, m4_y * h);
        pathP1.lineTo(h4_x * w, h4_y * h);
        pathP1.close();
        canvas.drawPath(pathP1, paintP1);

        //draw Path2
        pathP2.moveTo(h6_x * w, h6_y * h);
        pathP2.lineTo(h1_x * w, h1_y * h);
        pathP2.lineTo(h2_x * w, h2_y * h);
        pathP2.lineTo(m2_x * w, m2_y * h);
        pathP2.lineTo(m1_x * w, m1_y * h);
        pathP2.lineTo(m6_x * w, m6_y * h);
        pathP2.close();
        canvas.drawPath(pathP2, paintP2);

        //draw Path3
        pathP3.moveTo(h2_x * w, h2_y * h);
        pathP3.lineTo(h3_x * w, h3_y * h);
        pathP3.lineTo(h4_x * w, h4_y * h);
        pathP3.lineTo(m4_x * w, m4_y * h);
        pathP3.lineTo(m3_x * w, m3_y * h);
        pathP3.lineTo(m2_x * w, m2_y * h);
        pathP3.close();
        canvas.drawPath(pathP3, paintP3);

        //draw Path4
        pathP4.moveTo(m5_x * w, m5_y * h);
        pathP4.lineTo(m6_x * w, m6_y * h);
        pathP4.lineTo(m1_x * w, m1_y * h);
        pathP4.lineTo(center_x * w, center_y * h);
        pathP4.close();
        canvas.drawPath(pathP4, paintP4);

        //draw Path5
        pathP5.moveTo(m1_x * w, m1_y * h);
        pathP5.lineTo(m2_x * w, m2_y * h);
        pathP5.lineTo(m3_x * w, m3_y * h);
        pathP5.lineTo(center_x * w, center_y * h);
        pathP5.close();
        canvas.drawPath(pathP5, paintP5);

        //draw Path6
        pathP6.moveTo(m3_x * w, m3_y * h);
        pathP6.lineTo(m4_x * w, m4_y * h);
        pathP6.lineTo(m5_x * w, m5_y * h);
        pathP6.lineTo(center_x * w, center_y * h);
        pathP6.close();
        canvas.drawPath(pathP6, paintP6);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }



    public static final int HEAVY_YELLOW = 0xffec8406;

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        w = bounds.width();
        h = bounds.height();
    }
    public static final int MID_YELLOW = 0xfffabc36;
    public static final int LIGHT_YELLOW = 0xffffdc47;

    public static final float h1_x = 0.5f;
    public static final float h2_x = 0.93302f - (float) ((0.93302f - 0.5f) * 2 / 3);
    public static final float h3_x = 0.93302f - (float) ((0.93302f - 0.5f) * 2 / 3);
    public static final float h4_x = 0.5f;
    public static final float h5_x = 0.06699f + (float) ((0.5f - 0.06699f) * 2 / 3);
    public static final float h6_x = 0.06699f + (float) ((0.5f - 0.06699f) * 2 / 3);
    public static final float h1_y = 0f + (0.5f * 2 / 3);
    public static final float h2_y = 0.24999f + (float) ((0.5f - 0.24999f) * 2 / 3);
    public static final float h3_y = 0.74999f - (float) ((0.74999f - 0.5f) * 2 / 3);
    public static final float h4_y = 1 - (0.5f * 2 / 3);
    public static final float h5_y = 0.75f - (float) ((0.75f - 0.5f) * 2 / 3);
    public static final float h6_y = 0.25f + (float) ((0.5f - 0.25f) * 2 / 3);

    public static final float m1_x = 0.5f;
    public static final float m2_x = 0.71651f - (float) ((0.71651f - 0.5f) * 2 / 3);
    public static final float m3_x = 0.71651f - (float) ((0.71651f - 0.5f) * 2 / 3);
    public static final float m4_x = 0.5f;
    public static final float m5_x = 0.28349f + (float) ((0.5f - 0.28349f) * 2 / 3);
    public static final float m6_x = 0.28350f + (float) ((0.5f - 0.28349f) * 2 / 3);
    public static final float m1_y = 0.25f + (float) ((0.5f - 0.25f) * 2 / 3);
    public static final float m2_y = 0.375f + (float) ((0.5f - 0.375f) * 2 / 3);
    public static final float m3_y = 0.625f - (float) ((0.625f - 0.5f) * 2 / 3);
    public static final float m4_y = 0.75f - (float) ((0.75f - 0.5f) * 2 / 3);
    public static final float m5_y = 0.625f - (float) ((0.625 - 0.5f) * 2 / 3);
    public static final float m6_y = 0.375f + (float) ((0.5f - 0.375f) * 2 / 3);

    public static final float center_x = 0.5f;
    public static final float center_y = 0.5f;
}
