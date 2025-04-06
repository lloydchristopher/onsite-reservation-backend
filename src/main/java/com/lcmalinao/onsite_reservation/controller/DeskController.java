package com.lcmalinao.onsite_reservation.controller;

import com.lcmalinao.onsite_reservation.dto.DeskDto;
import com.lcmalinao.onsite_reservation.dto.response.ApiResponse;
import com.lcmalinao.onsite_reservation.service.DeskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/desks")
public class DeskController {

    private final DeskService deskService;

    public DeskController(DeskService deskService) {
        this.deskService = deskService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DeskDto>>> getAllDesks() {
        return ApiResponse.ok(deskService.getAllDesks());
    }

    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse<DeskDto>> getDeskByUsername(@PathVariable String username) {
        return ApiResponse.ok(deskService.getDeskByUsername(username));
    }
}
