package edu.sjsu.cmpe202.starbucks.api.v1;

import edu.sjsu.cmpe202.starbucks.beans.*;
import edu.sjsu.cmpe202.starbucks.core.service.item.ItemService;
import edu.sjsu.cmpe202.starbucks.core.service.item.datastore.DatastoreItemService;
import edu.sjsu.cmpe202.starbucks.core.service.order.OrderService;
import edu.sjsu.cmpe202.starbucks.core.service.order.datastore.DatastoreOrderService;
import edu.sjsu.cmpe202.starbucks.core.service.orderitem.OrderItemService;
import edu.sjsu.cmpe202.starbucks.core.service.orderitem.datastore.DatastoreOrderItemService;
import edu.sjsu.cmpe202.starbucks.core.service.user.UserService;
import edu.sjsu.cmpe202.starbucks.core.service.user.datastore.DatastoreUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")

public class OrderResource {

    private User user;
    private OrderService orderservice;
    private OrderItemService orderItemService;
    private UserService userService;
    private ItemService itemService;

    public OrderResource(){
        this.user = new User("foo", "bar", "testprofile");
        orderservice = new DatastoreOrderService();
        userService =new DatastoreUserService();
        orderItemService = new DatastoreOrderItemService();
        itemService = new DatastoreItemService();
    }

    //list the order with particular id
    @RequestMapping(value = "/order/{order}", method = RequestMethod.GET)
    public ResponseEntity<Order> getOrder(@PathVariable("order") String id) {
        Order order = orderservice.getOrder(id, user.getProfile());

        if(order==null){
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    //list all orders
    @RequestMapping(value = "/orders", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getOrders() {
        List<Order> o = orderservice.getOrders(user.getProfile());

        if (o == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(o, HttpStatus.OK);
    }

    //add new order
    @RequestMapping(value = "/order", method = RequestMethod.POST,consumes = "application/json")
    public ResponseEntity addOrder(@RequestBody Order order) {
        this.userService.insertUser(this.user);
        order.setUser(this.user.getProfile());

        try {
            boolean success = orderservice.createOrder(order);
            if (success) {
                return new ResponseEntity(HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>("Order already exists from controller", HttpStatus.BAD_REQUEST);
        }

    }

    //list the order with particular id
    @RequestMapping(value = "/order/{order}", method = RequestMethod.PUT)
    public ResponseEntity updateOrder(@RequestBody Order order, @PathVariable("order") String id) {
        this.userService.insertUser(this.user);
        order.setUser(this.user.getProfile());
        order.setId(id);

        boolean success = orderservice.updateOrder(order);
        if(!success){
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    //DELETE AN ORDER
    @RequestMapping(value = "/order/{order}", method = RequestMethod.DELETE)
    public ResponseEntity deleteOrder(@PathVariable("order") String id) {
        Order order = new Order(id, user.getProfile());
        boolean success = orderservice.deleteOrder(order);
        if (success) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //list the order with particular id
    @RequestMapping(value = "/order/{order}/total", method = RequestMethod.GET)
    public ResponseEntity<Order> getOrderTotal(@PathVariable("order") String id) {
        Order order = orderservice.getOrder(id, user.getProfile());
        if(order==null){
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<OrderItem> items = orderItemService.getItems(order.getId());
        System.out.println(items);
        double total = 0;
        for (OrderItem item : items) {
            System.out.println(item.getItemId());
            Item i  = itemService.getItems(item.getItemId());
            System.out.print(i);
            if (i != null) {
                total += item.getQuantity() * i.getPrice();
            }
        }

        order.setTotalAmount(total);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }


}

