/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.service;

import com.cruise.booking.entity.CruiseCabin;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for CruiseCabin operations.
 * Defines business logic methods for managing cabins available on specific cruises.
 */
public interface CruiseCabinService {
    /**
     * Retrieves all cruise cabins.
     * @return list of all cruise cabins
     */
    List<CruiseCabin> getAllCruiseCabins();
    
    /**
     * Retrieves a cruise cabin by its ID.
     * @param id the cruise cabin ID
     * @return Optional containing the cruise cabin if found
     */
    Optional<CruiseCabin> getCruiseCabinById(Long id);
    
    /**
     * Retrieves all cabins available for a specific cruise.
     * @param cruiseId the cruise ID
     * @return list of cruise cabins
     */
    List<CruiseCabin> getCabinsByCruiseId(Long cruiseId);
    
    /**
     * Saves a new cruise cabin.
     * @param cruiseCabin the cruise cabin to save
     * @return the saved cruise cabin
     */
    CruiseCabin saveCruiseCabin(CruiseCabin cruiseCabin);
    
    /**
     * Updates an existing cruise cabin.
     * @param id the cruise cabin ID to update
     * @param cruiseCabin the updated cruise cabin data
     * @return the updated cruise cabin
     */
    CruiseCabin updateCruiseCabin(Long id, CruiseCabin cruiseCabin);
    
    /**
     * Deletes a cruise cabin by ID.
     * @param id the cruise cabin ID to delete
     */
    void deleteCruiseCabin(Long id);
}
