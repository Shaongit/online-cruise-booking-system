/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.service.impl;

import com.cruise.booking.entity.PaymentTransaction;
import com.cruise.booking.repository.PaymentTransactionRepository;
import com.cruise.booking.service.PaymentTransactionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of PaymentTransactionService interface.
 * Provides business logic for managing payment transactions and processing.
 */
@Service
public class PaymentTransactionServiceImpl implements PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;

    public PaymentTransactionServiceImpl(PaymentTransactionRepository paymentTransactionRepository) {
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    @Override
    public List<PaymentTransaction> getAllTransactions() {
        return paymentTransactionRepository.findAll();
    }

    @Override
    public Optional<PaymentTransaction> getTransactionById(Long id) {
        return paymentTransactionRepository.findById(id);
    }

    @Override
    public List<PaymentTransaction> getTransactionsByBookingId(Long bookingId) {
        return paymentTransactionRepository.findByBookingBookingId(bookingId);
    }

    @Override
    public PaymentTransaction saveTransaction(PaymentTransaction transaction) {
        return paymentTransactionRepository.save(transaction);
    }

    @Override
    public PaymentTransaction updateTransaction(Long id, PaymentTransaction transaction) {
        transaction.setTransactionId(id);
        return paymentTransactionRepository.save(transaction);
    }

    @Override
    public void deleteTransaction(Long id) {
        paymentTransactionRepository.deleteById(id);
    }
}
