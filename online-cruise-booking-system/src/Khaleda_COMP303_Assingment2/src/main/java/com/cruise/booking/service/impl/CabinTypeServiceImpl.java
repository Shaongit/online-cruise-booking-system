/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.service.impl;

import com.cruise.booking.entity.CabinType;
import com.cruise.booking.repository.CabinTypeRepository;
import com.cruise.booking.service.CabinTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of CabinTypeService interface.
 * Provides business logic for managing cabin type definitions and specifications.
 */
@Service
public class CabinTypeServiceImpl implements CabinTypeService {

    private final CabinTypeRepository cabinTypeRepository;

    public CabinTypeServiceImpl(CabinTypeRepository cabinTypeRepository) {
        this.cabinTypeRepository = cabinTypeRepository;
    }

    @Override
    public List<CabinType> getAllCabinTypes() {
        return cabinTypeRepository.findAll();
    }

    @Override
    public Optional<CabinType> getCabinTypeById(Long id) {
        return cabinTypeRepository.findById(id);
    }

    @Override
    public CabinType saveCabinType(CabinType cabinType) {
        return cabinTypeRepository.save(cabinType);
    }

    @Override
    public CabinType updateCabinType(Long id, CabinType cabinType) {
        cabinType.setCabinTypeId(id);
        return cabinTypeRepository.save(cabinType);
    }

    @Override
    public void deleteCabinType(Long id) {
        cabinTypeRepository.deleteById(id);
    }
}
