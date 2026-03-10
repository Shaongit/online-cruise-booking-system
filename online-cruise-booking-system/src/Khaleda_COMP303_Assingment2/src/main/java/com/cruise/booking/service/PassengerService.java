/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.service;

import com.cruise.booking.entity.Passenger;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Passenger operations.
 * Defines business logic methods for managing passenger information.
 */
public interface PassengerService {
    /**
     * Retrieves all passengers.
     * @return list of all passengers
     */
    List<Passenger> getAllPassengers();
    
    /**
     * Retrieves a passenger by their ID.
     * @param id the passenger ID
     * @return Optional containing the passenger if found
     */
    Optional<Passenger> getPassengerById(Long id);
    
    /**
     * Retrieves all passengers associated with a booking.
     * @param bookingId the booking ID
     * @return list of passengers
     */
    List<Passenger> getPassengersByBookingId(Long bookingId);
    
    /**
     * Saves a new passenger.
     * @param passenger the passenger to save
     * @return the saved passenger
     */
    Passenger savePassenger(Passenger passenger);
    
    /**
     * Updates an existing passenger.
     * @param id the passenger ID to update
     * @param passenger the updated passenger data
     * @return the updated passenger
     */
    Passenger updatePassenger(Long id, Passenger passenger);
    
    /**
     * Deletes a passenger by ID.
     * @param id the passenger ID to delete
     */
    void deletePassenger(Long id);
}
