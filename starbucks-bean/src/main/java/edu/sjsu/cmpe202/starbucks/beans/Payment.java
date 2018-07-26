package edu.sjsu.cmpe202.starbucks.beans;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import java.util.UUID;

@JsonAutoDetect
public class Payment {
    private String paymentId;
    private String orderId;
    private String cardId;
    private String cardCVV;
    private double charge;
    private String userProfile;

    private enum paymentStatus {
        SUCCESS,
        FAILURE_DUE_TO_INSSUFICIENT_BALANCE,
        FAILURE_DUE_TO_USER_CARD_MISMATCH,
        FAILURE_DUE_TO_MISSING_ORDER_ID,
        FAILURE_DUE_TO_INCORRECT_CVV,
        FAILURE_DUE_TO_INCORRECT_CARD_NUMBER,
        FAILURE_DUE_TO_OTHER,
        NOUPDATE
    }
    private paymentStatus status;

    public Payment(){
    }

    public Payment(String cardId, String orderId, String userProfile, String cvv, double charge) {
        this.cardId = cardId;
        this.userProfile = userProfile;
        this.cardCVV = cvv;
        this.charge = charge;
        this.paymentId = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.status = paymentStatus.NOUPDATE;
    }

    public paymentStatus getStatus() {
        return this.status;
    }

    public void setStatus(paymentStatus status) {
        this.status = status;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getCardCVV() {
        return cardCVV;
    }

    public void setCardCVV(String cardCVV) {
        this.cardCVV = cardCVV;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }
}
