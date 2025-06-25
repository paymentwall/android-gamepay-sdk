package com.paymentwall.pwunifiedsdk.brick.core;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by paymentwall on 28/05/15.
 */
public class BrickCard {
    public static class Params {
        public static final String KEY = "public_key";
        public static final String NUMBER = "card[number]";
        public static final String EXP_MONTH = "card[exp_month]";
        public static final String EXP_YEAR = "card[exp_year]";
        public static final String CVV = "card[cvv]";
    }

    private Map<String, String> parameters;

    private String number;
    private String expMonth;
    private String expYear;
    private String cvv;
    private String type;
    private String last4;
    private String bin;
    private String email;

    public Map<String, String> getParameters() throws BrickError {
        if (parameters == null) throw new BrickError(BrickError.Kind.INVALID);
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(String key, String value) {
        if (parameters == null) parameters = new HashMap<String, String>();
        parameters.put(key, value);
    }

    public BrickCard(String number, String expMonth, String expYear, String cvv, String email) {
        this(number, expMonth, expYear, cvv, email, null, null, null);
    }

//    public BrickCard(String type, String last4, String bin, String expMonth,
//                     String expYear) {
//        this(null, expMonth, expYear, null, type, last4, bin);
//    }

    public BrickCard(String number, String expMonth, String expYear,
                     String cvv, String email, String type, String last4, String bin) {
        super();
        this.number = number;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.cvv = cvv;
        this.type = type;
        this.last4 = last4;
        this.bin = bin;
        this.email = email;
        addParameter(Params.NUMBER, number);
        addParameter(Params.EXP_MONTH, expMonth);
        addParameter(Params.EXP_YEAR, expYear);
        addParameter(Params.CVV, cvv);
        addParameter(Params.EXP_MONTH, expMonth);
    }

    public String getType() {
        if (BrickHelper.isBlank(type) && !BrickHelper.isBlank(number)) {
            if (CardType.detect(number) == CardType.AMERICAN_EXPRESS) {
                return CardType.TYPE_AMERICAN_EXPRESS;
            } else if (CardType.detect(number) == CardType.MASTERCARD) {
                return CardType.TYPE_MASTERCARD;
            } else if (CardType.detect(number) == CardType.DINERS_CLUB) {
                return CardType.TYPE_DINERS_CLUB;
            } else if (CardType.detect(number) == CardType.DISCOVER) {
                return CardType.TYPE_DISCOVER;
            } else if (CardType.detect(number) == CardType.JCB) {
                return CardType.TYPE_JCB;
            } else if (CardType.detect(number) == CardType.VISA) {
                return CardType.TYPE_VISA;
            } else {
                return CardType.TYPE_UNKNOWN;
            }
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(String expMonth) {
        this.expMonth = expMonth;
        addParameter(Params.EXP_MONTH, expMonth);
    }

    public String getExpYear() {
        return expYear;
    }

    public void setExpYear(String expYear) {
        this.expYear = expYear;
        addParameter(Params.EXP_YEAR, expYear);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
        addParameter(Params.NUMBER, number);
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvc(String cvv) {
        this.cvv = cvv;
        addParameter(Params.CVV, number);
    }

    public boolean isValid() {
        return
                isNumberValid() &&
                        isExpValid() && isCvcValid() && isEmailValid();
    }

    public boolean isNumberValid() {
        if (BrickHelper.isBlank(number)) {
            return false;
        }

        String strNumber = number.trim().replaceAll("\\s+|-", "");
        if (BrickHelper.isBlank(strNumber) || !BrickHelper.luhnCheck(strNumber)) {
            return false;
        }
        return true;

    }

    public boolean isExpValid() {
        try {
            int month = Integer.parseInt(expMonth);
            int year = Integer.parseInt(expYear);
            int length = expYear.length();
            return !BrickHelper.isDateExpired(month, year, length);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCvcValid() {
        if (BrickHelper.isBlank(cvv)) {
            return false;
        }
        String cvvValue = cvv.trim();

        boolean validLength = ((type == null && cvvValue.length() >= 3 && cvvValue
                .length() <= 4)
                || (CardType.TYPE_AMERICAN_EXPRESS.equals(type) && cvvValue
                .length() == 4) || (!CardType.TYPE_AMERICAN_EXPRESS
                .equals(type) && cvvValue.length() == 3));

        if (!validLength) {
            return false;
        }
        return true;
    }

    public boolean isEmailValid() {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public enum CardType {

        UNKNOWN, VISA("^4[0-9]{12}(?:[0-9]{3})?$"), MASTERCARD(
                "^5[1-5][0-9]{14}$"), AMERICAN_EXPRESS("^3[47][0-9]{13}$"), DINERS_CLUB(
                "^3(?:0[0-5]|[68][0-9])[0-9]{11}$"), DISCOVER(
                "^6(?:011|5[0-9]{2})[0-9]{12}$"), JCB(
                "^(?:2131|1800|35\\d{3})\\d{11}$");

        public static final String TYPE_AMERICAN_EXPRESS = "American Express";
        public static final String TYPE_DISCOVER = "Discover";
        public static final String TYPE_JCB = "JCB";
        public static final String TYPE_DINERS_CLUB = "Diners Club";
        public static final String TYPE_VISA = "Visa";
        public static final String TYPE_MASTERCARD = "MasterCard";
        public static final String TYPE_UNKNOWN = "Unknown";

        private Pattern pattern;

        CardType() {
            this.pattern = null;
        }

        CardType(String pattern) {
            this.pattern = Pattern.compile(pattern);
        }

        public static CardType detect(String cardNumber) {

            for (CardType cardType : CardType.values()) {
                if (null == cardType.pattern)
                    continue;
                if (cardType.pattern.matcher(cardNumber).matches())
                    return cardType;
            }

            return UNKNOWN;
        }

    }
}
