package com.tvv.service;

import com.tvv.db.entity.Payment;

public class PaymentService {
    public static void preparePayment(Payment payment) {
        payment.setStatus("");
    }

    public static void submitPayment(Payment payment) {

    }
}
