package com.activiti.repository;

import com.activiti.domain.FilterConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FilterConfigRepository extends JpaRepository<FilterConfigEntity, Long> {

    Optional<FilterConfigEntity> findByProcessDefinitionKey(String processDefinitionKey);
}
