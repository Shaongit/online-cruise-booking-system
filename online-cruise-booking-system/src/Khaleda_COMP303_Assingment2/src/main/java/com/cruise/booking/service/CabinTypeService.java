/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.service;

import com.cruise.booking.entity.CabinType;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for CabinType operations.
 * Defines business logic methods for managing cabin types (e.g., Suite, Deluxe, Standard).
 */
public interface CabinTypeService {
    /**
     * Retrieves all cabin types.
     * @return list of all cabin types
     */
    List<CabinType> getAllCabinTypes();
    
    /**
     * Retrieves a cabin type by its ID.
     * @param id the cabin type ID
     * @return Optional containing the cabin type if found
     */
    Optional<CabinType> getCabinTypeById(Long id);
    
    /**
     * Saves a new cabin type.
     * @param cabinType the cabin type to save
     * @return the saved cabin type
     */
    CabinType saveCabinType(CabinType cabinType);
    
    /**
     * Updates an existing cabin type.
     * @param id the cabin type ID to update
     * @param cabinType the updated cabin type data
     * @return the updated cabin type
     */
    CabinType updateCabinType(Long id, CabinType cabinType);
    
    /**
     * Deletes a cabin type by ID.
     * @param id the cabin type ID to delete
     */
    void deleteCabinType(Long id);
}
