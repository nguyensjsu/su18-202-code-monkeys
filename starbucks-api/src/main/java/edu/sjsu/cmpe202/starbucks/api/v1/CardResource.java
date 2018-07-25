package edu.sjsu.cmpe202.starbucks.api.v1;

import edu.sjsu.cmpe202.starbucks.beans.Card;
import edu.sjsu.cmpe202.starbucks.beans.User;
import edu.sjsu.cmpe202.starbucks.core.service.card.CardService;
import edu.sjsu.cmpe202.starbucks.core.service.card.datastore.DatastoreCardService;
import edu.sjsu.cmpe202.starbucks.core.service.user.UserService;
import edu.sjsu.cmpe202.starbucks.core.service.user.datastore.DatastoreUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CardResource {
    private UserService userService;
    private CardService cardService;
    private User user;

    public CardResource() {
        this.user = new User("foo", "bar", "testprofile");
        this.userService =  new DatastoreUserService();
        this.cardService = new DatastoreCardService();
    }

    @RequestMapping(value = "/card/{card}", method = RequestMethod.GET)
    public ResponseEntity<Card> getCard(@PathVariable("card") String id) {
        Card c = cardService.getCard(id, user.getProfile());
        if (c == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(c, HttpStatus.OK);
    }

    @RequestMapping(value = "/cards", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getCards() {
        List<Card> c = cardService.getCards(user.getProfile());
        if (c == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(c, HttpStatus.OK);
    }

    @RequestMapping(value = "/card", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity addCard(@RequestBody Card card) {
        this.userService.insertUser(this.user);
        card.setUser(this.user.getProfile());
        card.setBalance(20f);//
        try {
            boolean success = cardService.addCard(card);
            if (success) {
                return new ResponseEntity(HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>("Card already exists", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/card/{card}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity updateCard(@RequestBody Card card) {
        card.setUser(this.user.getProfile());
        boolean success = cardService.updateCard(card);
        if (success) {
            return new ResponseEntity(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/card/{card}", method = RequestMethod.DELETE)
    public ResponseEntity deleteCard(@PathVariable("card") String id) {
        Card c = new Card(id, user.getProfile(), "", 0f);
        boolean success = cardService.deleteCard(c);
        if (success) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
