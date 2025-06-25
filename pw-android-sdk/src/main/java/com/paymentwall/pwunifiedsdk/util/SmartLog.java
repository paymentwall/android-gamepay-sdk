/*
 * Name: $RCSfile: SmartLog.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: May 27, 2011 3:27:39 PM $
 *
 * Copyright (C) 2011 COMPANY NAME, Inc. All rights reserved.
 */

package com.paymentwall.pwunifiedsdk.util;

import android.util.Log;

/**
 * Smart Log
 *
 * @author nguyen.anh
 */
public final class SmartLog {
    public static final boolean DEBUG = true;
    private static String TAG = "PaymentwallSdk";

    private SmartLog() {
    }
    public static void i(String msg) {i(TAG, msg);}

    public static void i(String tag, String msg) {if(DEBUG) Log.i(tag, msg);}

    public static void e(String msg) {e(TAG, msg);}

    public static void e(String tag, String msg) {if(DEBUG) Log.e(tag, msg);}

    public static void d(String msg) {d(TAG, msg);}

    public static void d(String tag, String msg) {if(DEBUG) Log.d(tag, msg);}

    public static void w(String msg) {w(TAG, msg);}

    public static void w(String tag, String msg) {if(DEBUG) Log.w(tag, msg);}
    
    public static void longString(String TAG, String str) {
        if (str.length() > 4000) {
            Log.i(TAG, str.substring(0, 4000));
            longString(TAG, str.substring(4000));
        } else
            Log.i(TAG, str);
    }
}
