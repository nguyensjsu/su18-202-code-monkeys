package edu.sjsu.cmpe202.starbucks.core.service.payments;

import edu.sjsu.cmpe202.starbucks.beans.Payment;
import edu.sjsu.cmpe202.starbucks.beans.User;
import edu.sjsu.cmpe202.starbucks.core.service.card.CardService;

import java.util.List;

public interface PaymentService {
    public boolean addPayment(Payment payment);
    public List<Payment> getAllPayments();
    public List<Payment> getPaymentsByCardId(String cardId);
    public Payment getPaymentFromPaymentId(String paymentId);
    public List<Payment> getPaymentsInRange(Double low, Double high);
    public PaymentStatus performPaymentValidation(Payment payment, User user, CardService cardService);
    public PaymentStatus performPaymentUpdate(Payment oldPayment, User user, CardService cardService, Payment newPayment);
    public boolean deletePayment(String paymentId);
    public boolean updatePayment(Payment payment);

}
