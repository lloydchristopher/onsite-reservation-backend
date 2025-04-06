package com.lcmalinao.onsite_reservation.service;

import com.lcmalinao.onsite_reservation.dto.UserDto;
import com.lcmalinao.onsite_reservation.exception.ResourceNotFoundException;
import com.lcmalinao.onsite_reservation.mapper.UserMapper;
import com.lcmalinao.onsite_reservation.model.User;
import com.lcmalinao.onsite_reservation.repository.DepartmentRepository;
import com.lcmalinao.onsite_reservation.repository.DeskRepository;
import com.lcmalinao.onsite_reservation.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final DeskRepository deskRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       DepartmentRepository departmentRepository, DeskRepository deskRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.deskRepository = deskRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDto> getAllUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return userMapper.toDto(user);
    }

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
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
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", user.getDepartment().getDepartmentId()));
        }

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Transactional
    public UserDto updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

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
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", userDetails.getDepartment().getDepartmentId())));
        }

        // Update desk if provided
        if (userDetails.getDesk() != null && userDetails.getDesk().getDeskId() != null) {
            user.setDesk(deskRepository.findById(userDetails.getDesk().getDeskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Desk", "id", userDetails.getDesk().getDeskId())));
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        userRepository.deleteById(id);
    }
}

