package edu.sjsu.cmpe202.starbucks.core.service.order.datastore;

import edu.sjsu.cmpe202.starbucks.beans.Order;
import edu.sjsu.cmpe202.starbucks.beans.User;
import edu.sjsu.cmpe202.starbucks.core.service.order.OrderService;

public class DatastoreOrderService implements OrderService {

    private Order order;

    public boolean createOrder(Order order){
        if(order!=null){
        this.order=order;
        return true;
        }
        return false;
    }
    public void deleteOrder(){
        this.order=null;
    }
    public void addItemInOrder(){

    }

    public void removeItemFromOrder(){

    }

    public Order getOrder(User user){
        Order order = new Order("111","121");
        return order;
    };
}
