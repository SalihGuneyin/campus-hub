package com.salihguneyin.campushub.dto;

import java.util.List;

public record DashboardResponse(
        List<SummaryCardResponse> summary,
        List<PipelineMetricResponse> pipeline,
        List<RegistrationResponse> recentRegistrations
) {
}

