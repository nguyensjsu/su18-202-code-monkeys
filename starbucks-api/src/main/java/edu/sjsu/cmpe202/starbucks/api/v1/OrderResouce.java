package edu.sjsu.cmpe202.starbucks.api.v1;

import edu.sjsu.cmpe202.starbucks.beans.Card;
import edu.sjsu.cmpe202.starbucks.beans.Items;
import edu.sjsu.cmpe202.starbucks.beans.Order;
import edu.sjsu.cmpe202.starbucks.beans.User;
import edu.sjsu.cmpe202.starbucks.core.service.order.OrderService;
import edu.sjsu.cmpe202.starbucks.core.service.order.datastore.DatastoreOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")

public class OrderResouce {

    private User user;
    private OrderService orderservice;

    public OrderResouce(){
        this.user = new User("foo", "bar", "testprofile");
        orderservice = new DatastoreOrderService();

    }


    @RequestMapping(value = "/order/", method = RequestMethod.GET)
    public ResponseEntity<Order> getOrder() {
        Order order = orderservice.getOrder(user);
        order.addItem(new Items("11","Coffee","order",10f));
        if(order==null){
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
//w
