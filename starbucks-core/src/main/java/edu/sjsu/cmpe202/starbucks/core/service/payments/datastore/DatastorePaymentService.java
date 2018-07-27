package edu.sjsu.cmpe202.starbucks.core.service.payments.datastore;

import com.google.cloud.datastore.*;
import edu.sjsu.cmpe202.starbucks.beans.Payment;
import edu.sjsu.cmpe202.starbucks.beans.User;
import edu.sjsu.cmpe202.starbucks.core.service.card.CardService;
import edu.sjsu.cmpe202.starbucks.core.service.payments.PaymentService;
import edu.sjsu.cmpe202.starbucks.core.service.payments.PaymentStatus;

import java.util.LinkedList;
import java.util.List;


public class DatastorePaymentService implements PaymentService {

    private static final String Kind = "Payment";
    private static final String Card = "Card";
    private Datastore datastore;
    private KeyFactory keyFactory;

    public DatastorePaymentService(){
        this.datastore = DatastoreOptions.getDefaultInstance().getService();
        this.keyFactory = datastore.newKeyFactory().setKind(Kind);
    }




    /**
     * Get the payment id and construct a Key for calling the table
     * @param payment
     * @return
     */
    private Key getKey(Payment payment) {
        return this.datastore.newKeyFactory()
                .setKind(Kind)
                .newKey(payment.getPaymentId());
    }

    /**
     * using the payment object build the entity
     * @param payment
     * @return
     */
    private Entity getEntity(Payment payment) {
        Key paymentKey = getKey(payment);

        Entity paymentEntity = Entity.newBuilder(paymentKey)
                .set("orderId", payment.getOrderId())
                .set("cardId", payment.getCardId())
                .set("pay", payment.getPay())
                .build();

        return paymentEntity;
    }

    private Payment getPaymentFromEntity(Entity entity) {
        String orderId = entity.getString("orderId");
        String cardId = entity.getString("cardId");
        double pay = entity.getDouble("pay");
        String paymentId = entity.getKey().getName();
        Payment payment = new Payment(paymentId, orderId, cardId, pay);
        return payment;
    }

    @Override
    public boolean addPayment(Payment payment) {
        if (payment.getPay() == 0) {
            return false;
        }

        String cardId = payment.getCardId();
        FullEntity paymentEntry = this.getEntity(payment);
        Entity e = datastore.add(paymentEntry);
        if (e == null)
            return false;
        return true;
    }




    @Override
    public List<Payment> getAllPayments() {

        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(Kind)
                .build();
        QueryResults<Entity> resultList = datastore.run(query);   // Run the query
        List<Payment> paymentList = entitiesToPayments(resultList);
        return paymentList;

    }



    @Override
    public List<Payment> getPaymentsInRange(Double low, Double high) {
        if (low == null) {
            low = 0.0;
        }
        List<Payment> allPayments = getAllPayments();
        List<Payment> matchingPayments = new LinkedList<>();
        for(Payment payment : allPayments) {
            if (high != null) {
                if (payment.getPay() >= low && payment.getPay() <= high) {
                    matchingPayments.add(payment);
                }
            } else {
                if (payment.getPay() >= low) {
                    matchingPayments.add(payment);
                }

            }

        }

        return matchingPayments;


    }

    @Override
    public boolean deletePayment(String paymentId) {
        Key key = this.datastore.newKeyFactory()
                .setKind(Kind)
                .newKey(paymentId);
        try {
            Entity e = datastore.get(key);
            if (e == null) {
                return false;
            }
            this.datastore.delete(key);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updatePayment(Payment payment) {
        Key key = this.datastore.newKeyFactory()
                .setKind(Kind)
                .newKey(payment.getPaymentId());
        if (datastore.get(key) != null) {
            Entity e = this.getEntity(payment);
            datastore.update(e);
        }
        return false;
    }


    @Override
    public List<Payment> getPaymentsByCardId(String cardId) {

        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(Kind)
                .build();
        QueryResults<Entity> resultList = datastore.run(query);   // Run the query
        List<Payment> paymentList = entitiesToPayments(resultList);
        return filterListByCardId(paymentList, cardId);

    }

    @Override
    public Payment getPaymentFromPaymentId(String paymentId) {
        List<Payment> allPayments = getAllPayments();
        if (allPayments.size() == 0) return null;
        return filterListByPaymentId(allPayments, paymentId).get(0);
    }


    private List<Payment> filterListByCardId(List<Payment> list, String cardId) {
        List<Payment> result = new LinkedList<>();
        for(Payment payment: list) {
            if (payment.getCardId().equals(cardId)) {
                result.add(payment);
            }
        }

        return result;
    }

    private List<Payment> filterListByOrderId(List<Payment> list, String orderId) {
        List<Payment> result = new LinkedList<>();
        for(Payment payment: list) {
            if (payment.getOrderId().equals(orderId)) {
                result.add(payment);
            }
        }

        return result;
    }


    private List<Payment> filterListByPaymentId(List<Payment> list, String paymentId) {
        List<Payment> result = new LinkedList<>();
        for(Payment payment: list) {
            if (payment.getPaymentId().equals(paymentId)) {
                result.add(payment);
            }
        }

        return result;
    }





    public List<Payment> entitiesToPayments(QueryResults<Entity> resultList) {
        List<Payment> list = new LinkedList<>();
        while (resultList.hasNext()) {
            list.add(getPaymentFromEntity(resultList.next()));
        }
        return list;
    }

    /**
     * When a payment is removed or updated, card balance has to be updated
     * @param oldPayment
     * @param user
     * @param cardService
     * @param newPayment

     * @return
     */
    public PaymentStatus performPaymentUpdate(Payment oldPayment, User user, CardService cardService, Payment newPayment) {
        edu.sjsu.cmpe202.starbucks.beans.Card c = cardService.getCard(oldPayment.getCardId(), user.getProfile());
     if (getPaymentFromPaymentId(oldPayment.getPaymentId()) ==null) {
       return PaymentStatus.FAILURE_IN_UPDATING_BALANCE;
     }
      double newBalance = c.getBalance() + oldPayment.getPay();
      c.setBalance(c.getBalance() + oldPayment.getPay());
      if (newBalance > 0) {
        if (newPayment != null) {
          newBalance = newBalance - newPayment.getPay();
          if (newBalance >= 0) {
            c.setBalance(c.getBalance() - newPayment.getPay());
          } else {
            return PaymentStatus.INSUFFICIENT_BALANCE;
          }
        }
      }
      cardService.updateCard(c);

      return PaymentStatus.SUCCESFUL_CARD_UPDATE;

    }


    public PaymentStatus performPaymentValidation(Payment payment, User user, CardService cardService){
        if (payment.getOrderId() == null || payment.getOrderId().isEmpty()) {
            return PaymentStatus.INVALID_OR_MISSING_ORDERID;

        }
        edu.sjsu.cmpe202.starbucks.beans.Card c = cardService.getCard(payment.getCardId(), user.getProfile());
        if (c == null){
            return  PaymentStatus.INVALID_OR_MISSING_CARDID;

        }

        if (c.getBalance() < payment.getPay()) {
            return PaymentStatus.INSUFFICIENT_BALANCE;
        }

        c.setBalance(c.getBalance() - payment.getPay());
        boolean balanceUpdated = cardService.updateCard(c);

        if (!balanceUpdated) {
            return PaymentStatus.FAILURE_IN_UPDATING_BALANCE;
        }

        return PaymentStatus.SUCCESFUL_CARD_UPDATE;

    }
}
