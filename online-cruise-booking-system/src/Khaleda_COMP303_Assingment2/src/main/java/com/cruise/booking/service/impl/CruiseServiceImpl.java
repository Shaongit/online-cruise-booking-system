/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.service.impl;

import com.cruise.booking.entity.Cruise;
import com.cruise.booking.repository.CruiseRepository;
import com.cruise.booking.service.CruiseService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of CruiseService interface.
 * Provides business logic for managing cruise schedules and availability.
 * Includes methods to filter cruises by status and availability dates.
 */
@Service
public class CruiseServiceImpl implements CruiseService {

    private final CruiseRepository cruiseRepository;

    public CruiseServiceImpl(CruiseRepository cruiseRepository) {
        this.cruiseRepository = cruiseRepository;
    }

    @Override
    public List<Cruise> getAllCruises() {
        return cruiseRepository.findAll();
    }

    @Override
    public Optional<Cruise> getCruiseById(Long id) {
        return cruiseRepository.findById(id);
    }

    @Override
    public List<Cruise> getCruisesByStatus(String status) {
        return cruiseRepository.findByStatus(status);
    }

    @Override
    public List<Cruise> getAvailableCruises() {
        return cruiseRepository.findByDepartureDateAfter(LocalDate.now());
    }

    @Override
    public Cruise saveCruise(Cruise cruise) {
        return cruiseRepository.save(cruise);
    }

    @Override
    public Cruise updateCruise(Long id, Cruise cruise) {
        cruise.setCruiseId(id);
        return cruiseRepository.save(cruise);
    }

    @Override
    public void deleteCruise(Long id) {
        cruiseRepository.deleteById(id);
    }
}
