package com.paymentwall.pwunifiedsdk.brick.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by nguyen.anh on 3/2/2016.
 */
public class BrickLoadingView extends View {

    public static final long DURATION_DEFAULT = 2000; //2000000ns

    Paint paintP1;

    public static final float HALF_CANVAS = 0.5f;
    public static final float DEFAULT_RADIUS = 0.02f;

    int w = -1;
    int h = -1;

    float k = 1 / 100f;
    private float r11, r12, r13, r21, r22, r23, r31, r32, r33;

    public long lastTime1, lastTime2, lastTime3, currentTime;

    public BrickLoadingView(Context context) {
        super(context);
        init();
    }

    public BrickLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BrickLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paintP1 = new Paint();
        paintP1.setColor(LIGHT_YELLOW);
        paintP1.setStyle(Paint.Style.FILL);
        paintP1.setAntiAlias(true);

        lastTime1 = SystemClock.elapsedRealtime(); //The 1st cirle appears immediately when the view is being drawn
        lastTime2 = lastTime1 + 100; // The 2nd circle appears after 1st one with 100ms delay
        lastTime3 = lastTime2 + 100; // The 3rd circle with 100ms delay too
        initAnimCircles();
    }

    private void initAnimCircles() {

        //Calculate coordination of animation circles
        c11_x = center_x + (float) (Math.sqrt((0.5 - h2_x) * (0.5 - h2_x) + (0.5 - h2_y) * (0.5 - h2_y)) * 3 / 2 * Math.cos(Math.toRadians(210)));
        c11_y = center_y - (float) (Math.sqrt((0.5 - h2_x) * (0.5 - h2_x) + (0.5 - h2_y) * (0.5 - h2_y)) * 3 / 2 * Math.sin(Math.toRadians(210)));
        c12_x = center_x + (float) (Math.sqrt((0.5 - h2_x) * (0.5 - h2_x) + (0.5 - h2_y) * (0.5 - h2_y)) * 4 / 2 * Math.cos(Math.toRadians(210)));
        c12_y = center_y - (float) (Math.sqrt((0.5 - h2_x) * (0.5 - h2_x) + (0.5 - h2_y) * (0.5 - h2_y)) * 4 / 2 * Math.sin(Math.toRadians(210)));
        c13_x = center_x + (float) (Math.sqrt((0.5 - h2_x) * (0.5 - h2_x) + (0.5 - h2_y) * (0.5 - h2_y)) * 5 / 2 * Math.cos(Math.toRadians(210)));
        c13_y = center_y - (float) (Math.sqrt((0.5 - h2_x) * (0.5 - h2_x) + (0.5 - h2_y) * (0.5 - h2_y)) * 5 / 2 * Math.sin(Math.toRadians(210)));

        c21_x = center_x + (float) (Math.sqrt((0.5 - h2_x) * (0.5 - h2_x) + (0.5 - h2_y) * (0.5 - h2_y)) * 3 / 2 * Math.cos(Math.toRadians(90)));
        c21_y = center_y - (float) (Math.sqrt((0.5 - h2_x) * (0.5 - h2_x) + (0.5 - h2_y) * (0.5 - h2_y)) * 3 / 2 * Math.sin(Math.toRadians(90)));
        c22_x = center_x + (float) (Math.sqrt((0.5 - h2_x) * (0.5 - h2_x) + (0.5 - h2_y) * (0.5 - h2_y)) * 4 / 2 * Math.cos(Math.toRadians(90)));
        c22_y = center_y - (float) (Math.sqrt((0.5 - h2_x) * (0.5 - h2_x) + (0.5 - h2_y) * (0.5 - h2_y)) * 4 / 2 * Math.sin(Math.toRadians(90)));
        c23_x = center_x + (float) (Math.sqrt((0.5 - h2_x) * (0.5 - h2_x) + (0.5 - h2_y) * (0.5 - h2_y)) * 5 / 2 * Math.cos(Math.toRadians(90)));
        c23_y = center_y - (float) (Math.sqrt((0.5 - h2_x) * (0.5 - h2_x) + (0.5 - h2_y) * (0.5 - h2_y)) * 5 / 2 * Math.sin(Math.toRadians(90)));

        c31_x = center_x + (float) (Math.sqrt((0.5 - h1_x) * (0.5 - h1_x) + (0.5 - h1_y) * (0.5 - h1_y)) * 3 / 2 * Math.cos(Math.toRadians(330)));
        c31_y = center_y - (float) (Math.sqrt((0.5 - h1_x) * (0.5 - h1_x) + (0.5 - h1_y) * (0.5 - h1_y)) * 3 / 2 * Math.sin(Math.toRadians(330)));
        c32_x = center_x + (float) (Math.sqrt((0.5 - h1_x) * (0.5 - h1_x) + (0.5 - h1_y) * (0.5 - h1_y)) * 4 / 2 * Math.cos(Math.toRadians(330)));
        c32_y = center_y - (float) (Math.sqrt((0.5 - h1_x) * (0.5 - h1_x) + (0.5 - h1_y) * (0.5 - h1_y)) * 4 / 2 * Math.sin(Math.toRadians(330)));
        c33_x = center_x + (float) (Math.sqrt((0.5 - h1_x) * (0.5 - h1_x) + (0.5 - h1_y) * (0.5 - h1_y)) * 5 / 2 * Math.cos(Math.toRadians(330)));
        c33_y = center_y - (float) (Math.sqrt((0.5 - h1_x) * (0.5 - h1_x) + (0.5 - h1_y) * (0.5 - h1_y)) * 5 / 2 * Math.sin(Math.toRadians(330)));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (w == -1 || h == -1) {
            w = getWidth();
            h = getHeight();
        }

        currentTime = SystemClock.elapsedRealtime();

        //Calculate radius for circles
        r11 = DEFAULT_RADIUS * (float) ((Math.sin(k * (currentTime - lastTime1)) + 1) / 2);
        r12 = currentTime >= lastTime2 ? DEFAULT_RADIUS * (float) ((Math.sin(k * (currentTime - lastTime2)) + 1) / 2) : 0;
        r13 = currentTime >= lastTime3 ? DEFAULT_RADIUS * (float) ((Math.sin(k * (currentTime - lastTime3)) + 1) / 2) : 0;

        r21 = DEFAULT_RADIUS * (float) ((Math.sin(k * (currentTime - lastTime1)) + 1) / 2);
        r22 = currentTime >= lastTime2 ? DEFAULT_RADIUS * (float) ((Math.sin(k * (currentTime - lastTime2)) + 1) / 2) : 0;
        r23 = currentTime >= lastTime3 ? DEFAULT_RADIUS * (float) ((Math.sin(k * (currentTime - lastTime3)) + 1) / 2) : 0;

        r31 = DEFAULT_RADIUS * (float) ((Math.sin(k * (currentTime - lastTime1)) + 1) / 2);
        r32 = currentTime >= lastTime2 ? DEFAULT_RADIUS * (float) ((Math.sin(k * (currentTime - lastTime2)) + 1) / 2) : 0;
        r33 = currentTime >= lastTime3 ? DEFAULT_RADIUS * (float) ((Math.sin(k * (currentTime - lastTime3)) + 1) / 2) : 0;

//        Log.i("RADIUS", r11 + "");
        canvas.drawCircle(c11_x * w, c11_y * h, r11 * w, paintP1);
        canvas.drawCircle(c12_x * w, c12_y * h, r12 * w, paintP1);
        canvas.drawCircle(c13_x * w, c13_y * h, r13 * w, paintP1);

        canvas.drawCircle(c21_x * w, c21_y * h, r21 * w, paintP1);
        canvas.drawCircle(c22_x * w, c22_y * h, r22 * w, paintP1);
        canvas.drawCircle(c23_x * w, c23_y * h, r23 * w, paintP1);

        canvas.drawCircle(c31_x * w, c31_y * h, r31 * w, paintP1);
        canvas.drawCircle(c32_x * w, c32_y * h, r32 * w, paintP1);
        canvas.drawCircle(c33_x * w, c33_y * h, r33 * w, paintP1);

        invalidate();
    }

    public static final int HEAVY_YELLOW = 0xffec8406;
    public static final int MID_YELLOW = 0xfffabc36;
    public static final int LIGHT_YELLOW = 0xffffdc47;

    public static final float h1_x = 0.5f;
    public static final float h2_x = 0.06699f + (float) ((0.5f - 0.06699f) * 2 / 3);
    public static final float h1_y = 0f + (0.5f * 2 / 3);
    public static final float h2_y = 0.75f - (float) ((0.75f - 0.5f) * 2 / 3);

    public static float c11_x, c11_y, c12_x, c12_y, c13_x, c13_y;
    public static float c21_x, c21_y, c22_x, c22_y, c23_x, c23_y;
    public static float c31_x, c31_y, c32_x, c32_y, c33_x, c33_y;

    public static final float center_x = 0.5f;
    public static final float center_y = 0.5f;

}

