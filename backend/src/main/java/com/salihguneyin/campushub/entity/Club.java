package com.salihguneyin.campushub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private String leadName;
    private String contactEmail;
    private Integer memberCount;
    private boolean active;
    private LocalDate createdAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDate.now();
    }
}
