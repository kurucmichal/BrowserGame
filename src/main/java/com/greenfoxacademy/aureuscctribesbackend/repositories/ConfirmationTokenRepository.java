package com.greenfoxacademy.aureuscctribesbackend.repositories;

import com.greenfoxacademy.aureuscctribesbackend.models.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

  ConfirmationToken findByToken(String token);
}

