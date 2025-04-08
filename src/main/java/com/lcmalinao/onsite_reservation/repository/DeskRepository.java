package com.lcmalinao.onsite_reservation.repository;

import com.lcmalinao.onsite_reservation.model.Desk;
import com.lcmalinao.onsite_reservation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeskRepository extends JpaRepository<Desk, String> {
    Desk findByUser(User user);
    List<Desk> findBySectionContaining(String section);
    boolean existsByDeskId(String deskId);
}
