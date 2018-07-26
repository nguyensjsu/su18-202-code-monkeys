package edu.sjsu.cmpe202.starbucks.api.v1;

import edu.sjsu.cmpe202.starbucks.beans.Payment;
import edu.sjsu.cmpe202.starbucks.core.service.payments.PaymentService;
import edu.sjsu.cmpe202.starbucks.core.service.payments.datastore.DatastorePaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PaymentResource {
        private PaymentService service;

        public PaymentResource() {
            service = new DatastorePaymentService();
        }


    @RequestMapping(value = "/payment/card/{cardid}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getItems(@PathVariable("cardid") String id) {
        List<Payment> p = service.getAllPayments(id);
        if (p == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(p, HttpStatus.OK);
    }


    @RequestMapping(value = "/allpayments", method = RequestMethod.GET, produces = "application/json")
        public ResponseEntity getItems() {

            List<Payment> p = service.getAllPayments();
            if (p == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }
            return new ResponseEntity<>(p, HttpStatus.OK);
        }

        @RequestMapping(value = "/payment", method = RequestMethod.POST, consumes = "application/json")
        public ResponseEntity addPayment(@RequestBody Payment payment) {
            //item.setPrice(20f);
            payment = new Payment(payment.getOrderId(), payment.getCardId(), payment.getPay());
            try {
                boolean success = service.addPayment(payment);
                if (success) {
                    return new ResponseEntity(HttpStatus.ACCEPTED);
                } else {
                    return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (Exception e) {
                return new ResponseEntity<String>("exception" + e.toString(), HttpStatus.BAD_REQUEST);
            }
        }


}
