package com.paymentwall.pwunifiedsdk.payalto.message;

import com.paymentwall.pwunifiedsdk.payalto.utils.Const;

/**
 * Base class to create Payment Status query.
 * Please use Builder to create query.
 **/
public class PaymentStatusRequest extends PWSDKRequest{
    private String agExternalId;
    private String ref;

    private PaymentStatusRequest() {

    }

    public static class Builder {
        PaymentStatusRequest request;
        public PaymentStatusRequest build() {
            return request;
        }

        public Builder setQuery(String projectKey, String ref) {
            request = new PaymentStatusRequest(projectKey, ref);
            return this;
        }

        public Builder setQueryWithSign(String projectKey, String ref, String sign, int signVersion) {
            request = new PaymentStatusRequest(projectKey, ref);
            request.setSign(sign);
            request.setSignVersion(signVersion);
            return this;
        }

        public Builder setQuery(String projectKey, String ref, String secretKey, int signVersion) {
            request = new PaymentStatusRequest(projectKey, ref);
            request.setSecretKey(secretKey);
            request.setSignVersion(signVersion);
            return this;
        }

        /**
         * Use this method for unsigned Payment Status call.
         * Please ask bizdev for enable unsigned call
         * @param projectKey  project key
         * @param uid  user id
         * @param agExternalId  external id of the product
         **/
        public Builder setQuery(String projectKey, String uid, String agExternalId) {
            request = new PaymentStatusRequest(projectKey, uid, agExternalId);
            return this;
        }
        /**
         * Use this method for signed Payment Status call
         * @param projectKey  project key
         * @param uid  user id
         * @param agExternalId  external id of the product
         * @param secretKey  project secret key
         * @param signVersion  signature version (3 is recommended)
         **/
        public Builder setQuery(String projectKey, String uid, String agExternalId, String secretKey, int signVersion) {
            request = new PaymentStatusRequest(projectKey, uid, agExternalId);
            request.setSecretKey(secretKey);
            request.setSignVersion(signVersion);
            return this;
        }

        public Builder setQueryWithSign(String projectKey, String uid, String agExternalId, String sign, int signVersion) {
            request = new PaymentStatusRequest(projectKey, uid, agExternalId);
            request.setSign(sign);
            request.setSignVersion(signVersion);
            return this;
        }
    }

    private PaymentStatusRequest(String key, String ref) {
        setKey(key);
        setRef(ref);
    }

    private PaymentStatusRequest(String key, String uid, String agExternalId) {
        setAgExternalId(agExternalId);
        setKey(key);
        setUid(uid);
    }

    public String getAgExternalId() {
        return agExternalId;
    }

    private void setAgExternalId(String agExternalId) {
        addParameter(Const.P.AG_EXTERNAL_ID, agExternalId);
        this.agExternalId = agExternalId;
    }

    public String getRef() {
        return ref;
    }

    private void setRef(String ref) {
        addParameter(Const.P.REF, ref);
        this.ref = ref;
    }
}
