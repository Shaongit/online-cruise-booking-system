/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.service;

import com.cruise.booking.entity.Ship;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Ship operations.
 * Defines business logic methods for managing cruise ships.
 */
public interface ShipService {
    /**
     * Retrieves all ships.
     * @return list of all ships
     */
    List<Ship> getAllShips();
    
    /**
     * Retrieves a ship by its ID.
     * @param id the ship ID
     * @return Optional containing the ship if found
     */
    Optional<Ship> getShipById(Long id);
    
    /**
     * Saves a new ship.
     * @param ship the ship to save
     * @return the saved ship
     */
    Ship saveShip(Ship ship);
    
    /**
     * Updates an existing ship.
     * @param id the ship ID to update
     * @param ship the updated ship data
     * @return the updated ship
     */
    Ship updateShip(Long id, Ship ship);
    
    /**
     * Deletes a ship by ID.
     * @param id the ship ID to delete
     */
    void deleteShip(Long id);
}
