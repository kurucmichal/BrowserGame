package com.greenfoxacademy.aureuscctribesbackend.repositories;

import com.greenfoxacademy.aureuscctribesbackend.models.Troop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TroopRepository extends JpaRepository<Troop, Long> {

}
