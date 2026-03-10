/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.service.impl;

import com.cruise.booking.entity.CruiseCabin;
import com.cruise.booking.repository.CruiseCabinRepository;
import com.cruise.booking.service.CruiseCabinService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of CruiseCabinService interface.
 * Provides business logic for managing cruise cabin availability and assignments.
 */
@Service
public class CruiseCabinServiceImpl implements CruiseCabinService {

    private final CruiseCabinRepository cruiseCabinRepository;

    public CruiseCabinServiceImpl(CruiseCabinRepository cruiseCabinRepository) {
        this.cruiseCabinRepository = cruiseCabinRepository;
    }

    @Override
    public List<CruiseCabin> getAllCruiseCabins() {
        return cruiseCabinRepository.findAll();
    }

    @Override
    public Optional<CruiseCabin> getCruiseCabinById(Long id) {
        return cruiseCabinRepository.findById(id);
    }

    @Override
    public List<CruiseCabin> getCabinsByCruiseId(Long cruiseId) {
        return cruiseCabinRepository.findByCruiseCruiseId(cruiseId);
    }

    @Override
    public CruiseCabin saveCruiseCabin(CruiseCabin cruiseCabin) {
        return cruiseCabinRepository.save(cruiseCabin);
    }

    @Override
    public CruiseCabin updateCruiseCabin(Long id, CruiseCabin cruiseCabin) {
        cruiseCabin.setCruiseCabinId(id);
        return cruiseCabinRepository.save(cruiseCabin);
    }

    @Override
    public void deleteCruiseCabin(Long id) {
        cruiseCabinRepository.deleteById(id);
    }
}
