package edu.sjsu.cmpe202.starbucks.api.v1;

import edu.sjsu.cmpe202.starbucks.beans.Items;
import edu.sjsu.cmpe202.starbucks.core.service.items.datastore.DatastoreItemsService;
import edu.sjsu.cmpe202.starbucks.core.service.items.ItemsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ItemsResource {
        private ItemsService itemsService;

        public ItemsResource() {
            this.itemsService = new DatastoreItemsService();
        }

        @RequestMapping(value = "/items/{item}", method = RequestMethod.GET)
        public ResponseEntity<Items> getItem(@PathVariable("item") String id) {
            Items i = itemsService.getItems(id);
            if (i == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }
            return new ResponseEntity<>(i, HttpStatus.OK);
        }

        @RequestMapping(value = "/items", method = RequestMethod.GET, produces = "application/json")
        public ResponseEntity getItems() {
            List<Items> i = itemsService.getItems();
            if (i == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }
            return new ResponseEntity<>(i, HttpStatus.OK);
        }

        @RequestMapping(value = "/item", method = RequestMethod.POST, consumes = "application/json")
        public ResponseEntity addItems(@RequestBody Items items) {
            items.setName(items.getName());  //check this
            items.setPrice(20f);
            try {
                boolean success = itemsService.addItems(items);
                if (success) {
                    return new ResponseEntity(HttpStatus.ACCEPTED);
                } else {
                    return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (Exception e) {
                return new ResponseEntity<String>("Item already exists", HttpStatus.BAD_REQUEST);
            }
        }

        @RequestMapping(value = "/items/{item}", method = RequestMethod.PUT, consumes = "application/json")
        public ResponseEntity updateItems(@RequestBody Items items) {
            items.setName(items.getName()); //check this
            boolean success = itemsService.updateItems(items);
            if (success) {
                return new ResponseEntity(HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @RequestMapping(value = "/items/{item}", method = RequestMethod.DELETE)
        public ResponseEntity deleteItems(@PathVariable("item") String id) {
            Items i = new Items(id, "", "", 0f);  //check this
            boolean success = itemsService.deleteItems(i);
            if (success) {
                return new ResponseEntity(HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
}
