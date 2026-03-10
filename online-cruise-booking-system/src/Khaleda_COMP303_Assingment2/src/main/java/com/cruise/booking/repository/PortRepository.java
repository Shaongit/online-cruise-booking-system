/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.repository;

import com.cruise.booking.entity.Port;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Port entity.
 * Provides database access methods for port-related operations.
 * Supports finding ports by code and name.
 */
@Repository
public interface PortRepository extends JpaRepository<Port, Long> {
    /**
     * Finds a port by its unique port code.
     * @param portCode the port code to search for
     * @return Optional containing the port if found
     */
    Optional<Port> findByPortCode(String portCode);
    
    /**
     * Finds a port by its name.
     * @param portName the port name to search for
     * @return Optional containing the port if found
     */
    Optional<Port> findByPortName(String portName);
}
