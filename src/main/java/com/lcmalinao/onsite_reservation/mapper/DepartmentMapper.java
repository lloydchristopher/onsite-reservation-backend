package com.lcmalinao.onsite_reservation.mapper;

import com.lcmalinao.onsite_reservation.dto.DepartmentDto;
import com.lcmalinao.onsite_reservation.model.Department;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DepartmentMapper {

    private final UserMapper userMapper;

    public DepartmentMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public DepartmentDto toDto(Department department) {
        if (department == null) {
            return null;
        }

        DepartmentDto dto = new DepartmentDto();
        dto.setDepartmentId(department.getDepartmentId());
        dto.setDepartmentName(department.getDepartmentName());

        if (department.getUsers() != null) {
            dto.setUsers(userMapper.toDtoList(department.getUsers()));
        }

        return dto;
    }

    public List<DepartmentDto> toDtoList(List<Department> departments) {
        if (departments == null) {
            return null;
        }

        return departments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
