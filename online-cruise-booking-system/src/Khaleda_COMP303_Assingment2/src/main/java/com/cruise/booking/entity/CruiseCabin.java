/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Entity class representing a Cruise Cabin in the cruise booking system.
 * A cruise cabin represents the association between a cabin type and a specific cruise.
 * Includes cabin-specific details like cabin number, deck, view type, and availability.
 */
@Entity
@Table(name = "cruise_cabins")
public class CruiseCabin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cruise_cabin_id")
    private Long cruiseCabinId;

    @Column(name = "cabin_number", nullable = false)
    private String cabinNumber;

    private String deck;

    @Column(name = "price_multiplier")
    private BigDecimal priceMultiplier;

    @Column(name = "available_quantity")
    private Integer availableQuantity;

    @Column(name = "view_type")
    private String viewType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cruise_id", nullable = false)
    private Cruise cruise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cabin_type_id", nullable = false)
    private CabinType cabinType;

    @OneToMany(mappedBy = "cruiseCabin", fetch = FetchType.LAZY)
    private List<Booking> bookings;

    public CruiseCabin() {}

    public Long getCruiseCabinId() { return cruiseCabinId; }
    public void setCruiseCabinId(Long cruiseCabinId) { this.cruiseCabinId = cruiseCabinId; }

    public String getCabinNumber() { return cabinNumber; }
    public void setCabinNumber(String cabinNumber) { this.cabinNumber = cabinNumber; }

    public String getDeck() { return deck; }
    public void setDeck(String deck) { this.deck = deck; }

    public BigDecimal getPriceMultiplier() { return priceMultiplier; }
    public void setPriceMultiplier(BigDecimal priceMultiplier) { this.priceMultiplier = priceMultiplier; }

    public Integer getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }

    public String getViewType() { return viewType; }
    public void setViewType(String viewType) { this.viewType = viewType; }

    public Cruise getCruise() { return cruise; }
    public void setCruise(Cruise cruise) { this.cruise = cruise; }

    public CabinType getCabinType() { return cabinType; }
    public void setCabinType(CabinType cabinType) { this.cabinType = cabinType; }

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
}
