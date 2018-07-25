package edu.sjsu.cmpe202.starbucks.core.service.order;

import edu.sjsu.cmpe202.starbucks.beans.Order;
import edu.sjsu.cmpe202.starbucks.beans.User;

public interface OrderService {
    public boolean createOrder(Order order);
    public void deleteOrder();
    public void addItemInOrder();
    public void removeItemFromOrder();
    public Order getOrder(User User);
}
