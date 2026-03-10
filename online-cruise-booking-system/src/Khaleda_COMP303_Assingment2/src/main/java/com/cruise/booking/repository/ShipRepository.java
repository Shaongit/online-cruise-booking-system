/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.repository;

import com.cruise.booking.entity.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Ship entity.
 * Provides database access methods for ship-related operations.
 * Supports finding ships by name.
 */
@Repository
public interface ShipRepository extends JpaRepository<Ship, Long> {
    /**
     * Finds a ship by its name.
     * @param shipName the ship name to search for
     * @return Optional containing the ship if found
     */
    Optional<Ship> findByShipName(String shipName);
}
