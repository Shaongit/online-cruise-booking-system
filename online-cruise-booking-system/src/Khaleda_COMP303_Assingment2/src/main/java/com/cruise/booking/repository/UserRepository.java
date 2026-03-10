/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.repository;

import com.cruise.booking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity.
 * Provides database access methods for user-related operations.
 * Extends JpaRepository to inherit basic CRUD operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by their email address.
     * @param email the email to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Checks if a user exists with the given email.
     * @param email the email to check
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);
}
