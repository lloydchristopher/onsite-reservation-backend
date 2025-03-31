package com.lcmalinao.onsite_reservation.controller;

import com.lcmalinao.onsite_reservation.dto.LoginRequest;
import com.lcmalinao.onsite_reservation.dto.RegisterRequest;
import com.lcmalinao.onsite_reservation.dto.UserDto;
import com.lcmalinao.onsite_reservation.model.Department;
import com.lcmalinao.onsite_reservation.model.User;
import com.lcmalinao.onsite_reservation.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Check if username or email already exists
            if (userService.existsByUsername(registerRequest.getUsername())) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Username is already taken");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            if (userService.existsByEmail(registerRequest.getEmail())) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Email is already in use");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            // Create new user
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(registerRequest.getPassword()); // Service will encode this
            user.setRole(registerRequest.getRole());
            user.setActive(true);

            // Set department
            Department department = new Department();
            department.setDepartmentId(registerRequest.getDepartmentId());
            user.setDepartment(department);

            UserDto createdUser = userService.createUser(user);

            Map<String, Object> response = new HashMap<>();
            response.put("user", createdUser);
            response.put("message", "User registered successfully");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Return user details
            UserDto userDto = userService.getUserByUsername(loginRequest.getUsername());

            Map<String, Object> response = new HashMap<>();
            response.put("user", userDto);
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "An error occurred during authentication: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Logout successful");
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserByUsername(authentication.getName()));
    }
}

