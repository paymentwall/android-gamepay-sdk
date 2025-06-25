package com.paymentwall.pwunifiedsdk.payalto.message;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.paymentwall.pwunifiedsdk.payalto.utils.PayAltoMiscUtils;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by paymentwall on 12/01/16.
 */
public class UserProfile implements Parameterable, Parcelable {
    protected String email;
    protected Long registrationDate;
    protected Long birthday;
    protected String sex;
    protected String username;
    protected String firstname;
    protected String lastname;
    protected String city;
    protected String state;
    protected String address;
    protected String country;
    protected String zip;
    protected String membership;
    protected Long membershipDate;
    protected String registrationCountry;
    protected String registrationIp;
    protected String registrationEmail;
    protected Boolean registrationEmailVerified;
    protected String registrationName;
    protected String registrationLastname;
    protected String registrationSource;
    protected Integer loginsNumber;
    protected Integer paymentsNumber;
    protected Double paymentsAmount;
    protected Integer followers;
    protected Integer messageSent;
    protected Integer messageSentLast24h;
    protected Integer messageReceived;
    protected Integer interactions;
    protected Integer interactionsLast24h;
    protected Float riskScore;
    protected Integer complaints;
    protected Boolean wasBanned;
    protected Integer deliveredProducts;
    protected Integer canceledPayments;
    protected Float rating;
    protected Integer registrationAge;
    protected Boolean enable3dSecure;

    public UserProfile(String email, Long registrationDate) {
        this.email = email;
        this.registrationDate = registrationDate;
    }

