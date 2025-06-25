package com.paymentwall.pwunifiedsdk.ui;

/**
 * Created by nguyen.anh on 4/26/2017.
 */

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.paymentwall.pwunifiedsdk.R;
import com.paymentwall.pwunifiedsdk.util.PwUtils;

import java.util.ArrayList;
import java.util.List;

public class WaveHelper {

    private WaveView mWaveView;

    private AnimatorSet mAnimatorSet;

    List<Animator> animators;

    public WaveHelper(Context context, WaveView waveView) {
        mWaveView = waveView;
        mWaveView.setShapeType(WaveView.ShapeType.CIRCLE);
        mWaveView.setBorder((int) PwUtils.dpToPx(context, 3f), PwUtils.getColorFromAttribute(context, "progressColor"));
        mWaveView.setWaveColor(context.getResources().getColor(R.color.transparent), PwUtils.getColorFromAttribute(context, "progressColor"));
        initAnimation();
    }

    public void start() {
        mWaveView.setShowWave(true);
        if (mAnimatorSet != null) {
            mAnimatorSet.start();
        }
    }

    private void initAnimation() {
        animators = new ArrayList<>();

        // horizontal animation.
        // wave waves infinitely.
        ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(
                mWaveView, "waveShiftRatio", 0f, 1.0f);
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(500);
        waveShiftAnim.setInterpolator(new LinearInterpolator());
        animators.add(waveShiftAnim);

        // vertical animation.
        // water level increases from 0 to center of WaveView
        ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(
                mWaveView, "waterLevelRatio", 0f, 0.6f);
        waterLevelAnim.setDuration(1000);
        waterLevelAnim.setInterpolator(new LinearInterpolator());
        animators.add(waterLevelAnim);

//        // amplitude animation.
//        // wave grows big then grows small, repeatedly
//        ObjectAnimator amplitudeAnim = ObjectAnimator.ofFloat(
//                mWaveView, "amplitudeRatio", 0.0001f, 0.1f);// amplitudeAnim.setRepeatCount(ValueAnimator.INFINITE);

//        amplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
//        amplitudeAnim.setDuration(5000);
//        amplitudeAnim.setInterpolator(new LinearInterpolator());
//        animators.add(amplitudeAnim);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animators);
    }

    public void finish(final IWaveView listener){
        mWaveView.clearAnimation();
        ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(
                mWaveView, "waterLevelRatio", 0.6f, 1.1f);
        waterLevelAnim.setDuration(500);
        waterLevelAnim.setInterpolator(new AccelerateInterpolator());
        waterLevelAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onFinish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        waterLevelAnim.start();
//        animators.add(waterLevelAnim);
//        mAnimatorSet = new AnimatorSet();
//        mAnimatorSet.playTogether(animators);


    }

    public void cancel() {
        if (mAnimatorSet != null) {
//            mAnimatorSet.cancel();
            mAnimatorSet.end();
        }
    }

    public interface IWaveView{
        void onFinish();
    }
}
