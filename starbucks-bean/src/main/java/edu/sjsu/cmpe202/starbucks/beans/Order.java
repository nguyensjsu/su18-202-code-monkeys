package edu.sjsu.cmpe202.starbucks.beans;

import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.HashMap;

public class Order {

	private String UserId;
    private String OrderId;
    private String ItemId;
    private String Quantity;//i think we can t  proced further coz we dont have idea how she is design structure
    private ArrayList<Item> itemList;//   item list with price   ArrayList<Item>  got it


    public Order(String UserId, String OrderId, String ItemId, String Quantity ) {
        this.UserId = UserId;
        this.OrderId = OrderId;
        this.Quantity = Quantity;
        itemList = new ArrayList<Item>();
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

    public void addItem(Item item){
        itemList.add(item);
    }


   public ArrayList<Item> getOrderItems(){
        return itemList;
   }


   class Item{
        private String itemName;
        private double price;

       public String getItemName() {
           return itemName;
       }

       public void setItemName(String itemName) {
           this.itemName = itemName;
       }

       public double getPrice() {
           return price;
       }

       public void setPrice(double price) {
           this.price = price;
       }
   }
}
