/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.repository;

import com.cruise.booking.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for PaymentTransaction entity.
 * Provides database access methods for payment transaction operations.
 * Supports finding transactions by booking and status.
 */
@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    /**
     * Finds all payment transactions for a specific booking.
     * @param bookingId the booking ID to search for
     * @return list of payment transactions
     */
    List<PaymentTransaction> findByBookingBookingId(Long bookingId);
    
    /**
     * Finds all payment transactions with a specific status.
     * @param transactionStatus the transaction status (e.g., COMPLETED, PENDING, FAILED)
     * @return list of payment transactions
     */
    List<PaymentTransaction> findByTransactionStatus(String transactionStatus);
}
