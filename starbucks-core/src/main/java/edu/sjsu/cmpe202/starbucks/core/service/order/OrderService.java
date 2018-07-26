package edu.sjsu.cmpe202.starbucks.core.service.order;

import edu.sjsu.cmpe202.starbucks.beans.Order;
import edu.sjsu.cmpe202.starbucks.beans.User;

import java.util.List;

//interface : parameter based on method
public interface OrderService {
    public boolean createOrder(Order order)throws Exception;
    public boolean deleteOrder(Order Order);
    public void addItemInOrder();
    public void removeItemFromOrder();
    public Order getOrder(String order, String user);
    public List<Order> getOrders(String user);
}
