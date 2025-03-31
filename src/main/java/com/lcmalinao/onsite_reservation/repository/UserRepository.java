package com.lcmalinao.onsite_reservation.repository;

import com.lcmalinao.onsite_reservation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String name);
    boolean existsByEmail(String email);
}
