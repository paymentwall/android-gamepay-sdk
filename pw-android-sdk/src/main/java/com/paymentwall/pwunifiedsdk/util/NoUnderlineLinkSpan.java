package com.paymentwall.pwunifiedsdk.util;

import android.text.TextPaint;
import android.text.style.URLSpan;

public class NoUnderlineLinkSpan extends URLSpan {
    public NoUnderlineLinkSpan(String url) {
        super(url);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false); // 🔥 Remove underline
        ds.setColor(ds.linkColor);  // Optional: customize color
    }
}