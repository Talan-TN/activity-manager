package com.activiti.domain.functionalMapper;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "FUNCTIONAL_ENTITY_MAPPING")
public class FunctionalEntityMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "process_definition_key")
    @NotNull
    private String processDefinitionKey;

    @Column(name = "sql_query")
    @NotNull
    private String sqlQuery;

}
