/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.service;

import com.cruise.booking.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for User operations.
 * Defines business logic methods for managing users in the cruise booking system.
 */
public interface UserService {
    /**
     * Retrieves all users in the system.
     * @return list of all users
     */
    List<User> getAllUsers();
    
    /**
     * Retrieves a user by their ID.
     * @param id the user ID
     * @return Optional containing the user if found
     */
    Optional<User> getUserById(Long id);
    
    /**
     * Retrieves a user by their email address.
     * @param email the user's email
     * @return Optional containing the user if found
     */
    Optional<User> getUserByEmail(String email);
    
    /**
     * Saves a new user.
     * @param user the user to save
     * @return the saved user
     */
    User saveUser(User user);
    
    /**
     * Updates an existing user.
     * @param id the user ID to update
     * @param user the updated user data
     * @return the updated user
     */
    User updateUser(Long id, User user);
    
    /**
     * Deletes a user by ID.
     * @param id the user ID to delete
     */
    void deleteUser(Long id);
    
    /**
     * Checks if a user exists with the given email.
     * @param email the email to check
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);
}
