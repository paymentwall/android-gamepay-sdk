package com.paymentwall.pwunifiedsdk.mint.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

public class MintEditText extends EditText {
	
    public MintEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MintEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MintEditText(Context context) {
        super(context);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new MintInputConnection(super.onCreateInputConnection(outAttrs),
                true);
    }

    private class MintInputConnection extends InputConnectionWrapper {

        public MintInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
            	
            }
            return super.sendKeyEvent(event);
        }
        
        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
        	// magic: in latest Android, deleteSurroundingText(1, 0) will be called for backspace
        	 if (beforeLength == 1 && afterLength == 0) {
                 // backspace
                 return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                     && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
             }

             return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }

}