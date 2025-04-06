package com.lcmalinao.onsite_reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeskDto {
    private String deskId;
    private String section;
    private LocalDateTime from;
    private LocalDateTime to;
    private boolean isOccupied;
    private String username;
}
