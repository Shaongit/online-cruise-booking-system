/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.service;

import com.cruise.booking.entity.Cruise;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Cruise operations.
 * Defines business logic methods for managing cruise schedules and availability.
 */
public interface CruiseService {
    /**
     * Retrieves all cruises.
     * @return list of all cruises
     */
    List<Cruise> getAllCruises();
    
    /**
     * Retrieves a cruise by its ID.
     * @param id the cruise ID
     * @return Optional containing the cruise if found
     */
    Optional<Cruise> getCruiseById(Long id);
    
    /**
     * Retrieves cruises by their status.
     * @param status the cruise status to filter by
     * @return list of cruises with the specified status
     */
    List<Cruise> getCruisesByStatus(String status);
    
    /**
     * Retrieves all available cruises (future departures, active status).
     * @return list of available cruises
     */
    List<Cruise> getAvailableCruises();
    
    /**
     * Saves a new cruise.
     * @param cruise the cruise to save
     * @return the saved cruise
     */
    Cruise saveCruise(Cruise cruise);
    
    /**
     * Updates an existing cruise.
     * @param id the cruise ID to update
     * @param cruise the updated cruise data
     * @return the updated cruise
     */
    Cruise updateCruise(Long id, Cruise cruise);
    
    /**
     * Deletes a cruise by ID.
     * @param id the cruise ID to delete
     */
    void deleteCruise(Long id);
}
