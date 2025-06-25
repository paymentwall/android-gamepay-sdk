package com.paymentwall.pwunifiedsdk.mint.ui.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ScrollView;

public class PWScrollView extends ScrollView {
    private static String TAG = "PWScrollView";

    private Context context;
    private int maxHeight = 0;
    private boolean needToScrollToCenter = false;
    int top = 0;
    int bottom = 0;
    int left = 0;
    int right = 0;

    public PWScrollView(Context context) {
        super(context);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    public PWScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    public PWScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightAfter = MeasureSpec.getSize(heightMeasureSpec);
        int widthAfter = MeasureSpec.getSize(widthMeasureSpec);
        int heightBefore = getHeight();
        int widthBefore = getWidth();
        Log.d(TAG, "ah=" + heightAfter + " aw=" + widthAfter + " bh=" + heightBefore + " bw=" + widthBefore + " mh=" + maxHeight);
//        maxHeight = Math.max(heightBefore, heightAfter);
        if (widthBefore == widthAfter) {
            maxHeight = Math.max(heightBefore, heightAfter);
        } else {
            maxHeight = 0;
        }
        if (heightAfter < (maxHeight * 2 / 3) && maxHeight > 0 && heightAfter < heightBefore) {
            Log.d(TAG, "keyboard is showing");
            // Do it
            top = this.getTop();
            bottom = this.getBottom();
            left = this.getLeft();
            right = this.getRight();
            Log.d(TAG, "top=" + top + " bottom=" + bottom + " left=" + left + " right=" + right);
            needToScrollToCenter = true;
        } else {
            needToScrollToCenter = false;
        }
//        Log.d(TAG, "ah="+heightAfter+" aw="+widthAfter+" bh="+heightBefore+" bw="+widthBefore);

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        // TODO Auto-generated method stub
        super.onScrollChanged(l, t, oldl, oldt);
        if (needToScrollToCenter) {
            needToScrollToCenter = false;
            scrollTo(l, (int) (t + (convertDpToPixel(16))));
        }
    }

    public float convertDpToPixel(float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

}
