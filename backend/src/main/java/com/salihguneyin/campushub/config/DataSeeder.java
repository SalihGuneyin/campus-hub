package com.salihguneyin.campushub.config;

import com.salihguneyin.campushub.entity.Club;
import com.salihguneyin.campushub.entity.Event;
import com.salihguneyin.campushub.entity.EventFormat;
import com.salihguneyin.campushub.entity.Registration;
import com.salihguneyin.campushub.entity.RegistrationStatus;
import com.salihguneyin.campushub.repository.ClubRepository;
import com.salihguneyin.campushub.repository.EventRepository;
import com.salihguneyin.campushub.repository.RegistrationRepository;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(
            ClubRepository clubRepository,
            EventRepository eventRepository,
            RegistrationRepository registrationRepository
    ) {
        return args -> {
            if (clubRepository.count() > 0) {
                return;
            }

            Club aiClub = club("AI and Data Club", "Technology", "Selin Kaya", "ai.club@campus.edu", 64, true);
            Club designClub = club("Design Circle", "Creative", "Mert Cakir", "design.circle@campus.edu", 41, true);
            Club entrepreneurClub = club("Entrepreneurship Society", "Business", "Ece Yilmaz", "startup.society@campus.edu", 58, true);
            clubRepository.save(aiClub);
            clubRepository.save(designClub);
            clubRepository.save(entrepreneurClub);

            Event hackNight = event(aiClub, "Spring Hack Night", "Engineering Building A2", LocalDate.now().plusDays(10), 80, 36, EventFormat.ON_CAMPUS, true, "Evening workshop focused on APIs, product demos and rapid prototyping.");
            Event uxStudio = event(designClub, "UX Portfolio Review", "Design Lab", LocalDate.now().plusDays(5), 30, 18, EventFormat.HYBRID, true, "Peer review session for portfolio feedback and case study critique.");
            Event founderTalk = event(entrepreneurClub, "Founder Fireside Chat", "Conference Hall", LocalDate.now().plusDays(14), 120, 72, EventFormat.ON_CAMPUS, true, "Guest talk on early-stage product validation and campus startup building.");
            eventRepository.save(hackNight);
            eventRepository.save(uxStudio);
            eventRepository.save(founderTalk);

            registrationRepository.save(registration(hackNight, "Ayse Dogan", "ayse@campus.edu", "Computer Engineering", 2, RegistrationStatus.APPROVED, "Interested in backend track and API workshop."));
            registrationRepository.save(registration(uxStudio, "Burak Aydin", "burak@campus.edu", "Visual Communication", 3, RegistrationStatus.PENDING, "Requested portfolio feedback on mobile product case study."));
            registrationRepository.save(registration(founderTalk, "Ceren Tas", "ceren@campus.edu", "Business Administration", 4, RegistrationStatus.WAITLISTED, "Would like to join the networking session after the talk."));
        };
    }

    private Club club(
            String name,
            String category,
            String leadName,
            String contactEmail,
            int memberCount,
            boolean active
    ) {
        Club club = new Club();
        club.setName(name);
        club.setCategory(category);
        club.setLeadName(leadName);
        club.setContactEmail(contactEmail);
        club.setMemberCount(memberCount);
        club.setActive(active);
        return club;
    }

    private Event event(
            Club club,
            String title,
            String location,
            LocalDate eventDate,
            int capacity,
            int approvedCount,
            EventFormat format,
            boolean published,
            String summary
    ) {
        Event event = new Event();
        event.setClub(club);
        event.setTitle(title);
        event.setLocation(location);
        event.setEventDate(eventDate);
        event.setCapacity(capacity);
        event.setApprovedCount(approvedCount);
        event.setEventFormat(format);
        event.setPublished(published);
        event.setSummary(summary);
        return event;
    }

    private Registration registration(
            Event event,
            String attendeeName,
            String attendeeEmail,
            String department,
            int yearOfStudy,
            RegistrationStatus status,
            String notes
    ) {
        Registration registration = new Registration();
        registration.setEvent(event);
        registration.setAttendeeName(attendeeName);
        registration.setAttendeeEmail(attendeeEmail);
        registration.setDepartment(department);
        registration.setYearOfStudy(yearOfStudy);
        registration.setStatus(status);
        registration.setNotes(notes);
        return registration;
    }
}
