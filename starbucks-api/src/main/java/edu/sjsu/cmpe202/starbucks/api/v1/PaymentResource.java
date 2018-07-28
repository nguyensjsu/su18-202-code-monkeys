package edu.sjsu.cmpe202.starbucks.api.v1;

import edu.sjsu.cmpe202.starbucks.beans.Order;
import edu.sjsu.cmpe202.starbucks.beans.Payment;
import edu.sjsu.cmpe202.starbucks.beans.User;
import edu.sjsu.cmpe202.starbucks.core.service.card.CardService;
import edu.sjsu.cmpe202.starbucks.core.service.card.datastore.DatastoreCardService;
import edu.sjsu.cmpe202.starbucks.core.service.order.OrderService;
import edu.sjsu.cmpe202.starbucks.core.service.order.datastore.DatastoreOrderService;
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

    private PaymentService service;
        private CardService cardService;
        private OrderService orderService;

        public PaymentResource() {

            service = new DatastorePaymentService();
            cardService = new DatastoreCardService();
            orderService = new DatastoreOrderService();

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


    @RequestMapping(value = "/payment/{paymentId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity deletePayment(@PathVariable("paymentId") String paymentId, @RequestAttribute(name ="user") User user) {

        Payment payment = service.getPaymentFromPaymentId(paymentId);
        PaymentStatus status = service.performPaymentUpdate(payment, user, cardService, null);
        if (status == PaymentStatus.SUCCESFUL_CARD_UPDATE) {
            if (service.deletePayment(paymentId)) {
                return new ResponseEntity<String>(status.toString() + paymentId, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @RequestMapping(value = "/payment", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity updatePayment(@RequestBody Payment newPayment, @RequestAttribute(name ="user") User user) {
        try {
            Payment oldPayment = service.getPaymentFromPaymentId(newPayment.getPaymentId());
            if (oldPayment == null) {
                return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
            }
            PaymentStatus status = service.performPaymentUpdate(oldPayment, user, cardService, newPayment);

            if (status == PaymentStatus.SUCCESFUL_CARD_UPDATE) {
                if (service.updatePayment(newPayment))
                    return new ResponseEntity<String>("Updated:" + newPayment.getPaymentId() + " " + status, HttpStatus.OK);
            }

            return new ResponseEntity<String>(status.toString() , HttpStatus.EXPECTATION_FAILED);
        } catch (Exception e) {
            return new ResponseEntity<String>("Exception", HttpStatus.EXPECTATION_FAILED);

        }

    }

    @RequestMapping(value = "/payments", method = RequestMethod.GET, produces = "application/json")
        public ResponseEntity getAllPayments() {

            List<Payment> p = service.getAllPayments();
            if (p == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }
            return new ResponseEntity<>(p, HttpStatus.OK);
        }

        @RequestMapping(value = "/payment", method = RequestMethod.POST, consumes = "application/json")
        public ResponseEntity addPayment(@RequestBody Payment payment, @RequestAttribute(name ="user") User user) {
            payment = new Payment(payment.getOrderId(), payment.getCardId(), payment.getPay());
            String orderId = payment.getOrderId();
            Order order = orderService.getOrder(orderId, user.getProfile());
            if (order == null)
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            PaymentStatus status = service.performPaymentValidation(payment, user, cardService);
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
