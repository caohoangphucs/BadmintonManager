package com.example.winfinal.dao;

import com.example.winfinal.entity.Payment;

public class PaymentDAO extends BaseDAO<Payment, Integer> {
    public PaymentDAO() {
        super(Payment.class);
    }
}
