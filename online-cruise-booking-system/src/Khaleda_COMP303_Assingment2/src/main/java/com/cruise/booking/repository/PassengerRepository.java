/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.repository;

import com.cruise.booking.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Passenger entity.
 * Provides database access methods for passenger-related operations.
 * Supports finding passengers by booking.
 */
@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    /**
     * Finds all passengers associated with a specific booking.
     * @param bookingId the booking ID to search for
     * @return list of passengers
     */
    List<Passenger> findByBookingBookingId(Long bookingId);
}
