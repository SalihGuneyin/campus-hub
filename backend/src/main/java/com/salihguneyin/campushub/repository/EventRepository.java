package com.salihguneyin.campushub.repository;

import com.salihguneyin.campushub.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

    long countByPublishedTrue();
}
