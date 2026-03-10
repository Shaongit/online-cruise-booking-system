/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.service;

import com.cruise.booking.entity.PaymentTransaction;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for PaymentTransaction operations.
 * Defines business logic methods for managing payment transactions.
 */
public interface PaymentTransactionService {
    /**
     * Retrieves all payment transactions.
     * @return list of all transactions
     */
    List<PaymentTransaction> getAllTransactions();
    
    /**
     * Retrieves a payment transaction by its ID.
     * @param id the transaction ID
     * @return Optional containing the transaction if found
     */
    Optional<PaymentTransaction> getTransactionById(Long id);
    
    /**
     * Retrieves all transactions for a specific booking.
     * @param bookingId the booking ID
     * @return list of payment transactions
     */
    List<PaymentTransaction> getTransactionsByBookingId(Long bookingId);
    
    /**
     * Saves a new payment transaction.
     * @param transaction the transaction to save
     * @return the saved transaction
     */
    PaymentTransaction saveTransaction(PaymentTransaction transaction);
    
    /**
     * Updates an existing payment transaction.
     * @param id the transaction ID to update
     * @param transaction the updated transaction data
     * @return the updated transaction
     */
    PaymentTransaction updateTransaction(Long id, PaymentTransaction transaction);
    
    /**
     * Deletes a payment transaction by ID.
     * @param id the transaction ID to delete
     */
    void deleteTransaction(Long id);
}
