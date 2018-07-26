package edu.sjsu.cmpe202.starbucks.core.service.payments;

public enum PaymentStatus {
        INVALID_OR_MISSING_CARDID,
        INSUFFICIENT_BALANCE,
        INVALID_OR_MISSING_ORDERID,
        FAILURE_IN_UPDATING_BALANCE,
        EXCEPTION_IN_PAYMENT,
        SUCCESFUL_CARD_UPDATE,
        SUCCESSFUL_PAYMENT_CARD_UPDATE
}

