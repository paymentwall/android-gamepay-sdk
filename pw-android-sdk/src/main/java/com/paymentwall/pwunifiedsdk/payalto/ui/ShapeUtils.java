package com.paymentwall.pwunifiedsdk.payalto.ui;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.PathShape;

/**
 * Created by harvey on 4/27/17.
 */

public class ShapeUtils {
    private static final float[] BACK_X = new float[] {24, 16, 24, 25.41f, 19.83f, 32, 32, 19.83f, 25.42f};
    private static final float[] BACK_Y = new float[] {16, 24, 32, 30.59f, 25, 25, 23, 23, 17.41f};
    private static final float[] CLOSE_X = new float[] {17, 18.41f, 24, 29.59f, 31, 25.41f, 31, 29.59f, 24, 18.41f, 17, 22.59f};
    private static final float[] CLOSE_Y = new float[] {18.41f, 17, 22.59f, 17, 18.41f, 24, 29.59f, 31, 25.41f, 31, 29.59f, 24};
    private static final float[] PWLOGO_BRICK_X = new float[] {8.6f, 2.3f, -13.2f};
    private static final float[] PWLOGO_BRICK_Y = new float[] {0, 6.4f, 0f};
    private static final float[] PWLOGO_START_X = new float[] {19.75f, 12.75f, 26.75f, 5.75f, 19.75f, 33.75f};
    private static final float[] PWLOGO_START_Y = new float[] {15.3f, 22.8f, 22.8f, 30.3f, 30.3f, 30.3f};

    private static PathShape getLogoShape(int height, int width) {
        Path path = new Path();
        for (int i = 0; i < PWLOGO_START_X.length; i++) {
            path.moveTo(PWLOGO_START_X[i]  * width / 48f, PWLOGO_START_Y[i] * height / 48f);
            path.rLineTo(PWLOGO_BRICK_X[0] * width / 48f, PWLOGO_BRICK_Y[0] * height / 48f);
            path.rLineTo(PWLOGO_BRICK_X[1] * width / 48f, PWLOGO_BRICK_Y[1] * height / 48f);
            path.rLineTo(PWLOGO_BRICK_X[2] * width / 48f, PWLOGO_BRICK_Y[2] * height / 48f);
            path.close();
        }
        return new PathShape(path, height, width);
    }

    public static ShapeDrawable getLogoDrawable(int color, int height, int width) {
        ShapeDrawable shapeDrawable = new ShapeDrawable();

        shapeDrawable.setShape(getLogoShape(height, width));
        shapeDrawable.getPaint().setColor(color);
        shapeDrawable.getPaint().setAntiAlias(true);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable.setIntrinsicHeight(height);
        shapeDrawable.setIntrinsicWidth(width);

        return shapeDrawable;
    }

    private static PathShape getBackButtonShape(int height, int width) {
        Path path = new Path();
        path.moveTo(BACK_X[0] * height / 48f, BACK_Y[0] * width / 48f);
        for(int i = 1; i < BACK_X.length; i++) {
            path.lineTo(BACK_X[i] * height / 48f, BACK_Y[i] * width / 48f);
        }
        path.close();
        return new PathShape(path, height, width);
    }

    private static PathShape getCloseButtonShape(int height, int width) {
        Path path = new Path();
        path.moveTo(CLOSE_X[0] * height / 48f, CLOSE_Y[0] * width / 48f);
        for(int i = 1; i < CLOSE_X.length; i++) {
            path.lineTo(CLOSE_X[i] * height / 48f, CLOSE_Y[i] * width / 48f);
        }
        path.close();
        return new PathShape(path, height, width);
    }

    public static ShapeDrawable getCloseButtonDrawable(int color, int height, int width) {
        ShapeDrawable shapeDrawable = new ShapeDrawable();

        shapeDrawable.setShape(getCloseButtonShape(height, width));
        shapeDrawable.getPaint().setColor(color);
        shapeDrawable.getPaint().setAntiAlias(true);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable.setIntrinsicHeight(height);
        shapeDrawable.setIntrinsicWidth(width);

        return shapeDrawable;
    }

    public static ShapeDrawable getBackButtonDrawable(int color, int height, int width) {
        ShapeDrawable shapeDrawable = new ShapeDrawable();

        shapeDrawable.setShape(getBackButtonShape(height, width));
        shapeDrawable.getPaint().setColor(color);
        shapeDrawable.getPaint().setAntiAlias(true);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable.setIntrinsicHeight(height);
        shapeDrawable.setIntrinsicWidth(width);

        return shapeDrawable;
    }

    public static StateListDrawable getButtonBackground(int colorEnable, int colorPressed) {
        StateListDrawable btnBackground = new StateListDrawable();
        ColorDrawable colorPressedDrawable = new ColorDrawable(colorPressed);
        ColorDrawable colorEnableDrawable = new ColorDrawable(colorEnable);
        int[] statePressed = {android.R.attr.state_pressed};
        int[] stateEnabled = {android.R.attr.state_enabled};
        btnBackground.addState(statePressed, colorPressedDrawable);
        btnBackground.addState(stateEnabled, colorEnableDrawable);
        return btnBackground;
    }
}
