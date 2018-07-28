package edu.sjsu.cmpe202.starbucks.core.service.orderitem.datastore;

import com.google.cloud.datastore.*;
import edu.sjsu.cmpe202.starbucks.beans.OrderItem;
import edu.sjsu.cmpe202.starbucks.core.service.orderitem.OrderItemService;

import java.util.ArrayList;
import java.util.List;

public class DatastoreOrderItemService implements OrderItemService {
    private static final String Kind = "OrderItem";
    private Datastore datastore;

    public DatastoreOrderItemService() {
        this.datastore = DatastoreOptions.getDefaultInstance().getService();
    }

    @Override
    public boolean addItem(OrderItem item) throws Exception {
        if (this.exists(item)) {
            throw new Exception("order item already exists");
        }

        FullEntity newItem = this.getEntity(item);
        Entity e = datastore.add(newItem);
        if (e == null) {
            return false;
        }

        return true;
    }

    @Override
    public boolean updateItem(OrderItem item) {
        if (! this.exists(item)) {
            return false;
        }

        Entity e = this.getEntity(item);
        datastore.update(e);
        return true;
    }

    @Override
    public List<OrderItem> getItems(String order) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(Kind)
                .setFilter(StructuredQuery.PropertyFilter.hasAncestor(
                        datastore.newKeyFactory().setKind("Order").newKey(order)))
                .build();

        QueryResults<Entity> entities = datastore.run(query);
        List<OrderItem> orderItems = new ArrayList<OrderItem>();

        while (entities.hasNext()) {
            Entity e = entities.next();
            orderItems.add(this.getOrderItemFromEntity(e));
        }

        return orderItems;
    }

    @Override
    public OrderItem getItem(String item, String order) {
        OrderItem temp = new OrderItem(item, order, "", "", 0f);
        Key cardKey = this.getKey(temp);
        Entity e = datastore.get(cardKey);

        if (e == null) {
            return null;
        }

        OrderItem i = this.getOrderItemFromEntity(e);
        i.setId(item);
        i.setOrderId(order);

        return i;
    }

    @Override
    public boolean exists(OrderItem item) {
        OrderItem i = this.getItem(item.getId(), item.getOrderId());
        if (i == null) {
            return false;
        }

        return true;
    }

    public boolean deleteItem(OrderItem item){
        if (! this.exists(item)) {
            return false;
        }

        Key orderKey = getKey(item);

        this.datastore.delete(orderKey);
        return true;
    }

    //order has ancestor : user
    private Key getKey(OrderItem item) {
        return this.datastore.newKeyFactory()
                .setKind(Kind)
                .addAncestor(PathElement.of("Order", item.getOrderId()))
                .newKey(item.getId());
    }

    private Entity getEntity(OrderItem item) {
        Key orderKey = getKey(item);

        Entity newOrder = Entity.newBuilder(orderKey)
                .set("id", item.getItemId())
                .set("quantity", item.getQuantity())
                .set("comments", item.getComments())
                .build();
        return newOrder;
    }

    private OrderItem getOrderItemFromEntity(Entity entity) {
        return new OrderItem( entity.getKey().getName(),"",
                entity.getString("id"), entity.getString("comments"), entity.getDouble("quantity"));
    }
}
