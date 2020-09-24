package org.activiti.test.controller;

import com.activiti.domain.functionalMapper.FunctionalEntityMapping;
import com.activiti.service.functionalMapping.FunctionalMappingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.activiti.test.ApplicationTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ApplicationTestConfiguration.class)
@Transactional
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:reset.sql")
})
@AutoConfigureMockMvc
public class FunctionalMappingControllerTest {

    @Autowired
    private FunctionalMappingService functionalMappingService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    public void getFunctionalEntityMapping() throws Exception {
        FunctionalEntityMapping functionalEntityMapping = new FunctionalEntityMapping();
        functionalEntityMapping.setName("CRA Test");
        functionalEntityMapping.setProcessDefinitionKey("CRA");
        functionalEntityMapping.setSqlQuery("select * from cra where processId = :processId");

        functionalMappingService.addFunctionalEntityMapping(functionalEntityMapping);
        Long id = this.functionalMappingService.getFunctionalEntitiesMapping().get(0).getId();

        mockMvc.perform(get("/rest/functional-mapping/"+id)
                .contentType(APPLICATION_JSON_UTF8)
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    public void getFunctionalEntitiesMapping() throws Exception {

        FunctionalEntityMapping functionalEntityMapping = new FunctionalEntityMapping();
        functionalEntityMapping.setName("CRA Test");
        functionalEntityMapping.setProcessDefinitionKey("CRA");
        functionalEntityMapping.setSqlQuery("select * from cra where processId = :processId");

        functionalMappingService.addFunctionalEntityMapping(functionalEntityMapping);

        FunctionalEntityMapping functionalEntityMapping2 = new FunctionalEntityMapping();
        functionalEntityMapping2.setName("CRA Test 2");
        functionalEntityMapping2.setProcessDefinitionKey("CRA");
        functionalEntityMapping2.setSqlQuery("select * from cra2 where processId = :processId");

        functionalMappingService.addFunctionalEntityMapping(functionalEntityMapping2);

        mockMvc.perform(get("/rest/functional-mapping")
                .contentType(APPLICATION_JSON_UTF8)
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        assertEquals(2, this.functionalMappingService.getFunctionalEntitiesMapping().size());

    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    public void addFunctionalEntityMapping() throws Exception {

        FunctionalEntityMapping functionalEntityMapping = new FunctionalEntityMapping();
        functionalEntityMapping.setName("CRA Test");
        functionalEntityMapping.setProcessDefinitionKey("CRA");
        functionalEntityMapping.setSqlQuery("select * from cra where processId = :processId");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String   requestJson= ow.writeValueAsString(functionalEntityMapping);

        mockMvc.perform(post("/rest/functional-mapping")
                .contentType(APPLICATION_JSON_UTF8)
                .accept(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    public void updateFunctionalEntityMapping() throws Exception {

        FunctionalEntityMapping functionalEntityMapping = new FunctionalEntityMapping();
        functionalEntityMapping.setName("CRA Test");
        functionalEntityMapping.setProcessDefinitionKey("CRA");
        functionalEntityMapping.setSqlQuery("select * from cra where processId = :processId");
        functionalMappingService.addFunctionalEntityMapping(functionalEntityMapping);
        Long id = this.functionalMappingService.getFunctionalEntitiesMapping().get(0).getId();

        functionalEntityMapping.setName("update CRA Test");
        functionalEntityMapping.setId(id);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String   requestJson= ow.writeValueAsString(functionalEntityMapping);

        mockMvc.perform(put("/rest/functional-mapping/"+id)
                .contentType(APPLICATION_JSON_UTF8)
                .accept(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk());

    }
    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    public void deleteFunctionalEntityMapping() throws Exception {
        FunctionalEntityMapping functionalEntityMapping = new FunctionalEntityMapping();
        functionalEntityMapping.setName("CRA Test");
        functionalEntityMapping.setProcessDefinitionKey("CRA");
        functionalEntityMapping.setSqlQuery("select * from cra where processId = :processId");
        functionalMappingService.addFunctionalEntityMapping(functionalEntityMapping);
        Long id = this.functionalMappingService.getFunctionalEntitiesMapping().get(0).getId();

        mockMvc.perform(delete("/rest/functional-mapping/"+id)
                .contentType(APPLICATION_JSON_UTF8)
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }



}
