package com.salihguneyin.campushub.service;

import com.salihguneyin.campushub.dto.DashboardResponse;
import com.salihguneyin.campushub.dto.PipelineMetricResponse;
import com.salihguneyin.campushub.dto.SummaryCardResponse;
import com.salihguneyin.campushub.entity.RegistrationStatus;
import com.salihguneyin.campushub.repository.ClubRepository;
import com.salihguneyin.campushub.repository.EventRepository;
import com.salihguneyin.campushub.repository.RegistrationRepository;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final ClubRepository clubRepository;
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final RegistrationService registrationService;

    public DashboardService(
            ClubRepository clubRepository,
            EventRepository eventRepository,
            RegistrationRepository registrationRepository,
            RegistrationService registrationService
    ) {
        this.clubRepository = clubRepository;
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.registrationService = registrationService;
    }

    public DashboardResponse getDashboard() {
        List<SummaryCardResponse> summary = List.of(
                new SummaryCardResponse("Active Clubs", clubRepository.count(), "ink"),
                new SummaryCardResponse("Published Events", eventRepository.countByPublishedTrue(), "mint"),
                new SummaryCardResponse("Pending Registrations", registrationRepository.countByStatus(RegistrationStatus.PENDING), "gold"),
                new SummaryCardResponse("Approved Attendees", registrationRepository.countByStatus(RegistrationStatus.APPROVED), "rose")
        );

        List<PipelineMetricResponse> pipeline = Arrays.stream(RegistrationStatus.values())
                .map(status -> new PipelineMetricResponse(status.name(), registrationRepository.countByStatus(status)))
                .toList();

        return new DashboardResponse(summary, pipeline, registrationService.getRecent());
    }
}
