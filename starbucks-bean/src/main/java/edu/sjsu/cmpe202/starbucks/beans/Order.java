package edu.sjsu.cmpe202.starbucks.beans;

import com.sun.org.apache.xpath.internal.operations.Or;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import java.awt.geom.QuadCurve2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@JsonAutoDetect
public class Order {

    //declare variables
    private String orderId;
    private String user;
    private HashMap<String,Integer> items;// for store the value parallel
    private double totalAmount;

    //constructor
    public Order(String orderId, String user) {
        this.orderId = orderId;
        this.user = user;
        items = new HashMap<String,Integer>();
    }

    //getter and setter methods

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    public void addItem(String itemid,int quant) {
        items.put(itemid,quant);
    }

    //method to get total price of the order
    public double getTotalAmount(){
        if(items!=null){
        /*for(Item item : items)
        {
            totalAmount+=item.getPrice();

        }*/
        }
        return totalAmount;
    }



}
