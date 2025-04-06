package com.lcmalinao.onsite_reservation.service;

import com.lcmalinao.onsite_reservation.dto.DeskDto;
import com.lcmalinao.onsite_reservation.exception.ResourceNotFoundException;
import com.lcmalinao.onsite_reservation.mapper.DeskMapper;
import com.lcmalinao.onsite_reservation.model.User;
import com.lcmalinao.onsite_reservation.repository.DeskRepository;
import com.lcmalinao.onsite_reservation.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeskService {

    private final DeskRepository deskRepository;
    private final UserRepository userRepository;
    private final DeskMapper deskMapper;

    public DeskService(DeskRepository deskRepository, UserRepository userRepository, DeskMapper deskMapper) {
        this.deskRepository = deskRepository;
        this.userRepository = userRepository;
        this.deskMapper = deskMapper;
    }

    public List<DeskDto> getAllDesks() {
        return deskMapper.toDtoList(deskRepository.findAll());
    }

    public DeskDto getDeskByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        return deskMapper.toDto(deskRepository.findByUser(user));
    }
}
