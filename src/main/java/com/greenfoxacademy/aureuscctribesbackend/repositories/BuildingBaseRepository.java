package com.greenfoxacademy.aureuscctribesbackend.repositories;

import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BuildingBaseRepository<T> extends JpaRepository<T, Long> {

  Optional<List<T>> findAllByKingdom(Kingdom kingdom);

  @Query(value = "SELECT b FROM #{#entityName} b")
  Optional<List<T>> findAllByCategory();
}
