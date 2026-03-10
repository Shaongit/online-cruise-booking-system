/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.repository;

import com.cruise.booking.entity.CabinType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for CabinType entity.
 * Provides database access methods for cabin type operations.
 * Supports finding cabin types by their type name.
 */
@Repository
public interface CabinTypeRepository extends JpaRepository<CabinType, Long> {
    /**
     * Finds a cabin type by its type name.
     * @param typeName the cabin type name to search for (e.g., Suite, Deluxe)
     * @return Optional containing the cabin type if found
     */
    Optional<CabinType> findByTypeName(String typeName);
}
