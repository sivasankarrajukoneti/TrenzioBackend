package com.ecommerce.trenzio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;  // Changed to Long for consistency and scalability

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", length = 20, nullable = false, unique = true)  // Added nullable = false for constraints
    private AppRole roleName;

    // Constructor for creating Role with roleName
    public Role(AppRole roleName) {
        this.roleName = roleName;
    }



}
