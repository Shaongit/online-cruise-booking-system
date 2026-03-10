/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.repository;

import com.cruise.booking.entity.CruiseCabin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for CruiseCabin entity.
 * Provides database access methods for cruise cabin operations.
 * Supports finding cabins by cruise ID and cabin type.
 */
@Repository
public interface CruiseCabinRepository extends JpaRepository<CruiseCabin, Long> {
    /**
     * Finds all cruise cabins for a specific cruise.
     * @param cruiseId the cruise ID to search for
     * @return list of cruise cabins
     */
    List<CruiseCabin> findByCruiseCruiseId(Long cruiseId);
    
    /**
     * Finds all cruise cabins of a specific cabin type.
     * @param cabinTypeId the cabin type ID to search for
     * @return list of cruise cabins
     */
    List<CruiseCabin> findByCabinTypeCabinTypeId(Long cabinTypeId);
}
