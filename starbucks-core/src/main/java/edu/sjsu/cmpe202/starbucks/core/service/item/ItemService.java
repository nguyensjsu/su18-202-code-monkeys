package edu.sjsu.cmpe202.starbucks.core.service.item;

import edu.sjsu.cmpe202.starbucks.beans.Item;

import java.util.List;

public interface ItemService {
    public boolean addItem(Item item) throws Exception;
    public boolean updateItem(Item item);
    public boolean deleteItem(Item item);
    public Item getItems(String items);
    public List<Item> getItems();
}
