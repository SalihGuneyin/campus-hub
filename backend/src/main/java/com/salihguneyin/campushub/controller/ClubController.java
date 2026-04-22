package com.salihguneyin.campushub.controller;

import com.salihguneyin.campushub.dto.ClubRequest;
import com.salihguneyin.campushub.dto.ClubResponse;
import com.salihguneyin.campushub.service.ClubService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clubs")
public class ClubController {

    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @GetMapping
    public List<ClubResponse> getAll() {
        return clubService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClubResponse create(@Valid @RequestBody ClubRequest request) {
        return clubService.create(request);
    }
}
