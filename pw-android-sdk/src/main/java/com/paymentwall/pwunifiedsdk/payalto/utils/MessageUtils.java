package com.paymentwall.pwunifiedsdk.payalto.utils;

import com.paymentwall.pwunifiedsdk.payalto.message.CustomRequest;
import com.paymentwall.pwunifiedsdk.payalto.message.LocalRequest;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by hoahieu on 03/04/17.
 */

public class MessageUtils {
    public static final String APP_NAME_UNKNOWN = "n/a";

    public static Map<String, String> getDefaultParameters(LocalRequest localRequest) {
        Map<String, String> parameters = localRequest.getParameter();
        if(localRequest.getSuccessUrl() != null && localRequest.getSuccessUrl().length() > 0) {
            parameters.put(Const.P.SUCCESS_URL, localRequest.getSuccessUrl());
        } else {
            parameters.put(Const.P.SUCCESS_URL, Const.DEFAULT_SUCCESS_URL);
        }

        return parameters;
    }

    public static Map<String, String> getDefaultParameters(CustomRequest customRequest) {
        Map<String, String> parameters = customRequest.getParameters();

        if(!parameters.containsKey(Const.P.SUCCESS_URL)) parameters.put(Const.P.SUCCESS_URL, Const.DEFAULT_SUCCESS_URL);
        return parameters;
    }

    public static Map<String, String> getDefaultParameters() {
        TreeMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put(Const.P.SUCCESS_URL, Const.DEFAULT_SUCCESS_URL);
        return parameters;
    }

}
