package com.activiti.web.rest;


import com.activiti.domain.functionalMapper.FunctionalEntityMapping;
import com.activiti.service.functionalMapping.FunctionalMappingService;
import net.sf.jsqlparser.JSQLParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
public class FunctionalMappingController {

    @Autowired
    private FunctionalMappingService functionalMappingService;

    @RequestMapping(value = "/rest/functional-mapping/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody FunctionalEntityMapping getFunctionalEntityMapping(@PathVariable Long id){
        return this.functionalMappingService.getFunctionalEntityMapping(id);
    }

    @RequestMapping(value = "/rest/functional-mapping", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<FunctionalEntityMapping> getFunctionalEntitiesMapping(){
        return this.functionalMappingService.getFunctionalEntitiesMapping();
    }

    @RequestMapping(value = "/rest/functional-mapping", method = RequestMethod.POST, produces = "application/json")
    public void addFunctionalEntityMapping(@RequestBody FunctionalEntityMapping newFunctionalEntityMapping) {
        this.functionalMappingService.addFunctionalEntityMapping(newFunctionalEntityMapping);
    }

    @RequestMapping(value = "/rest/functional-mapping/{id}", method = RequestMethod.PUT, produces = "application/json")
    public void updateFunctionalEntityMapping(@RequestBody FunctionalEntityMapping updatedFunctionalEntityMapping, @PathVariable Long id) {
        this.functionalMappingService.updateFunctionalEntityMapping(updatedFunctionalEntityMapping, id);
    }

    @RequestMapping(value = "/rest/functional-mapping/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public  void deleteFunctionalEntityMapping(@PathVariable Long id){
        this.functionalMappingService.deleteFunctionalEntityMapping(id);
    }

}
