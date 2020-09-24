package com.activiti.web.rest;


import com.activiti.domain.events.DatabaseConfigEvent;
import com.activiti.domain.functionalMapper.FunctionalDatabaseConfigEntity;
import com.activiti.helper.JdbcTemplateHolder;
import com.activiti.service.activiti.EncryptingService;
import com.activiti.service.functionalMapping.FunctionalDatabaseConfigService;
import com.activiti.web.rest.dto.FunctionalDatabaseConfigDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class FunctionalDatabaseConfigController {

    @Autowired
    private FunctionalDatabaseConfigService functionalDatabaseConfigService;

    @Autowired
    private JdbcTemplateHolder jdbcTemplateHolder;

    @RequestMapping(value = "/rest/database-configs/active", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody  FunctionalDatabaseConfigDTO getFunctionalDatabaseConfig(@RequestParam(name = "active", required = true) boolean active) {
        return this.functionalDatabaseConfigService.findActive(active);
    }

    @RequestMapping(value = "/rest/database-configs", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<FunctionalDatabaseConfigDTO> getAllFunctionalDatabaseConfig(){
        return this.functionalDatabaseConfigService.findAll();
    }

    @RequestMapping(value = "/rest/database-configs/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody  FunctionalDatabaseConfigDTO getFunctionalDatabaseConfig(@PathVariable Long id) {
        return this.functionalDatabaseConfigService.findOne(id);
    }


    @RequestMapping(value = "/rest/database-configs", method = RequestMethod.POST, produces = "application/json")
    public void createFunctionalDatabaseConfig(@RequestBody FunctionalDatabaseConfigDTO functionalDatabaseConfigDTO) {
         functionalDatabaseConfigService.save(functionalDatabaseConfigDTO);
    }

    @RequestMapping(value = "/rest/database-configs/{id}", method = RequestMethod.PUT, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateFunctionalDatabase(@PathVariable Long id, @RequestBody FunctionalDatabaseConfigDTO functionalDatabaseConfigDTO) {
        this.functionalDatabaseConfigService.update(functionalDatabaseConfigDTO);

    }

    @RequestMapping(value = "/rest/database-configs/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public void deleteFunctionalDatabaseConfig(@PathVariable Long id) {
         functionalDatabaseConfigService.deleteFunctionalDatabaseConfig(id);
    }

    @RequestMapping(value = "/rest/database-configs/healthCheck/{id}", method = RequestMethod.GET)
    public void checkFunctionalDatabaseHealth(@PathVariable Long id) {
        this.functionalDatabaseConfigService.checkFunctionalDatabaseHealth(id);
    }

}
