/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.repository;

import com.cruise.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Booking entity.
 * Provides database access methods for booking-related operations.
 * Supports queries by user, cruise, and booking status.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    /**
     * Finds all bookings made by a specific user.
     * @param userId the user ID to search for
     * @return list of bookings
     */
    List<Booking> findByUserUserId(Long userId);
    
    /**
     * Finds all bookings for a specific cruise.
     * @param cruiseId the cruise ID to search for
     * @return list of bookings
     */
    List<Booking> findByCruiseCruiseId(Long cruiseId);
    
    /**
     * Finds all bookings with a specific status.
     * @param bookingStatus the status to filter by (e.g., CONFIRMED, PENDING, CANCELLED)
     * @return list of bookings
     */
    List<Booking> findByBookingStatus(String bookingStatus);
}
