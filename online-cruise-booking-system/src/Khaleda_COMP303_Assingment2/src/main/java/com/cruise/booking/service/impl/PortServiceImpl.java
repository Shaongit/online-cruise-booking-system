/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.service.impl;

import com.cruise.booking.entity.Port;
import com.cruise.booking.repository.PortRepository;
import com.cruise.booking.service.PortService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of PortService interface.
 * Provides business logic for managing port information and operations.
 */
@Service
public class PortServiceImpl implements PortService {

    private final PortRepository portRepository;

    public PortServiceImpl(PortRepository portRepository) {
        this.portRepository = portRepository;
    }

    @Override
    public List<Port> getAllPorts() {
        return portRepository.findAll();
    }

    @Override
    public Optional<Port> getPortById(Long id) {
        return portRepository.findById(id);
    }

    @Override
    public Optional<Port> getPortByCode(String portCode) {
        return portRepository.findByPortCode(portCode);
    }

    @Override
    public Port savePort(Port port) {
        return portRepository.save(port);
    }

    @Override
    public Port updatePort(Long id, Port port) {
        port.setPortId(id);
        return portRepository.save(port);
    }

    @Override
    public void deletePort(Long id) {
        portRepository.deleteById(id);
    }
}
