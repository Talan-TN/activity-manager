package com.activiti.service.functionalMapping;

import com.activiti.domain.functionalMapper.FunctionalDatabaseConfigEntity;
import com.activiti.domain.events.DatabaseConfigEvent;
import com.activiti.helper.JdbcTemplateHolder;
import com.activiti.repository.FunctionalDatabaseConfigRepository;
import com.activiti.service.activiti.EncryptingService;
import com.activiti.web.rest.dto.FunctionalDatabaseConfigDTO;
import com.activiti.web.rest.exception.BadRequestException;
import com.google.common.eventbus.EventBus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class FunctionalDatabaseConfigService {

    @Autowired
    private FunctionalDatabaseConfigRepository functionalDatabaseConfigRepository;

    @Autowired
    private EncryptingService encryptingService;

    @Autowired
    private EventBus eventBus;

    @Autowired
    private JdbcTemplateHolder jdbcTemplateHolder;

    @Value("${functionalDatabase.checkingQuery}")
    private String databaseCheckingQuery;

    @Transactional
    public FunctionalDatabaseConfigDTO findActive(boolean active) {
        FunctionalDatabaseConfigEntity functionalDatabaseConfigEntity = this.functionalDatabaseConfigRepository.findByActive(active);
        FunctionalDatabaseConfigDTO functionalDatabaseConfigDTO = new FunctionalDatabaseConfigDTO();
        BeanUtils.copyProperties(functionalDatabaseConfigEntity,functionalDatabaseConfigDTO);
        if (StringUtils.isNotEmpty(functionalDatabaseConfigDTO.getDatabasePassword())) {
            functionalDatabaseConfigEntity.setDatabasePassword (this.encryptingService.encrypt(functionalDatabaseConfigDTO.getDatabasePassword()));
        }
        return functionalDatabaseConfigDTO;
    }

    @Transactional
    public FunctionalDatabaseConfigDTO findOne(Long id) {
        FunctionalDatabaseConfigEntity functionalDatabaseConfigEntity = this.functionalDatabaseConfigRepository.findOne(id);
        FunctionalDatabaseConfigDTO functionalDatabaseConfigDTO = new FunctionalDatabaseConfigDTO();
        BeanUtils.copyProperties(functionalDatabaseConfigEntity,functionalDatabaseConfigDTO);
        if (StringUtils.isNotEmpty(functionalDatabaseConfigDTO.getDatabasePassword())) {
            functionalDatabaseConfigEntity.setDatabasePassword (this.encryptingService.encrypt(functionalDatabaseConfigDTO.getDatabasePassword()));
        }
        return functionalDatabaseConfigDTO;
    }

    @Transactional
    public List<FunctionalDatabaseConfigDTO> findAll(){
        List<FunctionalDatabaseConfigEntity> functionalDatabaseConfigEntities = this.functionalDatabaseConfigRepository.findAll();
        List<FunctionalDatabaseConfigDTO> functionalDatabaseConfigDTOList = new ArrayList<>();
        functionalDatabaseConfigEntities.forEach(functionalDatabaseConfigEntity -> {
            FunctionalDatabaseConfigDTO functionalDatabaseConfigDTO= new FunctionalDatabaseConfigDTO();
            BeanUtils.copyProperties(functionalDatabaseConfigEntity, functionalDatabaseConfigDTO);
            functionalDatabaseConfigDTO.setDatabasePassword(null);
            functionalDatabaseConfigDTOList.add(functionalDatabaseConfigDTO);
        });
        return functionalDatabaseConfigDTOList;
    }

    @Transactional
    public void save(FunctionalDatabaseConfigDTO functionalDatabaseConfigDTO) {
        FunctionalDatabaseConfigEntity functionalDatabaseConfigEntity = new FunctionalDatabaseConfigEntity();
        BeanUtils.copyProperties(functionalDatabaseConfigDTO,functionalDatabaseConfigEntity);
        this.functionalDatabaseConfigRepository.save(functionalDatabaseConfigEntity);
        this.eventBus.post(new DatabaseConfigEvent(functionalDatabaseConfigEntity));
    }

    public void update(FunctionalDatabaseConfigDTO functionalDatabaseConfigDTO){
        FunctionalDatabaseConfigEntity functionalDatabaseConfigEntity = this.functionalDatabaseConfigRepository.findOne(functionalDatabaseConfigDTO.getId());

        if (functionalDatabaseConfigEntity == null) {
            throw new BadRequestException("Functional database with id '" + functionalDatabaseConfigDTO.getId() + "' does not exist");
        }

        if (StringUtils.isNotEmpty(functionalDatabaseConfigDTO.getDatabasePassword())) {
            functionalDatabaseConfigEntity.setDatabasePassword (this.encryptingService.encrypt(functionalDatabaseConfigDTO.getDatabasePassword()));
        }

        functionalDatabaseConfigEntity.setId(functionalDatabaseConfigDTO.getId());
        functionalDatabaseConfigEntity.setDatabaseUrl(functionalDatabaseConfigDTO.getDatabaseUrl());
        functionalDatabaseConfigEntity.setDatabaseSchema(functionalDatabaseConfigDTO.getDatabaseSchema());
        functionalDatabaseConfigEntity.setDatabaseUsername(functionalDatabaseConfigDTO.getDatabaseUsername());
        functionalDatabaseConfigEntity.setActive(functionalDatabaseConfigDTO.isActive());
        if(functionalDatabaseConfigEntity.isActive()){
            this.eventBus.post(new DatabaseConfigEvent(functionalDatabaseConfigEntity));
        }
        this.functionalDatabaseConfigRepository.save(functionalDatabaseConfigEntity);
    }

    public void deleteFunctionalDatabaseConfig(Long id) {
        functionalDatabaseConfigRepository.delete(id);
        this.eventBus.post(new DatabaseConfigEvent(null));
    }


    public void checkFunctionalDatabaseHealth(long id){
        FunctionalDatabaseConfigDTO functionalDatabaseConfigDTO = this.findOne(id);
        FunctionalDatabaseConfigEntity functionalDatabaseConfigEntity = new FunctionalDatabaseConfigEntity();
        BeanUtils.copyProperties(functionalDatabaseConfigDTO,functionalDatabaseConfigEntity);

        DatabaseConfigEvent databaseConfigEvent = new DatabaseConfigEvent(functionalDatabaseConfigEntity);
        this.jdbcTemplateHolder.handleDatabaseChangeEvent(databaseConfigEvent);
        this.eventBus.post(new DatabaseConfigEvent(functionalDatabaseConfigEntity));
        this.jdbcTemplateHolder.getJdbcTemplate().execute(this.databaseCheckingQuery);
    }



}
