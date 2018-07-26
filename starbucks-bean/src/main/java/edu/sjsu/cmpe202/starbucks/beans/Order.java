package edu.sjsu.cmpe202.starbucks.beans;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.awt.geom.QuadCurve2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Order implements Serializable {

    private String id;
	private String UserId;
    private String OrderId;
    private String User;
    private ArrayList<Items> itemList;

    public Order() {

    }

    public Order(String UserId, String OrderId) {
        this.UserId = UserId;
        this.OrderId=OrderId;
        itemList = new ArrayList<Items>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean addItem(Items item){
        if(itemList!=null) {
            itemList.add(item);
        }
         return false;
    }



    public ArrayList<Items> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<Items> itemList) {
        this.itemList = itemList;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }
}
