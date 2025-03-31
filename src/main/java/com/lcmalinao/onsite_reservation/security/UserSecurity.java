package com.lcmalinao.onsite_reservation.security;

import com.lcmalinao.onsite_reservation.model.User;
import com.lcmalinao.onsite_reservation.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {

    private final UserRepository userRepository;

    public UserSecurity(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isCurrentUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String currentUsername = authentication.getName();
        User user = userRepository.findById(userId).orElse(null);

        return user != null && user.getUsername().equals(currentUsername);
    }
}
