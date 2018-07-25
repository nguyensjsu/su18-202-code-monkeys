package edu.sjsu.cmpe202.starbucks.api.v1;

import edu.sjsu.cmpe202.starbucks.beans.Card;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")

public class OrderResouce {


    @RequestMapping(value = "/card/{card}", method = RequestMethod.GET)
    public ResponseEntity<Card> getCard() {

        return new ResponseEntity<>(c, HttpStatus.OK);
    }
}
//why
