package com.salihguneyin.campushub;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CampusHubApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void dashboardEndpointReturnsSeededMetrics() throws Exception {
        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summary.length()").value(4))
                .andExpect(jsonPath("$.recentRegistrations.length()").value(3));
    }

    @Test
    void createClubAddsANewRecord() throws Exception {
        String payload = """
                {
                  "name": "Mobile Developers Club",
                  "category": "Technology",
                  "leadName": "Deniz Arslan",
                  "contactEmail": "mobile.club@campus.edu",
                  "memberCount": 28,
                  "active": true
                }
                """;

        String response = mockMvc.perform(post("/api/clubs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contactEmail").value("mobile.club@campus.edu"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).contains("Mobile Developers Club");
    }
}
