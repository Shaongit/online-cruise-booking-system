/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.service.impl;

import com.cruise.booking.entity.Passenger;
import com.cruise.booking.repository.PassengerRepository;
import com.cruise.booking.service.PassengerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of PassengerService interface.
 * Provides business logic for managing passenger information and details.
 */
@Service
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;

    public PassengerServiceImpl(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    @Override
    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    @Override
    public Optional<Passenger> getPassengerById(Long id) {
        return passengerRepository.findById(id);
    }

    @Override
    public List<Passenger> getPassengersByBookingId(Long bookingId) {
        return passengerRepository.findByBookingBookingId(bookingId);
    }

    @Override
    public Passenger savePassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    @Override
    public Passenger updatePassenger(Long id, Passenger passenger) {
        passenger.setPassengerId(id);
        return passengerRepository.save(passenger);
    }

    @Override
    public void deletePassenger(Long id) {
        passengerRepository.deleteById(id);
    }
}
