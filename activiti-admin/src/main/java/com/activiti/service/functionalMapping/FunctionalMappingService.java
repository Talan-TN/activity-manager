package com.activiti.service.functionalMapping;

import com.activiti.domain.functionalMapper.FunctionalEntityMapping;
import com.activiti.helper.Couple;
import com.activiti.helper.RestApiServerConfigHelper;
import com.activiti.helper.Triple;
import com.activiti.repository.FunctionalEntityMappingRepository;
import com.activiti.service.activiti.ProcessDefinitionService;
import com.activiti.service.activiti.exception.ActivitiServiceException;
import com.activiti.service.functionalMapping.Exception.ExistingMappingException;
import com.activiti.service.functionalMapping.Exception.InvalidQueryException;
import com.activiti.service.functionalMapping.Exception.InvalidSelectQueryException;
import com.activiti.web.rest.exception.BadRequestException;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.List;
import java.util.Optional;


@Service
public class FunctionalMappingService {


    @Autowired
    protected ProcessDefinitionService processDefinitionService;

    @Autowired
    private FunctionalEntityMappingRepository functionalEntityMappingRepository;

    @Autowired
    private FunctionalEntityFetcherService functionalEntityFetcherService;

    @Autowired
    private RestApiServerConfigHelper restApiServerConfigHelper;

    @Autowired
    private QueryCheckingService queryCheckingService;

    public List<Triple> getProcessInstanceFunctionalEntity(String processDefinitionKey, String processInstanceId) {
        try {
            Optional<FunctionalEntityMapping> functionalEntityMapping = this.functionalEntityMappingRepository.findByProcessDefinitionKey(processDefinitionKey);
            if (functionalEntityMapping.isPresent()) {
                return this.functionalEntityFetcherService.find(functionalEntityMapping.get().getSqlQuery(), processInstanceId);
            } else {
                return null;
            }
        } catch (ActivitiServiceException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public FunctionalEntityMapping getFunctionalEntityMapping(Long id) {
        try {
            FunctionalEntityMapping functionalEntityMapping = this.functionalEntityMappingRepository.findOne(id);

            return functionalEntityMapping;

        } catch (ActivitiServiceException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public List<FunctionalEntityMapping> getFunctionalEntitiesMapping() {
        return this.functionalEntityMappingRepository.findAll();
    }

    public FunctionalEntityMapping addFunctionalEntityMapping(FunctionalEntityMapping newFunctionalEntityMapping) {

        Optional<FunctionalEntityMapping> functionalEntityMapping = this.functionalEntityMappingRepository.findByProcessDefinitionKey(newFunctionalEntityMapping.getProcessDefinitionKey());
        if (functionalEntityMapping.isPresent()) {
            throw  new ExistingMappingException("Mapping already exist for: "+newFunctionalEntityMapping.getProcessDefinitionKey());
        }

        if (this.queryCheckingService.checkQueryIsSelect(newFunctionalEntityMapping.getSqlQuery())) {
            return functionalEntityMappingRepository.save(newFunctionalEntityMapping);
        } else {
            throw new InvalidSelectQueryException("Invalid Sql Select query");

        }
    }

    public FunctionalEntityMapping updateFunctionalEntityMapping(FunctionalEntityMapping updatedFunctionalEntityMapping, Long id) {
        FunctionalEntityMapping functionalEntityMapping = functionalEntityMappingRepository.findOne(id);

        if (StringUtils.isNotEmpty(functionalEntityMapping.getName())) {
            functionalEntityMapping.setId(updatedFunctionalEntityMapping.getId());
            functionalEntityMapping.setName(updatedFunctionalEntityMapping.getName());
            functionalEntityMapping.setProcessDefinitionKey(updatedFunctionalEntityMapping.getProcessDefinitionKey());
            functionalEntityMapping.setSqlQuery(updatedFunctionalEntityMapping.getSqlQuery());
        }
        if (this.queryCheckingService.checkQueryIsSelect(functionalEntityMapping.getSqlQuery())) {
            return functionalEntityMappingRepository.save(functionalEntityMapping);
        } else {
            throw new InvalidSelectQueryException("Invalid Sql Select query");

        }
    }

    public void deleteFunctionalEntityMapping(Long id) {
        functionalEntityMappingRepository.delete(id);
    }


}
