package com.activiti.service;

import com.activiti.domain.FilterConfigEntity;
import com.activiti.helper.Couple;
import com.activiti.repository.FilterConfigRepository;
import com.activiti.service.FunctionalFilter.Exception.ExistingFilterConfigException;
import com.activiti.service.FunctionalFilter.Exception.FilterConfigNotFoundException;
import com.activiti.service.FunctionalFilter.FunctionalFilterEntityFetcherService;
import com.activiti.service.functionalMapping.Exception.InvalidSelectQueryException;
import com.activiti.service.functionalMapping.QueryCheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FilterConfigService {

    @Autowired
    private FilterConfigRepository filterConfigRepository;

    @Autowired
    private FunctionalFilterEntityFetcherService functionalFilterEntityFetcherService;

    @Autowired
    private QueryCheckingService queryCheckingService;

    public FilterConfigEntity getFilterConfig(Long id) {
        return this.filterConfigRepository.findOne(id);
    }

    public Optional<FilterConfigEntity> getFilterConfigByProcessDefinitionKey(String processDefinitionKey) {
        return this.filterConfigRepository.findByProcessDefinitionKey(processDefinitionKey);
    }

    public List<FilterConfigEntity> getFiltersConfig() {
        return this.filterConfigRepository.findAll();
    }

    public FilterConfigEntity addFilterConfig(FilterConfigEntity newFilterConfigEntity) {

        Optional<FilterConfigEntity> functionalEntityMapping = this.filterConfigRepository.findByProcessDefinitionKey(newFilterConfigEntity.getProcessDefinitionKey());
        if (functionalEntityMapping.isPresent()) {
            throw  new ExistingFilterConfigException("filter config already exist for: "+newFilterConfigEntity.getProcessDefinitionKey());
        }

        if (this.queryCheckingService.checkQueryIsSelect(newFilterConfigEntity.getSqlQuery())) {
            return this.filterConfigRepository.save(newFilterConfigEntity);
        } else {
            throw new InvalidSelectQueryException("Invalid Sql Select query");
        }
    }

    public FilterConfigEntity updateFilterConfig(FilterConfigEntity updatedFilterConfigEntity, Long id) {
        FilterConfigEntity filterConfigEntity = this.getFilterConfig(updatedFilterConfigEntity.getId());
        filterConfigEntity.setId(updatedFilterConfigEntity.getId());
        filterConfigEntity.setFilterName(updatedFilterConfigEntity.getFilterName());
        filterConfigEntity.setProcessIdColumnName(updatedFilterConfigEntity.getProcessIdColumnName());
        filterConfigEntity.setProcessDefinitionKey(updatedFilterConfigEntity.getProcessDefinitionKey());
        filterConfigEntity.setSqlQuery(updatedFilterConfigEntity.getSqlQuery());

        if (this.queryCheckingService.checkQueryIsSelect(filterConfigEntity.getSqlQuery())) {
            return this.filterConfigRepository.save(filterConfigEntity);
        } else {
            throw new InvalidSelectQueryException("Invalid Sql Select query");
        }
    }

    public void deleteFilterConfig(Long id){
        this.filterConfigRepository.delete(id);
    }

    public List<Couple> getFilterColumnNames(String processDefinitionKey){
       Optional<FilterConfigEntity> filterConfigEntityOptional = this.filterConfigRepository.findByProcessDefinitionKey(processDefinitionKey);
       if(filterConfigEntityOptional.isPresent()){
           return  this.functionalFilterEntityFetcherService.getTableColumnsNamesAndTypes(filterConfigEntityOptional.get().getSqlQuery());
       }else{
           throw new FilterConfigNotFoundException("No filter found for process definition: " + processDefinitionKey);
       }
    }
}
