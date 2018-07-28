package edu.sjsu.cmpe202.starbucks.api.v1;

import edu.sjsu.cmpe202.starbucks.beans.OrderItem;
import edu.sjsu.cmpe202.starbucks.beans.User;
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
public class OrderItemResource {

    private User user;
    private OrderItemService orderItemService;
    private UserService userService;

    public OrderItemResource() {
        orderItemService = new DatastoreOrderItemService();
        userService = new DatastoreUserService();
    }

    @RequestMapping(value = "/order/{order}/item", method = RequestMethod.POST,consumes = "application/json")
    public ResponseEntity addOrder(@RequestBody OrderItem item, @PathVariable("order") String id) {
        item.setOrderId(id);

        try {
            boolean success = orderItemService.addItem(item);
            if (success) {
                return new ResponseEntity(HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>("Order already exists from controller", HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/order/{order}/item/{item}", method = RequestMethod.GET)
    public ResponseEntity<OrderItem> getOrder(@PathVariable("order") String order, @PathVariable("item") String itemId) {
        OrderItem item = orderItemService.getItem(itemId, order);

        if(order == null){
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @RequestMapping(value = "/order/{order}/items", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getOrders(@PathVariable("order") String order) {
        List<OrderItem> items = orderItemService.getItems(order);

        if (items == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @RequestMapping(value = "/order/{order}/item/{item}", method = RequestMethod.PUT)
    public ResponseEntity updateOrderItem(@RequestBody OrderItem orderItem, @PathVariable("order") String order,
                                          @PathVariable("item") String item ) {
        orderItem.setId(item);
        boolean success = orderItemService.updateItem(orderItem);
        if(!success){
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @RequestMapping(value = "/order/{order}/item/{item}", method = RequestMethod.DELETE)
    public ResponseEntity deleteOrderItem(@PathVariable("order") String order,
                                          @PathVariable("item") String item) {
        OrderItem orderItem = new OrderItem(item, order, "", "", 0f);
        boolean success = orderItemService.deleteItem(orderItem);
        if (success) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
