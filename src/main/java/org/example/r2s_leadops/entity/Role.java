package org.example.r2s_leadops.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(unique = true, nullable = false, length = 50)
    private String name; // ROLE_ADMIN, ROLE_LEADER_MARKETING, ROLE_SALES, ROLE_MARKETING_STAFF

    @Column(length = 255)
    private String description;
}