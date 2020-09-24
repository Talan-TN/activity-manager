package org.activiti.test.controller;

import com.activiti.service.activiti.EncryptingService;
import com.activiti.service.functionalMapping.FunctionalDatabaseConfigService;
import com.activiti.web.rest.dto.FunctionalDatabaseConfigDTO;
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
public class FunctionalDatabaseConfigControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FunctionalDatabaseConfigService functionalDatabaseConfigService;

    @Autowired
    private EncryptingService encryptingService;



    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    public void getFunctionalDatabaseConfig() throws Exception {
        FunctionalDatabaseConfigDTO functionalDatabaseConfigDTO = new FunctionalDatabaseConfigDTO();
        functionalDatabaseConfigDTO.setDatabaseUrl("url1");
        functionalDatabaseConfigDTO.setDatabaseUsername("user1");
        functionalDatabaseConfigDTO.setDatabasePassword(encryptingService.encrypt("password1"));
        functionalDatabaseConfigDTO.setDatabaseSchema("schema1");
        functionalDatabaseConfigDTO.setActive(false);
        functionalDatabaseConfigService.save(functionalDatabaseConfigDTO);
        Long id = this.functionalDatabaseConfigService.findAll().get(0).getId();

        mockMvc.perform(get("/rest/database-configs/"+id)
                .contentType(APPLICATION_JSON_UTF8)
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    public void createFunctionalDatabaseConfig() throws Exception {
        FunctionalDatabaseConfigDTO functionalDatabaseConfigDTO = new FunctionalDatabaseConfigDTO();
        functionalDatabaseConfigDTO.setDatabaseUrl("url1");
        functionalDatabaseConfigDTO.setDatabaseUsername("user1");
        functionalDatabaseConfigDTO.setActive(true);
        functionalDatabaseConfigDTO.setDatabasePassword(encryptingService.encrypt("password1"));
        functionalDatabaseConfigDTO.setDatabaseSchema("schema1");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String   requestJson= ow.writeValueAsString(functionalDatabaseConfigDTO);

        mockMvc.perform(post("/rest/database-configs")
                .contentType(APPLICATION_JSON_UTF8)
                .accept(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    public void updateFunctionalDatabase() throws Exception {

        FunctionalDatabaseConfigDTO functionalDatabaseConfigDTO = new FunctionalDatabaseConfigDTO();
        functionalDatabaseConfigDTO.setDatabaseUrl("url1");
        functionalDatabaseConfigDTO.setDatabaseUsername("user1");
        functionalDatabaseConfigDTO.setActive(true);
        functionalDatabaseConfigDTO.setDatabasePassword(encryptingService.encrypt("password1"));
        functionalDatabaseConfigDTO.setDatabaseSchema("schema1");
        functionalDatabaseConfigService.save(functionalDatabaseConfigDTO);
        Long id = this.functionalDatabaseConfigService.findAll().get(0).getId();

        functionalDatabaseConfigDTO.setDatabaseUsername("updatedUser");
        functionalDatabaseConfigDTO.setDatabaseSchema("updatedSchema");
        functionalDatabaseConfigDTO.setId(id);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String   requestJson= ow.writeValueAsString(functionalDatabaseConfigDTO);

        mockMvc.perform(put("/rest/database-configs/"+id)
                .contentType(APPLICATION_JSON_UTF8)
                .accept(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    public void deleteFunctionalDatabaseConfig() throws Exception {
        FunctionalDatabaseConfigDTO functionalDatabaseConfigDTO = new FunctionalDatabaseConfigDTO();
        functionalDatabaseConfigDTO.setDatabaseUrl("url1");
        functionalDatabaseConfigDTO.setDatabaseUsername("user1");
        functionalDatabaseConfigDTO.setActive(true);
        functionalDatabaseConfigDTO.setDatabasePassword(encryptingService.encrypt("password1"));
        functionalDatabaseConfigDTO.setDatabaseSchema("schema1");
        functionalDatabaseConfigService.save(functionalDatabaseConfigDTO);
        Long id = this.functionalDatabaseConfigService.findAll().get(0).getId();
        mockMvc.perform(delete("/rest/database-configs/"+id)
                .contentType(APPLICATION_JSON_UTF8)
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

}
