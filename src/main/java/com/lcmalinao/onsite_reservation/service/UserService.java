package com.lcmalinao.onsite_reservation.service;

import com.lcmalinao.onsite_reservation.dto.UserDto;
import com.lcmalinao.onsite_reservation.mapper.UserMapper;
import com.lcmalinao.onsite_reservation.model.User;
import com.lcmalinao.onsite_reservation.repository.DepartmentRepository;
import com.lcmalinao.onsite_reservation.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       DepartmentRepository departmentRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDto> getAllUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        return userMapper.toDto(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public UserDto createUser(User user) {
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Validate department exists
        if (user.getDepartment() != null && user.getDepartment().getDepartmentId() != null) {
            departmentRepository.findById(user.getDepartment().getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found"));
        }

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Transactional
    public UserDto updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setRole(userDetails.getRole());
        user.setActive(userDetails.isActive());

        // Only update password if provided
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        // Update department if provided
        if (userDetails.getDepartment() != null && userDetails.getDepartment().getDepartmentId() != null) {
            user.setDepartment(departmentRepository.findById(userDetails.getDepartment().getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found")));
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}

