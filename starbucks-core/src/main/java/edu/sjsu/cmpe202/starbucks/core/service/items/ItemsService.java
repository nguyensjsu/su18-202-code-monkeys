package edu.sjsu.cmpe202.starbucks.core.service.items;

import edu.sjsu.cmpe202.starbucks.beans.Items;

import java.util.List;

public interface ItemsService {
    public boolean addItems(Items items) throws Exception;
    public boolean updateItems(Items items);
    public boolean deleteItems(Items items);
    public Items getItems(String items);
    public List<Items> getItems();
}
