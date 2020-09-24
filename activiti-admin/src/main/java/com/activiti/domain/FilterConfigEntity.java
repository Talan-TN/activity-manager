package com.activiti.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Data
@Table(name = "filter_config")
public class FilterConfigEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "filter_name")
    private String filterName;

    @Column(name = "pid_column_name")
    private String processIdColumnName;

    @Column(name = "process_definition_key")
    @NotNull
    private String processDefinitionKey;


    @Column(name = "sql_query")
    @NotNull
    private String sqlQuery;
}
