package edu.sjsu.cmpe202.starbucks.api.v1;

import edu.sjsu.cmpe202.starbucks.beans.Payment;
import edu.sjsu.cmpe202.starbucks.beans.User;
import edu.sjsu.cmpe202.starbucks.core.service.card.CardService;
import edu.sjsu.cmpe202.starbucks.core.service.card.datastore.DatastoreCardService;
import edu.sjsu.cmpe202.starbucks.core.service.payments.PaymentService;
import edu.sjsu.cmpe202.starbucks.core.service.payments.PaymentStatus;
import edu.sjsu.cmpe202.starbucks.core.service.payments.datastore.DatastorePaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PaymentResource {
    private User user;

    private PaymentService service;
        private CardService cardService;

        public PaymentResource() {

            service = new DatastorePaymentService();
            cardService = new DatastoreCardService();
            user = new User("foo", "bar", "testprofile");

        }



    @RequestMapping(value = "/payment", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getPaymentsInRange(@RequestParam(value ="low") Double low, @RequestParam("high") Double high) {


        List<Payment> list = service.getPaymentsInRange(low, high);
        return new ResponseEntity<List<Payment>>(list, HttpStatus.OK);

    }

    @RequestMapping(value = "/payment/card/{cardid}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getPaymentsByCardIds(@PathVariable("cardid") String id) {
        List<Payment> p = service.getPaymentsByCardId(id);
        if (p == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(p, HttpStatus.OK);
    }


    @RequestMapping(value = "/allpayments", method = RequestMethod.GET, produces = "application/json")
        public ResponseEntity getAllPayments() {

            List<Payment> p = service.getAllPayments();
            if (p == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }
            return new ResponseEntity<>(p, HttpStatus.OK);
        }

        @RequestMapping(value = "/payment", method = RequestMethod.POST, consumes = "application/json")
        public ResponseEntity addPayment(@RequestBody Payment payment) {
            payment = new Payment(payment.getOrderId(), payment.getCardId(), payment.getPay());
            PaymentStatus status = service.performPaymentValidation(payment, this.user, cardService);
            if (status != PaymentStatus.SUCCESFUL_CARD_UPDATE) {
                return new ResponseEntity<PaymentStatus>(status, HttpStatus.EXPECTATION_FAILED);
            }


            try {
                boolean success = service.addPayment(payment);
                if (success) {
                    return new ResponseEntity<PaymentStatus>(PaymentStatus.SUCCESSFUL_PAYMENT_CARD_UPDATE, HttpStatus.ACCEPTED);
                } else {
                    return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (Exception e) {
                return new ResponseEntity<PaymentStatus>(PaymentStatus.EXCEPTION_IN_PAYMENT, HttpStatus.BAD_REQUEST);
            }
        }


}
