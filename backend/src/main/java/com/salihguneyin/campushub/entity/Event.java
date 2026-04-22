package com.salihguneyin.campushub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "club_id")
    private Club club;

    private String title;
    private String location;
    private LocalDate eventDate;
    private Integer capacity;
    private Integer approvedCount;

    @Enumerated(EnumType.STRING)
    private EventFormat eventFormat;

    private boolean published;
    private String summary;
    private LocalDate createdAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDate.now();
        if (approvedCount == null) {
            approvedCount = 0;
        }
    }
}
