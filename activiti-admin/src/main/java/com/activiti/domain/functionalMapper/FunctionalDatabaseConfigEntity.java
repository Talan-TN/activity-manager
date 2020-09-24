package com.activiti.domain.functionalMapper;

import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;



@Entity
@Data
@Table(name = "functional_database_config")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FunctionalDatabaseConfigEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name="database_url")
    @NotNull
    protected String databaseUrl;

    @Column(name="database_schema")
    protected String databaseSchema;

    @Column(name="database_username")
    protected  String databaseUsername;


    @Column(name="database_password")
    protected String databasePassword;

    @Column(name="active")
    protected  boolean active;
}


