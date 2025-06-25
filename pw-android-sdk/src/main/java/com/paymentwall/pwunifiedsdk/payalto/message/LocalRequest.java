package com.paymentwall.pwunifiedsdk.payalto.message;

import com.paymentwall.pwunifiedsdk.payalto.utils.Const;
import com.paymentwall.pwunifiedsdk.payalto.utils.Const.P;

import java.util.Map;
import java.util.Map.Entry;

public abstract class LocalRequest extends PWSDKRequest {
    /**
     *
     */
    private static final long serialVersionUID = 3314168911113735069L;
    protected String email;
    protected Integer evaluation;
    protected String firstname;
    protected String lang = "en";
    protected String lastname;
    protected String locationAddress;
    protected String locationCity;
    protected String locationCountry;
    protected String locationState;
    protected String locationZip;
    protected String pingbackUrl;
    protected String paymentSystem;
    protected String sex;
    protected String successUrl = Const.DEFAULT_SUCCESS_URL;
    protected String widget;
    protected String birthday;
    protected String countryCode;
    protected String apiType;
    protected String mobileDownloadLink;

    protected UserProfile userProfile;

    protected Map<Integer, String> externalIds;
    protected Map<Integer, Float> prices;
    protected Map<Integer, String> currencies;

    public Map<Integer, String> getCurrencies() {
        return currencies;
    }
    public Map<Integer, Float> getPrices() {
        return prices;
    }

    public String getMobileDownloadLink() {
        return mobileDownloadLink;
    }

    public void setMobileDownloadLink(String mobileDownloadLink) {
        this.mobileDownloadLink = mobileDownloadLink;
    }

    public void setPrices(Map<Integer, Float> prices) {
        if (prices != null && prices.size() > 1) {
            for (Entry<Integer, Float> entry : prices.entrySet()) {
                addParameter(P.PRICES + P.i(entry.getKey()), Float.toString(entry.getValue()));
            }
        } else if (prices != null && prices.size() == 1) {
            for (Entry<Integer, Float> entry : prices.entrySet()) {
                addParameter(P.PRICES, Float.toString(entry.getValue()));
            }
        }
        this.prices = prices;
    }

    public void setCurrencies(Map<Integer, String> currencies) {
        if (currencies != null && currencies.size() > 1) {
            for (Entry<Integer, String> entry : currencies.entrySet()) {
                addParameter(P.CURRENCIES + P.i(entry.getKey()), entry.getValue());
            }
        } else if (currencies != null && currencies.size() == 1) {
            for (Entry<Integer, String> entry : currencies.entrySet()) {
                addParameter(P.CURRENCIES, entry.getValue());
            }
        }
        this.currencies = currencies;
    }

    public void setExternalIds(Map<Integer, String> externalIds) {
        if (externalIds != null && externalIds.size() > 1) {
            for (Entry<Integer, String> entry : externalIds.entrySet()) {
                addParameter(P.EXTERNAL_IDS + P.i(entry.getKey()), entry.getValue());
            }
        } else if (externalIds != null && externalIds.size() == 1) {
            for (Entry<Integer, String> entry : externalIds.entrySet()) {
                addParameter(P.EXTERNAL_IDS, entry.getValue());
            }
        }
        this.externalIds = externalIds;
    }


    public Map<Integer, String> getExternalIds() {
        return externalIds;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        if (userProfile != null) parameters.putAll(userProfile.toParameters());
        this.userProfile = userProfile;
    }

    public String getApiType() {
        return apiType;
    }

    public void setApiType(String apiType) {
        this.apiType = apiType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        addParameter(P.EMAIL, email);
        this.email = email;
    }

    public Integer getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Integer evaluation) {
        addParameter(P.EVALUATION, evaluation.toString());
        this.evaluation = evaluation;
    }

    public void setEvaluation(Boolean evaluation) {
        if(evaluation == null) {
            this.evaluation = null;
            removeParameter(P.EVALUATION);
        } else {
            this.evaluation = evaluation? 1 : 0;
            addParameter(P.EVALUATION, this.evaluation.toString());
        }

    }

    public String getFirstname() {

        return firstname;
    }

    public void setFirstname(String firstname) {
        addParameter(P.FIRSTNAME, firstname);
        this.firstname = firstname;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        addParameter(P.LANG, lang);
        this.lang = lang;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        addParameter(P.LASTNAME, lastname);
        this.lastname = lastname;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        addParameter(P.LOCATION_ADDRESS, locationAddress);
        this.locationAddress = locationAddress;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        addParameter(P.LOCATION_CITY, locationCity);
        this.locationCity = locationCity;
    }

    public String getLocationCountry() {
        return locationCountry;
    }

    public void setLocationCountry(String locationCountry) {
        addParameter(P.LOCATION_COUNTRY, locationCountry);
        this.locationCountry = locationCountry;
    }

    public String getLocationState() {
        return locationState;
    }

    public void setLocationState(String locationState) {
        addParameter(P.LOCATION_STATE, locationState);
        this.locationState = locationState;
    }

    public String getLocationZip() {
        return locationZip;
    }

    public void setLocationZip(String locationZip) {
        addParameter(P.LOCATION_ZIP, locationZip);
        this.locationZip = locationZip;
    }

    public String getPingbackUrl() {
        return pingbackUrl;
    }

    public void setPingbackUrl(String pingbackUrl) {
        addParameter(P.PINGBACK_URL, pingbackUrl);
        this.pingbackUrl = pingbackUrl;
    }

    public String getPaymentSystem() {
        return paymentSystem;
    }

    public void setPaymentSystem(String paymentSystem) {
        addParameter(P.PS, paymentSystem);
        this.paymentSystem = paymentSystem;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        addParameter(P.SEX, sex);
        this.sex = sex;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        addParameter(P.SUCCESS_URL, successUrl);
        this.successUrl = successUrl;
    }

    public String getWidget() {
        return widget;
    }

    public void setWidget(String widget) {
        addParameter(P.WIDGET, widget);
        this.widget = widget;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        addParameter(P.BIRTHDAY, birthday);
        this.birthday = birthday;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        addParameter(P.COUNTRY_CODE, countryCode);
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LocalRequest{");
        sb.append("apiType='").append(apiType).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", evaluation=").append(evaluation);
        sb.append(", firstname='").append(firstname).append('\'');
        sb.append(", lang='").append(lang).append('\'');
        sb.append(", lastname='").append(lastname).append('\'');
        sb.append(", locationAddress='").append(locationAddress).append('\'');
        sb.append(", locationCity='").append(locationCity).append('\'');
        sb.append(", locationCountry='").append(locationCountry).append('\'');
        sb.append(", locationState='").append(locationState).append('\'');
        sb.append(", locationZip='").append(locationZip).append('\'');
        sb.append(", pingbackUrl='").append(pingbackUrl).append('\'');
        sb.append(", paymentSystem='").append(paymentSystem).append('\'');
        sb.append(", sex='").append(sex).append('\'');
        sb.append(", successUrl='").append(successUrl).append('\'');
        sb.append(", widget='").append(widget).append('\'');
        sb.append(", birthday='").append(birthday).append('\'');
        sb.append(", countryCode='").append(countryCode).append('\'');
        sb.append(", externalIds=").append(externalIds);
        sb.append(", prices=").append(prices);
        sb.append(", currencies=").append(currencies);
        sb.append('}');
        return sb.toString();
    }
}
