package edu.sjsu.cmpe202.starbucks.core.service.item.datastore;

import com.google.cloud.datastore.*;
import edu.sjsu.cmpe202.starbucks.beans.Item;
import edu.sjsu.cmpe202.starbucks.core.service.item.ItemService;

import java.util.ArrayList;
import java.util.List;

public class DatastoreItemService implements ItemService {

    private static final String Kind = "Item";
    private Datastore datastore;

    public DatastoreItemService(){
        this.datastore = DatastoreOptions.getDefaultInstance().getService();
    }

    public boolean addItem(Item item) throws Exception {
        FullEntity newItems = this.getEntity(item);
        Entity e = datastore.add(newItems);
        if (e == null) {
            return false;
        }
        return true;
    }

    public boolean updateItem(Item item) {
        if (! this.exists(item)) {
            return false;
        }

        Entity e = this.getEntity(item);
        datastore.update(e);
        return true;
    }

    public boolean exists(Item item) {
        Item i = this.getItems(item.getId());
        if (i == null) {
            return false;
        }

        return true;
    }

    public boolean deleteItem(Item item) {
        if (! this.exists(item)) {
            return false;
        }

        Key itemsKey = getKey(item);

        this.datastore.delete(itemsKey);
        return true;
    }

    public Item getItems(String items) {
        Item temp = new Item(items, "", "", 0f);
        Key itemsKey = this.getKey(temp);
        Entity e = datastore.get(itemsKey);

        if (e == null) {
            return null;
        }

        Item i = this.getItemsFromEntity(e);
        i.setId(items);

        return i;
    }

    public List<Item> getItems() {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(Kind)
                .build();

        QueryResults<Entity> entities = datastore.run(query);
        List<Item> items = new ArrayList<Item>();
        while (entities.hasNext()) {
            Entity e = entities.next();
            items.add(this.getItemsFromEntity(e));
        }
        return items;
    }

    private Key getKey(Item item) {
        return this.datastore.newKeyFactory()
                .setKind(Kind)
                .newKey(item.getId());
    }

    private Entity getEntity(Item item) {
        Key itemsKey = getKey(item);

        Entity newItems = Entity.newBuilder(itemsKey)
                .set("name", item.getName())
                .set("desc", item.getDesc())
                .set("price", item.getPrice())
                .build();

        return newItems;
    }

    private Item getItemsFromEntity(Entity entity) {
        return new Item(entity.getKey().getName(), entity.getString("name"), entity.getString("desc"), entity.getDouble("price"));
    }

}
