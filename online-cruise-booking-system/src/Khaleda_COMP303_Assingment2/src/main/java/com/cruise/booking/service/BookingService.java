/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.service;

import com.cruise.booking.entity.Booking;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Booking operations.
 * Defines business logic methods for managing cruise bookings.
 */
public interface BookingService {
    /**
     * Retrieves all bookings.
     * @return list of all bookings
     */
    List<Booking> getAllBookings();
    
    /**
     * Retrieves a booking by its ID.
     * @param id the booking ID
     * @return Optional containing the booking if found
     */
    Optional<Booking> getBookingById(Long id);
    
    /**
     * Retrieves all bookings made by a specific user.
     * @param userId the user ID
     * @return list of user's bookings
     */
    List<Booking> getBookingsByUserId(Long userId);
    
    /**
     * Retrieves all bookings for a specific cruise.
     * @param cruiseId the cruise ID
     * @return list of bookings for the cruise
     */
    List<Booking> getBookingsByCruiseId(Long cruiseId);
    
    /**
     * Saves a new booking.
     * @param booking the booking to save
     * @return the saved booking
     */
    Booking saveBooking(Booking booking);
    
    /**
     * Updates an existing booking.
     * @param id the booking ID to update
     * @param booking the updated booking data
     * @return the updated booking
     */
    Booking updateBooking(Long id, Booking booking);
    
    /**
     * Cancels a booking by changing its status.
     * @param id the booking ID to cancel
     * @throws IllegalStateException if cancellation is not allowed (less than 10 days before departure)
     */
    void cancelBooking(Long id) throws IllegalStateException;
    
    /**
     * Checks if a booking can be cancelled based on the cancellation policy.
     * Customers can cancel up to 10 days before the cruise departure date.
     * @param id the booking ID to check
     * @return true if cancellation is allowed, false otherwise
     */
    boolean canCancelBooking(Long id);
    
    /**
     * Gets the number of days remaining until the cruise departure.
     * @param id the booking ID
     * @return number of days until departure, or -1 if booking not found
     */
    long getDaysUntilDeparture(Long id);
    
    /**
     * Deletes a booking permanently.
     * @param id the booking ID to delete
     */
    void deleteBooking(Long id);
}
