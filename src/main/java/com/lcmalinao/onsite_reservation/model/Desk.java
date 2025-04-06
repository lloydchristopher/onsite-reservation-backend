package com.lcmalinao.onsite_reservation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "desks")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Desk {
    @Id
    private String deskId;

    @Column(name = "desk_section")
    private String section;

    @Column(name = "date_from")
    private LocalDateTime from;

    @Column(name = "date_to")
    private LocalDateTime to;

    @Column(name = "occupied", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isOccupied;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
