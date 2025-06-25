package com.paymentwall.pwunifiedsdk.brick.core;

/**
 * Created by paymentwall on 28/05/15.
 */
public class BrickToken {
    private String type;
    private String token;
    private float expires_in;
    private int active;
    private BrickCard card;

    public BrickToken(String type, String token, float expires_in, int active,
                      BrickCard card) {
        super();
        this.type = type;
        this.token = token;
        this.expires_in = expires_in;
        this.active = active;
        this.card = card;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public float getExpires() {
        return expires_in;
    }

    public void setExpires(float expires_in) {
        this.expires_in = expires_in;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public BrickCard getCard() {
        return card;
    }

    public void setCard(BrickCard card) {
        this.card = card;
    }
}
