package com.salihguneyin.campushub.controller;

import com.salihguneyin.campushub.dto.RegistrationRequest;
import com.salihguneyin.campushub.dto.RegistrationResponse;
import com.salihguneyin.campushub.service.RegistrationService;
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
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public List<RegistrationResponse> getAll() {
        return registrationService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponse create(@Valid @RequestBody RegistrationRequest request) {
        return registrationService.create(request);
    }
}
