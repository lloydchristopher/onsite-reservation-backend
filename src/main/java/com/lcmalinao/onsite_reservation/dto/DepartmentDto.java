package com.lcmalinao.onsite_reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {
    private Long departmentId;
    private String departmentName;
    private List<UserDto> users;
}
