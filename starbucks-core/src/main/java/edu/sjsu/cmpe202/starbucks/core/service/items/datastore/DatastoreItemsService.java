package edu.sjsu.cmpe202.starbucks.core.service.items.datastore;

import com.google.cloud.datastore.*;
import edu.sjsu.cmpe202.starbucks.beans.Items;
import edu.sjsu.cmpe202.starbucks.core.service.items.ItemsService;

import java.util.ArrayList;
import java.util.List;

public class DatastoreItemsService implements ItemsService {

    private static final String Kind = "Items";
    private Datastore datastore;

    public DatastoreItemsService(){
        this.datastore = DatastoreOptions.getDefaultInstance().getService();
    }

    public boolean addItems(Items items) throws Exception {
        FullEntity newItems = this.getEntity(items);
        Entity e = datastore.add(newItems);
        if (e == null) {
            return false;
        }
        return true;
    }

    public boolean updateItems(Items items) {
        if (! this.exists(items)) {
            return false;
        }

        Entity e = this.getEntity(items);
        datastore.update(e);
        return true;
    }

    public boolean exists(Items items) {
        Items i = this.getItems(items.getId());
        if (i == null) {
            return false;
        }

        return true;
    }

    public boolean deleteItems(Items items) {
        if (! this.exists(items)) {
            return false;
        }

        Key itemsKey = getKey(items);

        this.datastore.delete(itemsKey);
        return true;
    }

    public Items getItems(String items) {
        Items temp = new Items(items, "", "", 0f);
        Key itemsKey = this.getKey(temp);
        Entity e = datastore.get(itemsKey);

        if (e == null) {
            return null;
        }

        Items i = this.getItemsFromEntity(e);
        i.setId(items);

        return i;
    }

    public List<Items> getItems() {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(Kind)
                .build();

        QueryResults<Entity> entities = datastore.run(query);
        List<Items> items = new ArrayList<Items>();
        while (entities.hasNext()) {
            Entity e = entities.next();
            items.add(this.getItemsFromEntity(e));
        }
        return items;
    }

    private Key getKey(Items items) {
        return this.datastore.newKeyFactory()
                .setKind(Kind)
                .newKey(items.getId());
    }

    private Entity getEntity(Items items) {
        Key itemsKey = getKey(items);

        Entity newItems = Entity.newBuilder(itemsKey)
                .set("name", items.getName())
                .set("desc", items.getDesc())
                .set("price", items.getPrice())
                .build();

        return newItems;
    }

    private Items getItemsFromEntity(Entity entity) {
        return new Items(entity.getKey().getName(), "", entity.getString("desc"), entity.getDouble("price"));
    }

}
