package com.lcmalinao.onsite_reservation.service;

import com.lcmalinao.onsite_reservation.dto.DepartmentDto;
import com.lcmalinao.onsite_reservation.exception.ResourceNotFoundException;
import com.lcmalinao.onsite_reservation.mapper.DepartmentMapper;
import com.lcmalinao.onsite_reservation.model.Department;
import com.lcmalinao.onsite_reservation.repository.DepartmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    public List<DepartmentDto> getAllDepartments() {
        return departmentMapper.toDtoList(departmentRepository.findAll());
    }

    public DepartmentDto getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        return departmentMapper.toDto(department);
    }

    @Transactional
    public DepartmentDto createDepartment(Department department) {
        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toDto(savedDepartment);
    }

    @Transactional
    public DepartmentDto updateDepartment(Long id, Department departmentDetails) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        department.setDepartmentName(departmentDetails.getDepartmentName());

        Department updatedDepartment = departmentRepository.save(department);
        return departmentMapper.toDto(updatedDepartment);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department", "id", id);
        }
        departmentRepository.deleteById(id);
    }
}
