package edu.sjsu.cmpe202.starbucks.beans;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import java.util.UUID;

@JsonAutoDetect
public class Payment {
    String paymentId;
    String orderId;
    String cardId;
    double pay;

    public Payment() {

    }

    public Payment(String paymentId,String orderId, String cardId, double pay) {
        this.orderId = orderId;
        this.cardId = cardId;
        this.pay = pay;
        this.paymentId = paymentId;
    }

    public Payment(String orderId, String cardId, double pay) {
        this.orderId = orderId;
        this.cardId = cardId;
        this.pay = pay;
        this.paymentId = UUID.randomUUID().toString();
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

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }
}
