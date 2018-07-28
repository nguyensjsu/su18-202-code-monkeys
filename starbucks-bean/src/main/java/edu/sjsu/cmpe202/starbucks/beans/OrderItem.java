package edu.sjsu.cmpe202.starbucks.beans;

public class OrderItem {
    private String id;
    private String orderId;
    private String itemId;
    private String comments;
    private double quantity;

    public OrderItem() {

    }

    public OrderItem(String id, String orderId, String itemId, String comments, double quantity) {
        this.id = id;
        this.orderId = orderId;
        this.itemId = itemId;
        this.comments = comments;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
