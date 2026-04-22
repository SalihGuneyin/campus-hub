package com.salihguneyin.campushub.service;

import com.salihguneyin.campushub.dto.ClubRequest;
import com.salihguneyin.campushub.dto.ClubResponse;
import com.salihguneyin.campushub.entity.Club;
import com.salihguneyin.campushub.repository.ClubRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ClubService {

    private final ClubRepository clubRepository;

    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    public List<ClubResponse> getAll() {
        return clubRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ClubResponse create(ClubRequest request) {
        Club club = new Club();
        club.setName(request.name());
        club.setCategory(request.category());
        club.setLeadName(request.leadName());
        club.setContactEmail(request.contactEmail());
        club.setMemberCount(request.memberCount());
        club.setActive(request.active());
        return toResponse(clubRepository.save(club));
    }

    private ClubResponse toResponse(Club club) {
        return new ClubResponse(
                club.getId(),
                club.getName(),
                club.getCategory(),
                club.getLeadName(),
                club.getContactEmail(),
                club.getMemberCount(),
                club.isActive(),
                club.getCreatedAt()
        );
    }
}
