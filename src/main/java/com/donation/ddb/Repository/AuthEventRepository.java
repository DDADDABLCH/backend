package com.donation.ddb.Repository;

import com.donation.ddb.Domain.AuthEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthEventRepository extends JpaRepository<AuthEvent,Long> {
    //Optional<AuthEvent> findByUser(Long id);
}
