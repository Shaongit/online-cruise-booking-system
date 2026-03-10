/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.entity;

import jakarta.persistence.*;
import java.util.List;

/**
 * Entity class representing a Port in the cruise booking system.
 * Ports serve as departure and arrival points for cruises.
 * Each port has a unique code and location information.
 */
@Entity
@Table(name = "ports")
public class Port {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "port_id")
    private Long portId;

    @Column(name = "port_name", unique = true, nullable = false)
    private String portName;

    private String city;
    private String country;

    @Column(name = "port_code", unique = true, nullable = false)
    private String portCode;

    @OneToMany(mappedBy = "departurePort", fetch = FetchType.LAZY)
    private List<Cruise> departureCruises;

    @OneToMany(mappedBy = "arrivalPort", fetch = FetchType.LAZY)
    private List<Cruise> arrivalCruises;

    public Port() {}

    public Long getPortId() { return portId; }
    public void setPortId(Long portId) { this.portId = portId; }

    public String getPortName() { return portName; }
    public void setPortName(String portName) { this.portName = portName; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getPortCode() { return portCode; }
    public void setPortCode(String portCode) { this.portCode = portCode; }

    public List<Cruise> getDepartureCruises() { return departureCruises; }
    public void setDepartureCruises(List<Cruise> departureCruises) { this.departureCruises = departureCruises; }

    public List<Cruise> getArrivalCruises() { return arrivalCruises; }
    public void setArrivalCruises(List<Cruise> arrivalCruises) { this.arrivalCruises = arrivalCruises; }
}
