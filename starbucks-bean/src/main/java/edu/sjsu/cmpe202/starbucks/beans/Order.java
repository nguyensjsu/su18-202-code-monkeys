package edu.sjsu.cmpe202.starbucks.beans;

import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.HashMap;

public class Order {

	private String UserId;
    private String OrderId;
    private String ItemId;
    private String Quantity;//i think we can t  proced further coz we dont have idea how she is design structure
    private ArrayList<Items> itemList;//   item list with price   ArrayList<Item>  got it


    public Order(String UserId, String OrderId) {
        this.UserId = UserId;
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
