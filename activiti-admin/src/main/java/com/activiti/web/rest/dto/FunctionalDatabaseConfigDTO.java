package com.activiti.web.rest.dto;

import com.activiti.domain.functionalMapper.FunctionalDatabaseConfigEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
public class FunctionalDatabaseConfigDTO {

    protected Long id;
    protected String databaseUrl;
    protected String databaseSchema;
    protected  String databaseUsername;
    protected String databasePassword;
    protected  boolean active;

}
