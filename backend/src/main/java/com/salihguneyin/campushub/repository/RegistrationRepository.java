package com.salihguneyin.campushub.repository;

import com.salihguneyin.campushub.entity.Registration;
import com.salihguneyin.campushub.entity.RegistrationStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    long countByStatus(RegistrationStatus status);

    List<Registration> findTop5ByOrderByCreatedAtDesc();
}