    @Override
    public Map<String, String> toParameters() {
        TreeMap<String, String> parameters = new TreeMap<String, String>();
        if (!TextUtils.isEmpty(email))
            parameters.put(Parameters.EMAIL, PayAltoMiscUtils.val(email));
        if (registrationDate != null)
            parameters.put(Parameters.REGISTRATION_DATE, PayAltoMiscUtils.val(registrationDate));
        if (birthday != null) parameters.put(Parameters.BIRTHDAY, PayAltoMiscUtils.val(birthday));
        if (!TextUtils.isEmpty(sex)) parameters.put(Parameters.SEX, PayAltoMiscUtils.val(sex));
        if (!TextUtils.isEmpty(username))
            parameters.put(Parameters.USERNAME, PayAltoMiscUtils.val(username));
        if (!TextUtils.isEmpty(firstname))
            parameters.put(Parameters.FIRSTNAME, PayAltoMiscUtils.val(firstname));
        if (!TextUtils.isEmpty(lastname))
            parameters.put(Parameters.LASTNAME, PayAltoMiscUtils.val(lastname));
        if (!TextUtils.isEmpty(city)) parameters.put(Parameters.CITY, PayAltoMiscUtils.val(city));
        if (!TextUtils.isEmpty(state))
            parameters.put(Parameters.STATE, PayAltoMiscUtils.val(state));
        if (!TextUtils.isEmpty(address))
            parameters.put(Parameters.ADDRESS, PayAltoMiscUtils.val(address));
        if (!TextUtils.isEmpty(country))
            parameters.put(Parameters.COUNTRY, PayAltoMiscUtils.val(country));
        if (!TextUtils.isEmpty(zip)) parameters.put(Parameters.ZIP, PayAltoMiscUtils.val(zip));
        if (!TextUtils.isEmpty(membership))
            parameters.put(Parameters.MEMBERSHIP, PayAltoMiscUtils.val(membership));
        if (membershipDate != null)
            parameters.put(Parameters.MEMBERSHIP_DATE, PayAltoMiscUtils.val(membershipDate));
        if (!TextUtils.isEmpty(registrationCountry))
            parameters.put(Parameters.REGISTRATION_COUNTRY, PayAltoMiscUtils.val(registrationCountry));
        if (!TextUtils.isEmpty(registrationIp))
            parameters.put(Parameters.REGISTRATION_IP, PayAltoMiscUtils.val(registrationIp));
        if (!TextUtils.isEmpty(registrationEmail))
            parameters.put(Parameters.REGISTRATION_EMAIL, PayAltoMiscUtils.val(registrationEmail));
        if (registrationEmailVerified != null)
            parameters.put(Parameters.REGISTRATION_EMAIL_VERIFIED, PayAltoMiscUtils.val(registrationEmailVerified));
        if (!TextUtils.isEmpty(registrationName))
            parameters.put(Parameters.REGISTRATION_NAME, PayAltoMiscUtils.val(registrationName));
        if (!TextUtils.isEmpty(registrationLastname))
            parameters.put(Parameters.REGISTRATION_LASTNAME, PayAltoMiscUtils.val(registrationLastname));
        if (!TextUtils.isEmpty(registrationSource))
            parameters.put(Parameters.REGISTRATION_SOURCE, PayAltoMiscUtils.val(registrationSource));
        if (loginsNumber != null)
            parameters.put(Parameters.LOGINS_NUMBER, PayAltoMiscUtils.val(loginsNumber));
        if (paymentsNumber != null)
            parameters.put(Parameters.PAYMENTS_NUMBER, PayAltoMiscUtils.val(paymentsNumber));
        if (paymentsAmount != null)
            parameters.put(Parameters.PAYMENTS_AMOUNT, PayAltoMiscUtils.val(paymentsAmount));
        if (followers != null)
            parameters.put(Parameters.FOLLOWERS, PayAltoMiscUtils.val(followers));
        if (messageSent != null)
            parameters.put(Parameters.MESSAGE_SENT, PayAltoMiscUtils.val(messageSent));
        if (messageSentLast24h != null)
            parameters.put(Parameters.MESSAGE_SENT_LAST_24H, PayAltoMiscUtils.val(messageSentLast24h));
        if (messageReceived != null)
            parameters.put(Parameters.MESSAGE_RECEIVED, PayAltoMiscUtils.val(messageReceived));
        if (interactions != null)
            parameters.put(Parameters.INTERACTIONS, PayAltoMiscUtils.val(interactions));
        if (interactionsLast24h != null)
            parameters.put(Parameters.INTERACTIONS_LAST_24H, PayAltoMiscUtils.val(interactionsLast24h));
        if (riskScore != null)
            parameters.put(Parameters.RISK_SCORE, PayAltoMiscUtils.val(riskScore));
        if (complaints != null)
            parameters.put(Parameters.COMPLAINTS, PayAltoMiscUtils.val(complaints));
        if (wasBanned != null)
            parameters.put(Parameters.WAS_BANNED, PayAltoMiscUtils.val(wasBanned));
        if (deliveredProducts != null)
            parameters.put(Parameters.DELIVERED_PRODUCTS, PayAltoMiscUtils.val(deliveredProducts));
        if (canceledPayments != null)
            parameters.put(Parameters.CANCELED_PAYMENTS, PayAltoMiscUtils.val(canceledPayments));
        if (rating != null) parameters.put(Parameters.RATING, PayAltoMiscUtils.val(rating));
        if (registrationAge != null)
            parameters.put(Parameters.REGISTRATION_AGE, PayAltoMiscUtils.val(registrationAge));
        if (enable3dSecure != null)
            parameters.put(Parameters.ENABLE_3D_SECURE, PayAltoMiscUtils.val(enable3dSecure));
        return parameters;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public Integer getCanceledPayments() {
        return canceledPayments;
    }

    public void setCanceledPayments(Integer canceledPayments) {
        this.canceledPayments = canceledPayments;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getComplaints() {
        return complaints;
    }

    public void setComplaints(Integer complaints) {
        this.complaints = complaints;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getDeliveredProducts() {
        return deliveredProducts;
    }

    public void setDeliveredProducts(Integer deliveredProducts) {
        this.deliveredProducts = deliveredProducts;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEnable3dSecure() {
        return enable3dSecure;
    }

    public void setEnable3dSecure(Boolean enable3dSecure) {
        this.enable3dSecure = enable3dSecure;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getInteractions() {
        return interactions;
    }

    public void setInteractions(Integer interactions) {
        this.interactions = interactions;
    }

    public Integer getInteractionsLast24h() {
        return interactionsLast24h;
    }

    public void setInteractionsLast24h(Integer interactionsLast24h) {
        this.interactionsLast24h = interactionsLast24h;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getLoginsNumber() {
        return loginsNumber;
    }

    public void setLoginsNumber(Integer loginsNumber) {
        this.loginsNumber = loginsNumber;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public Long getMembershipDate() {
        return membershipDate;
    }

    public void setMembershipDate(Long membershipDate) {
        this.membershipDate = membershipDate;
    }

    public Integer getMessageReceived() {
        return messageReceived;
    }

    public void setMessageReceived(Integer messageReceived) {
        this.messageReceived = messageReceived;
    }

    public Integer getMessageSent() {
        return messageSent;
    }

    public void setMessageSent(Integer messageSent) {
        this.messageSent = messageSent;
    }

    public Integer getMessageSentLast24h() {
        return messageSentLast24h;
    }

    public void setMessageSentLast24h(Integer messageSentLast24h) {
        this.messageSentLast24h = messageSentLast24h;
    }

    public Double getPaymentsAmount() {
        return paymentsAmount;
    }

    public void setPaymentsAmount(Double paymentsAmount) {
        this.paymentsAmount = paymentsAmount;
    }

    public Integer getPaymentsNumber() {
        return paymentsNumber;
    }

    public void setPaymentsNumber(Integer paymentsNumber) {
        this.paymentsNumber = paymentsNumber;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Integer getRegistrationAge() {
        return registrationAge;
    }

    public void setRegistrationAge(Integer registrationAge) {
        this.registrationAge = registrationAge;
    }

    public String getRegistrationCountry() {
        return registrationCountry;
    }

    public void setRegistrationCountry(String registrationCountry) {
        this.registrationCountry = registrationCountry;
    }

    public Long getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Long registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegistrationEmail() {
        return registrationEmail;
    }

    public void setRegistrationEmail(String registrationEmail) {
        this.registrationEmail = registrationEmail;
    }

    public Boolean getRegistrationEmailVerified() {
        return registrationEmailVerified;
    }

    public void setRegistrationEmailVerified(Boolean registrationEmailVerified) {
        this.registrationEmailVerified = registrationEmailVerified;
    }

    public String getRegistrationIp() {
        return registrationIp;
    }

    public void setRegistrationIp(String registrationIp) {
        this.registrationIp = registrationIp;
    }

    public String getRegistrationLastname() {
        return registrationLastname;
    }

    public void setRegistrationLastname(String registrationLastname) {
        this.registrationLastname = registrationLastname;
    }

    public String getRegistrationName() {
        return registrationName;
    }

    public void setRegistrationName(String registrationName) {
        this.registrationName = registrationName;
    }

    public String getRegistrationSource() {
        return registrationSource;
    }

    public void setRegistrationSource(String registrationSource) {
        this.registrationSource = registrationSource;
    }

    public Float getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(Float riskScore) {
        this.riskScore = riskScore;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getWasBanned() {
        return wasBanned;
    }

    public void setWasBanned(Boolean wasBanned) {
        this.wasBanned = wasBanned;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeValue(this.registrationDate);
        dest.writeValue(this.birthday);
        dest.writeString(this.sex);
        dest.writeString(this.username);
        dest.writeString(this.firstname);
        dest.writeString(this.lastname);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.address);
        dest.writeString(this.country);
        dest.writeString(this.zip);
        dest.writeString(this.membership);
        dest.writeValue(this.membershipDate);
        dest.writeString(this.registrationCountry);
        dest.writeString(this.registrationIp);
        dest.writeString(this.registrationEmail);
        dest.writeValue(this.registrationEmailVerified);
        dest.writeString(this.registrationName);
        dest.writeString(this.registrationLastname);
        dest.writeString(this.registrationSource);
        dest.writeValue(this.loginsNumber);
        dest.writeValue(this.paymentsNumber);
        dest.writeValue(this.paymentsAmount);
        dest.writeValue(this.followers);
        dest.writeValue(this.messageSent);
        dest.writeValue(this.messageSentLast24h);
        dest.writeValue(this.messageReceived);
        dest.writeValue(this.interactions);
        dest.writeValue(this.interactionsLast24h);
        dest.writeValue(this.riskScore);
        dest.writeValue(this.complaints);
        dest.writeValue(this.wasBanned);
        dest.writeValue(this.deliveredProducts);
        dest.writeValue(this.canceledPayments);
        dest.writeValue(this.rating);
        dest.writeValue(this.registrationAge);
        dest.writeValue(this.enable3dSecure);
    }

    public UserProfile() {
    }

    protected UserProfile(Parcel in) {
        this.email = in.readString();
        this.registrationDate = (Long) in.readValue(Long.class.getClassLoader());
        this.birthday = (Long) in.readValue(Long.class.getClassLoader());
        this.sex = in.readString();
        this.username = in.readString();
        this.firstname = in.readString();
        this.lastname = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.address = in.readString();
        this.country = in.readString();
        this.zip = in.readString();
        this.membership = in.readString();
        this.membershipDate = (Long) in.readValue(Long.class.getClassLoader());
        this.registrationCountry = in.readString();
        this.registrationIp = in.readString();
        this.registrationEmail = in.readString();
        this.registrationEmailVerified = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.registrationName = in.readString();
        this.registrationLastname = in.readString();
        this.registrationSource = in.readString();
        this.loginsNumber = (Integer) in.readValue(Integer.class.getClassLoader());
        this.paymentsNumber = (Integer) in.readValue(Integer.class.getClassLoader());
        this.paymentsAmount = (Double) in.readValue(Double.class.getClassLoader());
        this.followers = (Integer) in.readValue(Integer.class.getClassLoader());
        this.messageSent = (Integer) in.readValue(Integer.class.getClassLoader());
        this.messageSentLast24h = (Integer) in.readValue(Integer.class.getClassLoader());
        this.messageReceived = (Integer) in.readValue(Integer.class.getClassLoader());
        this.interactions = (Integer) in.readValue(Integer.class.getClassLoader());
        this.interactionsLast24h = (Integer) in.readValue(Integer.class.getClassLoader());
        this.riskScore = (Float) in.readValue(Float.class.getClassLoader());
        this.complaints = (Integer) in.readValue(Integer.class.getClassLoader());
        this.wasBanned = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.deliveredProducts = (Integer) in.readValue(Integer.class.getClassLoader());
        this.canceledPayments = (Integer) in.readValue(Integer.class.getClassLoader());
        this.rating = (Float) in.readValue(Float.class.getClassLoader());
        this.registrationAge = (Integer) in.readValue(Integer.class.getClassLoader());
        this.enable3dSecure = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<UserProfile> CREATOR = new Parcelable.Creator<UserProfile>() {
        public UserProfile createFromParcel(Parcel source) {
            return new UserProfile(source);
        }

        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    public static final class Parameters {
        public static final String EMAIL = "email";
        public static final String REGISTRATION_DATE = "history[registration_date]";
        public static final String BIRTHDAY = "customer[birthday]";
        public static final String SEX = "customer[sex]";
        public static final String USERNAME = "customer[username]";
        public static final String FIRSTNAME = "customer[firstname]";
        public static final String LASTNAME = "customer[lastname]";
        public static final String CITY = "customer[city]";
        public static final String STATE = "customer[state]";
        public static final String ADDRESS = "customer[address]";
        public static final String COUNTRY = "customer[country]";
        public static final String ZIP = "customer[zip]";
        public static final String MEMBERSHIP = "history[membership]";
        public static final String MEMBERSHIP_DATE = "history[membership_date]";
        public static final String REGISTRATION_COUNTRY = "history[registration_country]";
        public static final String REGISTRATION_IP = "history[registration_ip]";
        public static final String REGISTRATION_EMAIL = "history[registration_email]";
        public static final String REGISTRATION_EMAIL_VERIFIED = "history[registration_email_verified]";
        public static final String REGISTRATION_NAME = "history[registration_name]";
        public static final String REGISTRATION_LASTNAME = "history[registration_lastname]";
        public static final String REGISTRATION_SOURCE = "history[registration_source]";
        public static final String LOGINS_NUMBER = "history[logins_number]";
        public static final String PAYMENTS_NUMBER = "history[payments_number]";
        public static final String PAYMENTS_AMOUNT = "history[payments_amount]";
        public static final String FOLLOWERS = "history[followers]";
        public static final String MESSAGE_SENT = "history[messages_sent]";
        public static final String MESSAGE_SENT_LAST_24H = "history[messages_sent_last_24hours]";
        public static final String MESSAGE_RECEIVED = "history[messages_received]";
        public static final String INTERACTIONS = "history[interactions]";
        public static final String INTERACTIONS_LAST_24H = "history[interactions_last_24hours]";
        public static final String RISK_SCORE = "history[risk_score]";
        public static final String COMPLAINTS = "history[complaints]";
        public static final String WAS_BANNED = "history[was_banned]";
        public static final String DELIVERED_PRODUCTS = "history[delivered_products]";
        public static final String CANCELED_PAYMENTS = "history[cancelled_payments]";
        public static final String RATING = "history[customer_rating]";
        public static final String REGISTRATION_AGE = "history[registration_age]";
        public static final String ENABLE_3D_SECURE = "3dsecure";
    }
}
