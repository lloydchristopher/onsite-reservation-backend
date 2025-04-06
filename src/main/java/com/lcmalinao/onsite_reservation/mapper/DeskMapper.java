package com.lcmalinao.onsite_reservation.mapper;

import com.lcmalinao.onsite_reservation.dto.DeskDto;
import com.lcmalinao.onsite_reservation.model.Desk;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DeskMapper {

    public DeskDto toDto(Desk desk) {
        if (desk == null) {
            return null;
        }

        DeskDto dto = new DeskDto();
        dto.setDeskId(desk.getDeskId());
        dto.setSection(desk.getSection());
        dto.setFrom(desk.getFrom());
        dto.setTo(desk.getTo());
        dto.setOccupied(desk.isOccupied());

        if (desk.getUser() != null) {
            dto.setUsername(desk.getUser().getUsername());
        }

        return dto;
    }

    public List<DeskDto> toDtoList(List<Desk> desks) {
        if (desks == null) {
            return null;
        }

        return desks.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
