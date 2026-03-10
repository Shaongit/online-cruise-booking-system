/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.service;

import com.cruise.booking.entity.Port;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Port operations.
 * Defines business logic methods for managing cruise ports.
 */
public interface PortService {
    /**
     * Retrieves all ports.
     * @return list of all ports
     */
    List<Port> getAllPorts();
    
    /**
     * Retrieves a port by its ID.
     * @param id the port ID
     * @return Optional containing the port if found
     */
    Optional<Port> getPortById(Long id);
    
    /**
     * Retrieves a port by its unique code.
     * @param portCode the port code
     * @return Optional containing the port if found
     */
    Optional<Port> getPortByCode(String portCode);
    
    /**
     * Saves a new port.
     * @param port the port to save
     * @return the saved port
     */
    Port savePort(Port port);
    
    /**
     * Updates an existing port.
     * @param id the port ID to update
     * @param port the updated port data
     * @return the updated port
     */
    Port updatePort(Long id, Port port);
    
    /**
     * Deletes a port by ID.
     * @param id the port ID to delete
     */
    void deletePort(Long id);
}
