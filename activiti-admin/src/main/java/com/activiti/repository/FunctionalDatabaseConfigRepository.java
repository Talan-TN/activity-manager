package com.activiti.repository;

import com.activiti.domain.functionalMapper.FunctionalDatabaseConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface FunctionalDatabaseConfigRepository extends JpaRepository<FunctionalDatabaseConfigEntity, Long> {

    FunctionalDatabaseConfigEntity findByActive(boolean active);

}
