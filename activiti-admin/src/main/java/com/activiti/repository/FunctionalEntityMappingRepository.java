package com.activiti.repository;

import com.activiti.domain.functionalMapper.FunctionalEntityMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;



public interface FunctionalEntityMappingRepository extends JpaRepository<FunctionalEntityMapping, Long> {

    Optional<FunctionalEntityMapping> findByProcessDefinitionKey(String processDefinitionKey);
}
