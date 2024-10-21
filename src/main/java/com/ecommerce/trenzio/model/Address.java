package com.ecommerce.trenzio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name must be at least 5 characters")
    @Column(name = "street", nullable = false)
    private String street;

    @NotBlank
    @Size(min = 5, message = "Building name must be at least 5 characters")
    @Column(name = "building_name", nullable = false)
    private String buildingName;

    @NotBlank
    @Size(min = 5, message = "City name must be at least 5 characters")
    @Column(name = "city", nullable = false)
    private String city;

    @NotBlank
    @Size(min = 5, message = "State name must be at least 5 characters")
    @Column(name = "state", nullable = false)
    private String state;

    @NotBlank
    @Size(min = 2, message = "Country name must be at least 2 characters")
    @Column(name = "country", nullable = false)
    private String country;

    @NotBlank
    @Size(min = 6, message = "Pincode must be at least 6 characters")
    @Column(name = "pincode", nullable = false)
    private String pincode;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Boolean isDefault = false;  // Indicates if this is the default address
    private Boolean isActive = true;    // Indicates if the address is active or deleted


    // Prevent recursive toString with @ToString.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses",  cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<User> users = new ArrayList<>();

    // Constructor without users list, since it's managed by relationship methods
    public Address(String street, String buildingName, String city, String state, String country, String pincode) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }

    // Automatically set createdAt and updatedAt before persisting
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Automatically update the updatedAt field before any update
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
