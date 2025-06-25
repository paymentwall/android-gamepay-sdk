package com.paymentwall.pwunifiedsdk.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.paymentwall.pwunifiedsdk.R;

public class LoadingButton extends FrameLayout {

    private Button button;
    private ProgressBar progressBar;
    private String text;

    public LoadingButton(Context context) {
        this(context, null);
    }

    public LoadingButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.loading_button_view, this, true);
        button = findViewById(R.id.btn);
        progressBar = findViewById(R.id.prBar);

        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LoadingButton, 0, 0);
            try {
                text = a.getString(R.styleable.LoadingButton_buttonText);
                setButtonText(text != null ? text : "");
            } finally {
                a.recycle();
            }
        }
    }

    public void setButtonOnClickListener(OnClickListener onClickListener) {
        button.setOnClickListener(onClickListener);
    }

    public void onStartLoading() {
        button.setText("");
        button.setClickable(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void onStopLoading() {
        button.setText(text);
        button.setClickable(true);
        progressBar.setVisibility(View.GONE);
    }

    public void setButtonText(String text) {
        this.text = text;
        button.setText(text);
    }

    public void setButtonEnabled(boolean enable) {
        this.setEnabled(enable);
        button.setEnabled(enable);
    }

    public boolean isInProgress() {
        return progressBar.getVisibility() == View.VISIBLE;
    }
}
