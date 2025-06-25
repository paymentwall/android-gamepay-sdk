package com.paymentwall.pwunifiedsdk.mint.ui.views;
// TODO Include your package name here

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

public class MintLogoNew {
    private static final Paint p = new Paint();
    private static final Paint ps = new Paint();
    private static final Path t = new Path();
    private static final Matrix m = new Matrix();
    private static float od;
    protected static ColorFilter cf = null;

    /**
     * IMPORTANT: Due to the static usage of this class this
     * method sets the tint color statically. So it is highly
     * recommended to call the clearColorTint method when you
     * have finished drawing.
     * <p>
     * Sets the color to use when drawing the SVG. This replaces
     * all parts of the drawable which are not completely
     * transparent with this color.
     */
    public static void setColorTint(int color) {
        cf = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    public static void clearColorTint(int color) {
        cf = null;
    }

    public static void draw(Canvas c, int w, int h) {
        draw(c, w, h, 0, 0);
    }

    public static void draw(Canvas c, int w, int h, int dx, int dy) {
        float ow = 100f;
        float oh = 100f;

        od = (w / ow < h / oh) ? w / ow : h / oh;

        r();
        c.save();
        c.translate((w - od * ow) / 2f + dx, (h - od * oh) / 2f + dy);

        m.reset();
        m.setScale(od, od);

        c.save();
        ps.setColor(Color.argb(0, 0, 0, 0));
        ps.setStrokeCap(Paint.Cap.BUTT);
        ps.setStrokeJoin(Paint.Join.MITER);
        ps.setStrokeMiter(4.0f * od);
        c.scale(1.0f, 1.0f);
        c.save();
        p.setColor(Color.parseColor("#6eae44"));
        t.reset();
        t.moveTo(18.69f, 57.21f);
        t.lineTo(18.69f, 81.91f);
        t.lineTo(45.94f, 97.64f);
        t.lineTo(50.0f, 75.29f);
        t.lineTo(18.69f, 57.21f);

        t.transform(m);
        c.drawPath(t, p);
        c.drawPath(t, ps);
        c.restore();
        r(9, 8, 5, 1);
        p.setColor(Color.parseColor("#6eae44"));
        c.save();
        p.setColor(Color.parseColor("#dcebc5"));
        t.reset();
        t.moveTo(50.19f, 1.27f);
        t.lineTo(10.77f, 22.66f);
        t.lineTo(6.71f, 70.31f);
        t.lineTo(18.69f, 81.91f);
        t.lineTo(18.69f, 57.21f);
        t.lineTo(50.0f, 75.29f);
        t.lineTo(81.31f, 57.21f);
        t.lineTo(81.31f, 81.91f);
        t.lineTo(89.23f, 77.34f);
        t.lineTo(93.29f, 29.7f);
        t.lineTo(54.06f, 2.35f);
        t.moveTo(53.87f, 97.75f);
        t.moveTo(53.11f, 98.1f);
        t.moveTo(52.36f, 98.37f);
        t.moveTo(51.59f, 98.57f);
        c.scale(1.0f, 1.0f);
        c.rotate(0.0f);
        c.translate(-50.0f * od, -90.6f * od);

        t.transform(m);
        c.drawPath(t, p);
        c.drawPath(t, ps);
        c.restore();
        r(9, 8, 5, 1, 3);
        p.setColor(Color.parseColor("#dcebc5"));
        c.save();
        p.setColor(Color.parseColor("#589d44"));
        t.reset();
        t.moveTo(81.31f, 57.21f);
        t.lineTo(50.0f, 75.29f);
        t.lineTo(50.0f, 98.73f);
        t.lineTo(81.31f, 81.91f);
        t.lineTo(81.31f, 57.21f);

        t.transform(m);
        c.drawPath(t, p);
        c.drawPath(t, ps);
        c.restore();
        r(9, 8, 5, 1, 3, 0);
        p.setColor(Color.parseColor("#589d44"));
        c.save();
        p.setColor(Color.parseColor("#68b345"));
        t.reset();
        t.moveTo(50.0f, 14.69f);
        t.lineTo(50.0f, 52.23f);
        t.lineTo(70.83f, 40.2f);
        t.lineTo(70.83f, 26.72f);
        t.lineTo(50.0f, 14.69f);

        t.transform(m);
        c.drawPath(t, p);
        c.drawPath(t, ps);
        c.restore();
        r(9, 8, 5, 1, 3, 0, 4);
        p.setColor(Color.parseColor("#68b345"));
        c.save();
        p.setColor(Color.parseColor("#5ba645"));
        t.reset();
        t.moveTo(75.73f, 37.38f);
        t.lineTo(50.0f, 52.23f);
        t.lineTo(50.0f, 75.29f);
        t.lineTo(75.73f, 60.44f);
        t.lineTo(75.73f, 37.37f);

        t.transform(m);
        c.drawPath(t, p);
        c.drawPath(t, ps);
        c.restore();
        r(9, 8, 5, 1, 3, 0, 4, 2);
        p.setColor(Color.parseColor("#5ba645"));
        c.save();
        p.setColor(Color.parseColor("#7fb542"));
        t.reset();
        t.moveTo(24.27f, 37.38f);
        t.lineTo(24.27f, 60.44f);
        t.lineTo(50.0f, 75.29f);
        t.lineTo(50.0f, 52.23f);
        t.lineTo(24.27f, 37.37f);

        t.transform(m);
        c.drawPath(t, p);
        c.drawPath(t, ps);
        c.restore();
        r(9, 8, 5, 1, 3, 0, 4, 2, 7);
        p.setColor(Color.parseColor("#7fb542"));
        c.save();
        p.setColor(Color.parseColor("#88c14f"));
        t.reset();
        t.moveTo(50.0f, 14.69f);
        t.lineTo(29.17f, 26.72f);
        t.lineTo(29.17f, 40.2f);
        t.lineTo(50.0f, 52.23f);
        t.lineTo(50.0f, 14.69f);

        t.transform(m);
        c.drawPath(t, p);
        c.drawPath(t, ps);
        c.restore();
        r(9, 8, 5, 1, 3, 0, 4, 2, 7, 6);
        p.setColor(Color.parseColor("#88c14f"));
        c.restore();
        r();

        c.restore();
    }

    private static void r(Integer... o) {
        p.reset();
        ps.reset();
        if (cf != null) {
            p.setColorFilter(cf);
            ps.setColorFilter(cf);
        }
        p.setAntiAlias(true);
        ps.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        ps.setStyle(Paint.Style.STROKE);
        for (Integer i : o) {
            switch (i) {
                case 0:
                    p.setColor(Color.parseColor("#dcebc5"));
                    break;
                case 1:
                    ps.setStrokeMiter(4.0f * od);
                    break;
                case 2:
                    p.setColor(Color.parseColor("#68b345"));
                    break;
                case 3:
                    p.setColor(Color.parseColor("#6eae44"));
                    break;
                case 4:
                    p.setColor(Color.parseColor("#589d44"));
                    break;
                case 5:
                    ps.setStrokeJoin(Paint.Join.MITER);
                    break;
                case 6:
                    p.setColor(Color.parseColor("#7fb542"));
                    break;
                case 7:
                    p.setColor(Color.parseColor("#5ba645"));
                    break;
                case 8:
                    ps.setStrokeCap(Paint.Cap.BUTT);
                    break;
                case 9:
                    ps.setColor(Color.argb(0, 0, 0, 0));
                    break;
            }
        }
    }
};
