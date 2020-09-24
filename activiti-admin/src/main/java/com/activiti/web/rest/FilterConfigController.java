package com.activiti.web.rest;

import com.activiti.domain.FilterConfigEntity;
import com.activiti.helper.Couple;
import com.activiti.helper.Triple;
import com.activiti.service.AssigneeService;
import com.activiti.service.FilterConfigService;
import com.activiti.service.FunctionalFilter.Exception.FilterConfigNotFoundException;
import com.activiti.service.FunctionalFilter.FunctionalFilterEntityFetcherService;
import com.activiti.service.FunctionalFilter.QueryHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FilterConfigController {

    @Autowired
    private FilterConfigService filterConfigService;

    @Autowired
    private FunctionalFilterEntityFetcherService functionalFilterEntityFetcherService;

    @Autowired
    private QueryHelperService queryHelperService;

    @Autowired
    private AssigneeService assigneeService;

    @RequestMapping(value = "/rest/filters-config/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody FilterConfigEntity getFilterConfig(@PathVariable Long id) {
        return this.filterConfigService.getFilterConfig(id);
    }

    @RequestMapping(value = "/rest/filters-config/processdefinitionkey/{processDefinitionKey}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody FilterConfigEntity getFilterConfigByProcessDefinitionKey(@PathVariable String processDefinitionKey) {
        return this.filterConfigService.getFilterConfigByProcessDefinitionKey(processDefinitionKey).orElseThrow(() -> new FilterConfigNotFoundException("No filter found for process definition: "+ processDefinitionKey));
    }

    @RequestMapping(value = "/rest/filters-config", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<FilterConfigEntity> getFiltersConfig() {
        return this.filterConfigService.getFiltersConfig();
    }

    @RequestMapping(value = "/rest/filters-config", method = RequestMethod.POST, produces = "application/json")
    public void addFilterConfig(@RequestBody FilterConfigEntity newFilterConfigEntity) {
        this.filterConfigService.addFilterConfig(newFilterConfigEntity);
    }

    @RequestMapping(value = "/rest/filters-config/{id}", method = RequestMethod.PUT, produces = "application/json")
    public void updateFilterConfig(@RequestBody FilterConfigEntity updatedFilterConfigEntity, @PathVariable Long id) {
        this.filterConfigService.updateFilterConfig(updatedFilterConfigEntity, id);
    }

    @RequestMapping(value = "/rest/filters-config/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public void deleteFilterConfig(@PathVariable Long id) {
        this.filterConfigService.deleteFilterConfig(id);
    }

    @RequestMapping(value = "/rest/Filters-config/columnsnames/{processDefinitionKey}", method = RequestMethod.GET, produces = "application/json")
    public List<Couple> getFilterColumnsNames(@PathVariable String processDefinitionKey) {
        FilterConfigEntity filterConfigEntity = this.getFilterConfigByProcessDefinitionKey(processDefinitionKey);
        return this.functionalFilterEntityFetcherService.getTableColumnsNamesAndTypes(filterConfigEntity.getSqlQuery());
    }

    @RequestMapping(value = "/rest/filter-data/{processDefinitionKey}/{processIdColumnName}", method = RequestMethod.POST, produces = "application/json")
    public List<String> getSearchedDataByFilter(@PathVariable String processDefinitionKey, @PathVariable String processIdColumnName, @RequestBody List<Triple> filtersData) {
        FilterConfigEntity filterConfigEntity = this.getFilterConfigByProcessDefinitionKey(processDefinitionKey);
        String updatedQuery = filterConfigEntity.getSqlQuery();
        StringBuilder finalUpdatedQuery = this.queryHelperService.addConditionToQuery(filtersData, updatedQuery);
        return this.functionalFilterEntityFetcherService.getSearchedDataByFilter(String.valueOf(finalUpdatedQuery), processIdColumnName);
    }

    @RequestMapping(value = "/rest/filter-data/assignnes", method = RequestMethod.GET, produces =  "application/json")
    public List<Triple> getAssignees(){
        return this.assigneeService.getAssignees();
    }

}
