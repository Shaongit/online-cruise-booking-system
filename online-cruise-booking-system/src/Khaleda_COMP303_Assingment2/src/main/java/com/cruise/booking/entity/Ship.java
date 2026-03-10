/*
 * Name: Khaleda Islam
 * ID: 301504989
 * Submission Date: March 10, 2026
 */
package com.cruise.booking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity class representing a Ship in the cruise booking system.
 * Ships are vessels that can be assigned to cruises.
 * Contains information about ship specifications, capacity, and operator.
 */
@Entity
@Table(name = "ships")
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ship_id")
    private Long shipId;

    @Column(name = "ship_name", unique = true, nullable = false)
    private String shipName;

    @Column(name = "ship_type")
    private String shipType;

    private Integer capacity;

    @Column(name = "year_built")
    private Integer yearBuilt;

    private String operator;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "ship", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cruise> cruises;

    public Ship() {}

    public Long getShipId() { return shipId; }
    public void setShipId(Long shipId) { this.shipId = shipId; }

    public String getShipName() { return shipName; }
    public void setShipName(String shipName) { this.shipName = shipName; }

    public String getShipType() { return shipType; }
    public void setShipType(String shipType) { this.shipType = shipType; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Integer getYearBuilt() { return yearBuilt; }
    public void setYearBuilt(Integer yearBuilt) { this.yearBuilt = yearBuilt; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<Cruise> getCruises() { return cruises; }
    public void setCruises(List<Cruise> cruises) { this.cruises = cruises; }
}
