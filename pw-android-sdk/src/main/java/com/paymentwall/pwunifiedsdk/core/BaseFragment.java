package com.paymentwall.pwunifiedsdk.core;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.paymentwall.pwunifiedsdk.R;
import com.paymentwall.pwunifiedsdk.ui.CardEditText;
import com.paymentwall.pwunifiedsdk.util.SmartLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nguyen.anh on 7/15/2016.
 */

public class BaseFragment extends Fragment {

    public Activity self;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = getActivity();
    }

    public PaymentSelectionActivity getMainActivity() {
        return (PaymentSelectionActivity) getActivity();
    }

    public void showKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager)
                self.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            view.requestFocus();
            inputManager.showSoftInput(view,
                    InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) self
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = self.getCurrentFocus();
        if (view != null) {
//            view.clearFocus();
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void setViewHierarchyEnabled(View view, boolean isEnable) {
        if (view instanceof CardEditText) return;

        if (view instanceof EditText) {
            view.setFocusableInTouchMode(isEnable);
        }
        view.setEnabled(isEnable);
        view.setFocusable(isEnable);

        if (view instanceof ViewGroup group) {
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                setViewHierarchyEnabled(child, isEnable);

            }
        }
    }

    public void showWaitLayout() {
        if (getMainActivity() != null)
            getMainActivity().showWaitLayout();
    }

    public void hideWaitLayout() {
        if (getMainActivity() != null)
            getMainActivity().hideWaitLayout();
    }

    public void showErrorLayout(String error) {
        if (getMainActivity() != null) {
            if (error != null && !error.isEmpty()) {
                try {
                    JSONObject objectError = new JSONObject(error);
                    error = objectError.getString("error");
                } catch (JSONException e) {
                    SmartLog.e(e.getMessage());
                }
            } else {
                error = getString(R.string.try_again_message);
            }
            getMainActivity().showErrorLayout(error);
        }
    }

    public void hideErrorLayout() {
        if (getMainActivity() != null)
            getMainActivity().hideErrorLayout();
    }

    public void displayPaymentSucceeded() {
        if (getMainActivity() != null)
            getMainActivity().displayPaymentSucceeded();
    }

    public void sendResultBack(int resultCode) {
        if (getMainActivity() != null)
            getMainActivity().setResultBack(resultCode);
    }

    public boolean isWaitLayoutShowing() {
        if (getMainActivity() != null)
            return getMainActivity().isWaitLayoutShowing;
        return false;
    }

    public void hide3dsWebview() {
        if (getMainActivity() != null)
            getMainActivity().hide3dsWebview();
    }

    public void onPaymentSuccessful() {

    }

    public void onPaymentError() {

    }

    public void onPaymentCancel() {

    }

    public void onPaymentError(String error) {

    }


}
