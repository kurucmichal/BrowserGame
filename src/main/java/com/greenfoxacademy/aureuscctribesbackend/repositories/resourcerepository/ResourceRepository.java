package com.greenfoxacademy.aureuscctribesbackend.repositories.resourcerepository;

import com.greenfoxacademy.aureuscctribesbackend.models.resource.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ResourceRepository extends JpaRepository<Resource, Long> {

}
