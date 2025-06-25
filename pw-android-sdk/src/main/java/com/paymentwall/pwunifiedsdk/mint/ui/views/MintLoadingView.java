package com.paymentwall.pwunifiedsdk.mint.ui.views;

/**
 * Created by nguyen.anh on 3/1/2016.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;


public class MintLoadingView extends View {

    public static final long DURATION_DEFAULT = 2000; //2000000ns

    //the last update and the current update moments
    private long lastTime, currentTime;

    Paint paintBGR, paintP1, paintP2, paintP3, paintP4, paintP5, paintP6;
    Path pathBGR, pathP1, pathP2, pathP3, pathP4, pathP5, pathP6;

    //indicate if we're done drawing leaves layer 1,2,3 from bottom to top correspondingly
    private boolean doneStep1, doneStep2, doneStep3, isPausing;

    int w = -1;
    int h = -1;

    public MintLoadingView(Context context) {
        super(context);
        init();
    }

    public MintLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MintLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
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
        paintP1.setColor(COLOR_P1);
        paintP2.setColor(COLOR_P2);
        paintP3.setColor(COLOR_P3);
        paintP4.setColor(COLOR_P4);
        paintP5.setColor(COLOR_P5);
        paintP6.setColor(COLOR_P6);

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

        lastTime = SystemClock.elapsedRealtime();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (w == -1 || h == -1) {
            w = getWidth();
            h = getHeight();
        }
        pathBGR.moveTo(h1_x * w, h1_y * h);
        pathBGR.lineTo(h2_x * w, h2_y * h);
        pathBGR.lineTo(h3_x * w, h3_y * h);
        pathBGR.lineTo(h4_x * w, h4_y * h);
        pathBGR.lineTo(h5_x * w, h5_y * h);
        pathBGR.lineTo(h6_x * w, h6_y * h);
        pathBGR.close();

        canvas.drawPath(pathBGR, paintBGR);

        currentTime = SystemClock.elapsedRealtime();

        //if the layer1 is not drawn completely, continue moving it up
        if (!doneStep1) {
            if (l12_y > l12_y_end) {
                l12_y -= (l12_y_start - l12_y_end) * (currentTime - lastTime) / (DURATION_DEFAULT / 4);
                if (l12_y < l12_y_end) l12_y = l12_y_end;
            }

            if (l13_y > l13_y_end) {
                l13_y -= (l13_y_start - l13_y_end) * (currentTime - lastTime) / (DURATION_DEFAULT / 4);
                if (l13_y < l13_y_end) l13_y = l13_y_end;
            }

            if (l14_y > l14_y_end) {
                l14_y -= (l14_y_start - l14_y_end) * (currentTime - lastTime) / (DURATION_DEFAULT / 4);
                if (l14_y < l14_y_end) l14_y = l14_y_end;
            }

            if (l12_y == l12_y_end && l13_y == l13_y_end && l14_y == l14_y_end) {
                doneStep1 = true;
            }
        }

        //if the layer 1 is done and the layer2 is not drawn completely, continue moving the layer2 up
        if (doneStep1 && !doneStep2) {

            if (l22_y > l22_y_end) {
                l22_y -= (l22_y_start - l22_y_end) * (currentTime - lastTime) / (DURATION_DEFAULT / 4);
                if (l22_y < l22_y_end) l22_y = l22_y_end;
            }

            if (l23_y > l23_y_end) {
                l23_y -= (l23_y_start - l23_y_end) * (currentTime - lastTime) / (DURATION_DEFAULT / 4);
                if (l23_y < l23_y_end) l23_y = l23_y_end;
            }

            if (l24_y > l24_y_end) {
                l24_y -= (l24_y_start - l24_y_end) * (currentTime - lastTime) / (DURATION_DEFAULT / 4);
                if (l24_y < l24_y_end) l24_y = l24_y_end;
            }


            if (l22_y == l22_y_end && l23_y == l23_y_end && l24_y == l24_y_end) {
                doneStep2 = true;
            }
        }

        //if the layer 1,2 are done and the layer3 is not drawn completely, continue moving the layer3 up
        if (doneStep1 && doneStep2 && !doneStep3) {

            if (l32_y > l32_y_end) {
                l32_y -= (l32_y_start - l32_y_end) * (currentTime - lastTime) / (DURATION_DEFAULT / 4);
                if (l32_y < l32_y_end) l32_y = l32_y_end;
            }

            if (l33_y > l33_y_end) {
                l33_y -= (l33_y_start - l33_y_end) * (currentTime - lastTime) / (DURATION_DEFAULT / 4);
                if (l33_y < l33_y_end) l33_y = l33_y_end;
            }

            if (l34_y > l34_y_end) {
                l34_y -= (l34_y_start - l34_y_end) * (currentTime - lastTime) / (DURATION_DEFAULT / 4);
                if (l34_y < l34_y_end) l34_y = l34_y_end;
            }

            if (l32_y == l32_y_end && l33_y == l33_y_end && l34_y == l34_y_end) {
                doneStep3 = true;
                isPausing = true;
            }
        }

        if (!doneStep3 || isPausing) {
            //draw path1
            pathP1.moveTo(l11_x * w, l11_y * h);
            pathP1.lineTo(l12_x * w, l12_y * h);
            pathP1.lineTo(l13_x * w, l13_y * h);
            pathP1.lineTo(h4_x * w, h4_y * h);
            pathP1.close();
            canvas.drawPath(pathP1, paintP1);

            //draw path2
            pathP2.moveTo(l13_x * w, l13_y * h);
            pathP2.lineTo(l14_x * w, l14_y * h);
            pathP2.lineTo(l15_x * w, l15_y * h);
            pathP2.lineTo(h4_x * w, h4_y * h);
            pathP2.close();
            canvas.drawPath(pathP2, paintP2);

            //if path1 and path2 are done, draw path3 and path4
            if (doneStep1) {
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
            }

            //if path1,2,3,4 are done, draw path5 and path6
            if (doneStep1 && doneStep2) {
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
            }

            if (!isPausing)
                lastTime = currentTime;
        }

        if (isPausing) {
            if (currentTime - lastTime >= DURATION_DEFAULT / 4) {
                repeatAnimation();
            }
        }

        postInvalidate();
    }

    public void repeatAnimation() {
        lastTime = SystemClock.elapsedRealtime();
        doneStep1 = false;
        doneStep2 = false;
        doneStep3 = false;
        isPausing = false;
        pathBGR.reset();
        pathP1.reset();
        pathP2.reset();
        pathP3.reset();
        pathP4.reset();
        pathP5.reset();
        pathP6.reset();
        resetParams();
    }

    public void resetParams() {
        l12_y = l11_y;
        l13_y = h4_y;
        l14_y = l15_y;
        l22_y = l21_y;
        l23_y = l13_y_end;
        l24_y = l25_y;
        l32_y = l31_y;
        l33_y = l23_y_end;
        l34_y = l35_y;
    }

    public static final int COLOR_BACKGROUND = 0xffd9eac1;
    public static final int COLOR_P1 = 0xff6eae44;
    public static final int COLOR_P2 = 0xff589d44;
    public static final int COLOR_P3 = 0xff7fb542;
    public static final int COLOR_P4 = 0xff6eae44;
    public static final int COLOR_P5 = 0xff88c14f;
    public static final int COLOR_P6 = 0xff68b345;

    public static final float h1_x = 0.5f;
    public static final float h2_x = 0.93301f;
    public static final float h3_x = 0.93301f;
    public static final float h4_x = 0.5f;
    public static final float h5_x = 0.06699f;
    public static final float h6_x = 0.06699f;
    public static final float h1_y = 0f;
    public static final float h2_y = 0.25f;
    public static final float h3_y = 0.75f;
    public static final float h4_y = 1f;
    public static final float h5_y = 0.75f;
    public static final float h6_y = 0.25f;

    public final float l11_x = 0.17777f;
    public final float l12_x = 0.17777f;
    public final float l13_x = 0.5f;
    public final float l14_x = 0.82223f;
    public final float l15_x = 0.82223f;
    public final float l21_x = 0.23485f;
    public final float l22_x = 0.23485f;
    public final float l23_x = 0.5f;
    public final float l24_x = 0.76515f;
    public final float l25_x = 0.76515f;
    public final float l31_x = 0.28641f;
    public final float l32_x = 0.28641f;
    public final float l33_x = 0.5f;
    public final float l34_x = 0.71359f;
    public final float l35_x = 0.71359f;

    public final float l11_y = 0.81393f;
    public float l12_y = l11_y;
    public final float l12_y_start = l11_y;
    public final float l12_y_end = 0.56386f;
    public float l13_y = h4_y;
    public final float l13_y_start = h4_y;
    public final float l13_y_end = 0.75f;

    public final float l15_y = 0.81393f;
    public float l14_y = l15_y;
    public final float l14_y_start = l15_y;
    public final float l14_y_end = 0.56386f;

    public final float l21_y = 0.59687f;
    public float l22_y = l21_y;
    public final float l22_y_start = l21_y;
    public final float l22_y_end = 0.36198f;
    public float l23_y = l13_y_end;
    public final float l23_y_start = l13_y_end;
    public final float l23_y_end = 0.50924f;

    public final float l25_y = 0.59687f;
    public float l24_y = l25_y;
    public final float l24_y_start = l25_y;
    public final float l24_y_end = 0.36198f;


    public final float l31_y = 0.39605f;
    public float l32_y = l31_y;
    public final float l32_y_start = l31_y;
    public final float l32_y_end = 0.2487f;

    public float l33_y = l23_y_end;
    public final float l33_y_start = l23_y_end;
    public final float l33_y_end = 0.12476f;
    public final float l35_y = 0.39605f;
    public float l34_y = l35_y;
    public final float l34_y_start = l35_y;
    public final float l34_y_end = 0.2487f;
}
