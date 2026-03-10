/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity class representing a Cruise in the cruise booking system.
 * A cruise represents a scheduled voyage on a ship from one port to another.
 * It includes details about dates, pricing, associated ship, and available cabins.
 */
@Entity
@Table(name = "cruises")
public class Cruise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cruise_id")
    private Long cruiseId;

    @Column(name = "cruise_name", nullable = false)
    private String cruiseName;

    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "duration_days")
    private Integer durationDays;

    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice;

    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ship_id", nullable = false)
    private Ship ship;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_port_id", nullable = false)
    private Port departurePort;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_port_id", nullable = false)
    private Port arrivalPort;

    @OneToMany(mappedBy = "cruise", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CruiseCabin> cruiseCabins;

    @OneToMany(mappedBy = "cruise", fetch = FetchType.LAZY)
    private List<Booking> bookings;

    public Cruise() {}

    public Long getCruiseId() { return cruiseId; }
    public void setCruiseId(Long cruiseId) { this.cruiseId = cruiseId; }

    public String getCruiseName() { return cruiseName; }
    public void setCruiseName(String cruiseName) { this.cruiseName = cruiseName; }

    public LocalDate getDepartureDate() { return departureDate; }
    public void setDepartureDate(LocalDate departureDate) { this.departureDate = departureDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public Integer getDurationDays() { return durationDays; }
    public void setDurationDays(Integer durationDays) { this.durationDays = durationDays; }

    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Ship getShip() { return ship; }
    public void setShip(Ship ship) { this.ship = ship; }

    public Port getDeparturePort() { return departurePort; }
    public void setDeparturePort(Port departurePort) { this.departurePort = departurePort; }

    public Port getArrivalPort() { return arrivalPort; }
    public void setArrivalPort(Port arrivalPort) { this.arrivalPort = arrivalPort; }

    public List<CruiseCabin> getCruiseCabins() { return cruiseCabins; }
    public void setCruiseCabins(List<CruiseCabin> cruiseCabins) { this.cruiseCabins = cruiseCabins; }

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
}
