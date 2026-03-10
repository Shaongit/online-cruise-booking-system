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
 * Entity class representing a Cabin Type in the cruise booking system.
 * Cabin types define different categories of cabins available (e.g., Suite, Deluxe, Standard).
 * Includes specifications like max occupancy, size, and amenities.
 */
@Entity
@Table(name = "cabin_types")
public class CabinType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cabin_type_id")
    private Long cabinTypeId;

    @Column(name = "type_name", unique = true, nullable = false)
    private String typeName;

    private String description;

    @Column(name = "max_occupancy", nullable = false)
    private Integer maxOccupancy;

    @Column(name = "size_sqft")
    private BigDecimal sizeSqft;

    private String amenities;

    @Column(name = "bed_configuration")
    private String bedConfiguration;

    @OneToMany(mappedBy = "cabinType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CruiseCabin> cruiseCabins;

    public CabinType() {}

    public Long getCabinTypeId() { return cabinTypeId; }
    public void setCabinTypeId(Long cabinTypeId) { this.cabinTypeId = cabinTypeId; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getMaxOccupancy() { return maxOccupancy; }
    public void setMaxOccupancy(Integer maxOccupancy) { this.maxOccupancy = maxOccupancy; }

    public BigDecimal getSizeSqft() { return sizeSqft; }
    public void setSizeSqft(BigDecimal sizeSqft) { this.sizeSqft = sizeSqft; }

    public String getAmenities() { return amenities; }
    public void setAmenities(String amenities) { this.amenities = amenities; }

    public String getBedConfiguration() { return bedConfiguration; }
    public void setBedConfiguration(String bedConfiguration) { this.bedConfiguration = bedConfiguration; }

    public List<CruiseCabin> getCruiseCabins() { return cruiseCabins; }
    public void setCruiseCabins(List<CruiseCabin> cruiseCabins) { this.cruiseCabins = cruiseCabins; }
}
