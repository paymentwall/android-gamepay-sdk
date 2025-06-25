package com.paymentwall.pwunifiedsdk.brick.core;

/**
 * Created by paymentwall on 28/05/15.
 */
public class BrickResponse {
    private String error;
    private String type;
    private String code;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
