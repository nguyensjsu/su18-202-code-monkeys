package edu.sjsu.cmpe202.starbucks.api.v1;

import edu.sjsu.cmpe202.starbucks.beans.Item;
import edu.sjsu.cmpe202.starbucks.core.service.item.datastore.DatastoreItemService;
import edu.sjsu.cmpe202.starbucks.core.service.item.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ItemResource {
        private ItemService itemService;

        public ItemResource() {
            this.itemService = new DatastoreItemService();
        }

        @RequestMapping(value = "/item/{itemid}", method = RequestMethod.GET)
        public ResponseEntity<Item> getItem(@PathVariable("itemid") String id) {
            Item i = itemService.getItems(id);
            if (i == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }
            return new ResponseEntity<>(i, HttpStatus.OK);
        }

        @RequestMapping(value = "/items", method = RequestMethod.GET, produces = "application/json")
        public ResponseEntity getItems() {
            List<Item> i = itemService.getItems();
            if (i == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }
            return new ResponseEntity<>(i, HttpStatus.OK);
        }

        @RequestMapping(value = "/item", method = RequestMethod.POST, consumes = "application/json")
        public ResponseEntity addItem(@RequestBody Item item) {
            item.setName(item.getName());
            //item.setPrice(20f);
            try {
                boolean success = itemService.addItem(item);
                if (success) {
                    return new ResponseEntity(HttpStatus.ACCEPTED);
                } else {
                    return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (Exception e) {
                return new ResponseEntity<String>("Item already exists", HttpStatus.BAD_REQUEST);
            }
        }

        @RequestMapping(value = "/item/{itemid}", method = RequestMethod.PUT, consumes = "application/json")
        public ResponseEntity updateItem(@RequestBody Item item) {
            item.setName(item.getName());
            boolean success = itemService.updateItem(item);
            if (success) {
                return new ResponseEntity(HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @RequestMapping(value = "/item/{itemid}", method = RequestMethod.DELETE)
        public ResponseEntity deleteItem(@PathVariable("itemid") String id) {
            Item i = new Item(id, "", "", 0f);
            boolean success = itemService.deleteItem(i);
            if (success) {
                return new ResponseEntity(HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
}
