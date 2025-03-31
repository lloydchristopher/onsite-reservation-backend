package com.lcmalinao.onsite_reservation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String role;
    private String password;

    @Column(name = "active")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

}
