package com.tvv.db.dao;

import com.tvv.db.entity.Payment;
import com.tvv.service.exception.AppException;

import java.util.List;

public interface PaymentDAO {
    List<Payment> findAllPayments() throws AppException;

    Payment findPaymentById(Long id) throws AppException;

    List<Payment> findPaymentsByUser(Long id) throws AppException;

    boolean updatePaymentStatus(Long id, String status) throws AppException;

    boolean insertPayment(Payment payment) throws AppException;

    boolean deletePaymentById(Long id) throws AppException;
}
