package com.activiti.helper;

import com.activiti.domain.functionalMapper.FunctionalDatabaseConfigEntity;
import com.activiti.domain.events.DatabaseConfigEvent;
import com.activiti.repository.FunctionalDatabaseConfigRepository;
import com.activiti.service.activiti.EncryptingService;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.mchange.v2.c3p0.DriverManagerDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;



@Slf4j
@Service
public class JdbcTemplateHolder {

    @Autowired
    private FunctionalDatabaseConfigRepository functionalDatabaseConfigRepository;

    @Autowired
    private EncryptingService encryptingService;

    @Autowired
    private EventBus eventBus;

    private JdbcTemplate jdbcTemplate;

    private final Object reloadLock = new Object();

    @PostConstruct
    public void init() {
        this.eventBus.register(this);
    }

    @Subscribe
    public void handleDatabaseChangeEvent(DatabaseConfigEvent databaseConfigEvent) {
        log.info("Database Configuration changed!");
        this.reloadJdbcTemplate(databaseConfigEvent.getFunctionalDatabaseConfigEntity());
    }

    public JdbcTemplate getJdbcTemplate() {
        if (this.jdbcTemplate == null) {
            List<FunctionalDatabaseConfigEntity> functionalDatabaseConfigEntities = this.functionalDatabaseConfigRepository.findAll();
            if(!functionalDatabaseConfigEntities.isEmpty()){
                functionalDatabaseConfigEntities.forEach( functionalDatabaseConfigEntity -> {
                    if(functionalDatabaseConfigEntity.isActive()){
                        this.reloadJdbcTemplate(functionalDatabaseConfigEntity);
                    }
                });
            }else{
                log.warn("No functional database configuration found !");
                synchronized (this.reloadLock) {
                    this.jdbcTemplate = null;
                }
            }
        }
        synchronized (this.reloadLock) {
            return this.jdbcTemplate;
        }
    }

    private void reloadJdbcTemplate(FunctionalDatabaseConfigEntity functionalDatabaseConfigEntity) {

        DriverManagerDataSource ds = new DriverManagerDataSource();
        if (functionalDatabaseConfigEntity != null) {
            String databaseUrl = functionalDatabaseConfigEntity.getDatabaseUrl();
            if(functionalDatabaseConfigEntity.getDatabaseSchema() != null && !functionalDatabaseConfigEntity.getDatabaseSchema().isEmpty()){
                databaseUrl += "?currentSchema="+ functionalDatabaseConfigEntity.getDatabaseSchema();
            }
            ds.setJdbcUrl(databaseUrl);
            ds.setUser(functionalDatabaseConfigEntity.getDatabaseUsername());
            ds.setPassword(this.encryptingService.decrypt(functionalDatabaseConfigEntity.getDatabasePassword()));
            synchronized (this.reloadLock) {
                this.jdbcTemplate = new JdbcTemplate(ds);
            }
        } else {
            log.warn("No functional database configuration found !");
            synchronized (this.reloadLock) {
                this.jdbcTemplate = null;
            }
        }
    }
}
