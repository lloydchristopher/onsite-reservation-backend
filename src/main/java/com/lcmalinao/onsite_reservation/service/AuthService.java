package com.lcmalinao.onsite_reservation.service;

import com.lcmalinao.onsite_reservation.dto.LoginRequest;
import com.lcmalinao.onsite_reservation.dto.RegisterRequest;
import com.lcmalinao.onsite_reservation.dto.UserDto;
import com.lcmalinao.onsite_reservation.exception.AuthenticationException;
import com.lcmalinao.onsite_reservation.exception.DuplicateResourceException;
import com.lcmalinao.onsite_reservation.model.Department;
import com.lcmalinao.onsite_reservation.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthService(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @Transactional
    public UserDto register(RegisterRequest registerRequest) {
        // Check if username or email already exists
        if (userService.existsByUsername(registerRequest.getUsername())) {
            throw new DuplicateResourceException("User", "username", registerRequest.getUsername());
        }

        if (userService.existsByEmail(registerRequest.getEmail())) {
            throw new DuplicateResourceException("User", "email", registerRequest.getEmail());
        }

        // Create new user
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setRole(registerRequest.getRole());
        user.setActive(true);

        // Set department
        Department department = new Department();
        department.setDepartmentId(registerRequest.getDepartmentId());
        user.setDepartment(department);

        return userService.createUser(user);
    }

    public Authentication login(LoginRequest loginRequest, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Explicitly create a session and store the authentication
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            return authentication;
        } catch (BadCredentialsException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationException("Authentication failed: " + e.getMessage());
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
    }

    public UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            throw new AuthenticationException("Not authenticated");
        }

        return userService.getUserByUsername(authentication.getName());
    }

    public Map<String, Object> getSessionDebugInfo(HttpServletRequest request) {
        Map<String, Object> debugInfo = new HashMap<>();
        HttpSession session = request.getSession(false);

        if (session != null) {
            debugInfo.put("sessionId", session.getId());
            debugInfo.put("sessionCreationTime", session.getCreationTime());
            debugInfo.put("sessionLastAccessedTime", session.getLastAccessedTime());
            debugInfo.put("sessionMaxInactiveInterval", session.getMaxInactiveInterval());

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null) {
                debugInfo.put("authName", authentication.getName());
                debugInfo.put("authType", authentication.getClass().getName());
                debugInfo.put("isAuthenticated", authentication.isAuthenticated());
                debugInfo.put("authorities", authentication.getAuthorities());
            } else {
                debugInfo.put("auth", "No authentication in context");
            }
        } else {
            debugInfo.put("session", "No active session");
        }

        return debugInfo;
    }
}
