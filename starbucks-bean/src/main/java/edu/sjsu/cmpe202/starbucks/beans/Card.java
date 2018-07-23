package edu.sjsu.cmpe202.starbucks.beans;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
public class Card {
    private String id;
    private String user;
    private String cvv;
    private double balance;

    public Card(){

    }

    public Card(String id, String user, String cvv) {
        this(id, user, cvv, 20f);
    }

    public Card(String id, String user, String cvv, double balance) {
        this.id = id;
        this.user = user;
        this.cvv = cvv;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
