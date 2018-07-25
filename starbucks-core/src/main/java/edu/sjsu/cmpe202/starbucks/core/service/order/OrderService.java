package edu.sjsu.cmpe202.starbucks.core.service.order;

import edu.sjsu.cmpe202.starbucks.beans.Order;

public interface OrderService {
    public void createOrder();
    public void deleteOrder();
    public void addItemInOrder();
    public void removeItemFromOrder();
    public Order getOrder();
}
