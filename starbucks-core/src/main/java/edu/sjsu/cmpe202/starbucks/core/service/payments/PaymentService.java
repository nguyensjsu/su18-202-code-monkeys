package edu.sjsu.cmpe202.starbucks.core.service.payments;

import edu.sjsu.cmpe202.starbucks.beans.Payment;
import edu.sjsu.cmpe202.starbucks.beans.User;
import edu.sjsu.cmpe202.starbucks.core.service.card.CardService;

import java.util.List;

public interface PaymentService {
    public boolean addPayment(Payment payment);
    public List<Payment> getAllPayments();
    public List<Payment> getAllPayments(String cardId);
    public PaymentStatus performPaymentValidation(Payment payment, User user, CardService cardService);

}
