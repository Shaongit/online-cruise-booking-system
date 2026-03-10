/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.service.impl;

import com.cruise.booking.entity.Ship;
import com.cruise.booking.repository.ShipRepository;
import com.cruise.booking.service.ShipService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of ShipService interface.
 * Provides business logic for managing ship information and operations.
 */
@Service
public class ShipServiceImpl implements ShipService {

    private final ShipRepository shipRepository;

    public ShipServiceImpl(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    public List<Ship> getAllShips() {
        return shipRepository.findAll();
    }

    @Override
    public Optional<Ship> getShipById(Long id) {
        return shipRepository.findById(id);
    }

    @Override
    public Ship saveShip(Ship ship) {
        return shipRepository.save(ship);
    }

    @Override
    public Ship updateShip(Long id, Ship ship) {
        ship.setShipId(id);
        return shipRepository.save(ship);
    }

    @Override
    public void deleteShip(Long id) {
        shipRepository.deleteById(id);
    }
}
