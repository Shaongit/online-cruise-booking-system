/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.repository;

import com.cruise.booking.entity.Cruise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Cruise entity.
 * Provides database access methods for cruise-related operations.
 * Supports queries by status, departure date, and ship.
 */
@Repository
public interface CruiseRepository extends JpaRepository<Cruise, Long> {
    /**
     * Finds all cruises with a specific status.
     * @param status the cruise status (e.g., SCHEDULED, COMPLETED, CANCELLED)
     * @return list of cruises
     */
    List<Cruise> findByStatus(String status);
    
    /**
     * Finds all cruises departing after a specific date.
     * @param date the date to search from
     * @return list of upcoming cruises
     */
    List<Cruise> findByDepartureDateAfter(LocalDate date);
    
    /**
     * Finds all cruises assigned to a specific ship.
     * @param shipId the ship ID to search for
     * @return list of cruises
     */
    List<Cruise> findByShipShipId(Long shipId);
}
