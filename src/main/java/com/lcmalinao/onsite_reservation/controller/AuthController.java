package com.lcmalinao.onsite_reservation.controller;

import com.lcmalinao.onsite_reservation.dto.LoginRequest;
import com.lcmalinao.onsite_reservation.dto.RegisterRequest;
import com.lcmalinao.onsite_reservation.dto.UserDto;
import com.lcmalinao.onsite_reservation.dto.response.ApiResponse;
import com.lcmalinao.onsite_reservation.service.AuthService;
import com.lcmalinao.onsite_reservation.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        UserDto createdUser = authService.register(registerRequest);
        return ApiResponse.created("User registered successfully", createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserDto>> login(@Valid @RequestBody LoginRequest loginRequest,
                                   HttpServletRequest request) {
        Authentication authentication = authService.login(loginRequest, request);

        UserDto userDto = userService.getUserByUsername(authentication.getName());
        return ApiResponse.ok("Login successful", userDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ApiResponse.ok("Logout successful", null);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser(HttpServletRequest request) {
        UserDto userDto = authService.getCurrentUser();
        return ApiResponse.ok(userDto);
    }

    @GetMapping("/session-debug")
    public ResponseEntity<ApiResponse<Map<String, Object>>> debugSession(HttpServletRequest request) {
        Map<String, Object> debugInfo = authService.getSessionDebugInfo(request);
        return ApiResponse.ok(debugInfo);
    }
}

