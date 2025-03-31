package com.lcmalinao.onsite_reservation.mapper;

import com.lcmalinao.onsite_reservation.dto.UserDto;
import com.lcmalinao.onsite_reservation.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());

        if (user.getDepartment() != null) {
            dto.setDepartmentId(user.getDepartment().getDepartmentId());
            dto.setDepartmentName(user.getDepartment().getDepartmentName());
        }

        return dto;
    }

    public List<UserDto> toDtoList(List<User> users) {
        if (users == null) {
            return null;
        }

        return users.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
