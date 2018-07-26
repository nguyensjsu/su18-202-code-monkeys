package edu.sjsu.cmpe202.starbucks.core.service.order.datastore;

import com.google.cloud.datastore.*;
import edu.sjsu.cmpe202.starbucks.beans.Order;
import edu.sjsu.cmpe202.starbucks.core.service.order.OrderService;

import java.util.ArrayList;
import java.util.List;

public class DatastoreOrderService implements OrderService {

    private static final String Kind = "Order";
    private Datastore datastore;

    //datastore object creation
    public DatastoreOrderService(){
        this.datastore = DatastoreOptions.getDefaultInstance().getService();
    }


    //create Order
    public boolean createOrder(Order order) throws Exception{
       if (this.exists(order)) {
            throw new Exception("order already exists");
        }

        FullEntity newCard = this.getEntity(order);
        Entity e = datastore.add(newCard);
        if (e == null) {
            return false;
        }

        return true;
    }



        //check order if already exists
        public boolean exists(Order order) {
            Order o = this.getOrder(order.getId(), order.getUser());
            if (o == null) {
                return false;
            }
            return true;
    }

    //delete order
    public boolean deleteOrder(Order order){
        if (! this.exists(order)) {
            return false;
        }

        Key cardKey = getKey(order);

        this.datastore.delete(cardKey);
        return true;
    }

    //add menu item in order
    public void addItemInOrder(){

    }

    //remove menu item in order
    public void removeItemFromOrder(){

    }

    //to get single order based on id
    public Order getOrder(String order, String user){
        Order temp = new Order(order, user);
        Key cardKey = this.getKey(temp);
        Entity e = datastore.get(cardKey);

        if (e == null) {
            return null;
        }

        Order o = this.getOrderFromEntity(e);
        o.setId(order);
        o.setUser(user);

        return o;
    }

    //to list all orders
    public List<Order> getOrders(String user){
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(Kind)
                .setFilter(StructuredQuery.PropertyFilter.hasAncestor(
                        datastore.newKeyFactory().setKind("User").newKey(user)))
                .build();

        QueryResults<Entity> entities = datastore.run(query);
        List<Order> orders = new ArrayList<Order>();

        while (entities.hasNext()) {
            Entity e = entities.next();
            orders.add(this.getOrderFromEntity(e));
        }

        return orders;
    }

    //order has ancestor : user
    private Key getKey(Order order) {
        return this.datastore.newKeyFactory()
                .setKind(Kind)
                .addAncestor(PathElement.of("User", order.getUser()))
                .newKey(order.getId());
    }

    //need to check
    private Entity getEntity(Order order) {
        Key cardKey = getKey(order);

        Entity newCard = Entity.newBuilder(cardKey)
                .set("orderno", order.getOrderId())
                .set("userid",order.getUserId())
                .build();

        return newCard;
    }

    //need to check
    private Order getOrderFromEntity(Entity entity) {
        return new Order(entity.getKey().getName(), "");
    }
}
