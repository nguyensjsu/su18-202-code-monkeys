package edu.sjsu.cmpe202.starbucks.core.service.orderitem;

import edu.sjsu.cmpe202.starbucks.beans.OrderItem;

import java.util.List;

public interface OrderItemService {
    public boolean addItem(OrderItem item) throws Exception;
    public boolean updateItem(OrderItem item);
    public boolean exists(OrderItem item);
    public boolean deleteItem(OrderItem item);
    public List<OrderItem> getItems(String order);
    public OrderItem getItem(String item, String order);
}
