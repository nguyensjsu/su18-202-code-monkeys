package edu.sjsu.cmpe202.starbucks.beans;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.awt.geom.QuadCurve2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Order implements Serializable {

	private String UserId;
    private String OrderId;
    private String ItemId;
    private String Quantity;
    private ArrayList<Items> itemList;

    public Order() {

    }

    public Order(String UserId, String OrderId) {
        this.UserId = UserId;
        this.OrderId=OrderId;
        itemList = new ArrayList<Items>();
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String OrderId) {
        this.OrderId = OrderId;
    }

    public void addItem(Items item){
        itemList.add(item);
    }


   public ArrayList<Items> getOrderItems(){
        return itemList;
   }


}
