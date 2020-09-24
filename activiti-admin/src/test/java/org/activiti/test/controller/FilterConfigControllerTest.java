package org.activiti.test.controller;

import com.activiti.domain.FilterConfigEntity;
import com.activiti.domain.events.DatabaseConfigEvent;
import com.activiti.domain.functionalMapper.FunctionalDatabaseConfigEntity;
import com.activiti.helper.JdbcTemplateHolder;
import com.activiti.helper.Triple;
import com.activiti.service.FilterConfigService;
import com.activiti.service.FunctionalFilter.FunctionalFilterEntityFetcherService;
import com.activiti.service.FunctionalFilter.QueryHelperService;
import com.activiti.service.activiti.EncryptingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.activiti.test.ApplicationTestConfiguration;
import org.junit.Before;
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

import java.util.ArrayList;
import java.util.List;

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
public class FilterConfigControllerTest {

    @Autowired
    private FilterConfigService filterConfigService;

    @Autowired
    private FunctionalFilterEntityFetcherService functionalFilterEntityFetcherService;

    @Autowired
    private QueryHelperService queryHelperService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EncryptingService encryptingService;

    @Autowired
    private JdbcTemplateHolder jdbcTemplateHolder;

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    public void getFilterConfig() throws Exception {
        FilterConfigEntity filterConfigEntity = new FilterConfigEntity();

        filterConfigEntity.setFilterName("TEST");
        filterConfigEntity.setProcessDefinitionKey("ATTESTATION");
        filterConfigEntity.setSqlQuery("SELECT * FROM test_table");
        filterConfigEntity.setProcessIdColumnName("process_id");

        filterConfigService.addFilterConfig(filterConfigEntity);

        long id = this.filterConfigService.getFiltersConfig().get(0).getId();

        mockMvc.perform(get("/rest/filters-config/"+id)
                .contentType(APPLICATION_JSON_UTF8)
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    public void getFilterConfigByProcessDefinitionKey() throws Exception {
        String processDefinitionKey = "ATTESTATION";

        FilterConfigEntity filterConfigEntity = new FilterConfigEntity();
        filterConfigEntity.setId((long) 1);
        filterConfigEntity.setFilterName("TEST");
        filterConfigEntity.setProcessDefinitionKey("ATTESTATION");
        filterConfigEntity.setSqlQuery("SELECT * FROM test_table;");
        filterConfigEntity.setProcessIdColumnName("process_id");

        filterConfigService.addFilterConfig(filterConfigEntity);

        long id = this.filterConfigService.getFiltersConfig().get(0).getId();

        mockMvc.perform(get("/rest/filters-config/processdefinitionkey/"+ processDefinitionKey)
                .contentType(APPLICATION_JSON_UTF8)
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    public void getFiltersConfig() throws Exception {
        FilterConfigEntity filterConfigEntity = new FilterConfigEntity();
        filterConfigEntity.setId((long) 1);
        filterConfigEntity.setFilterName("TEST");
        filterConfigEntity.setProcessDefinitionKey("ATTESTATION");
        filterConfigEntity.setSqlQuery("SELECT * FROM test_table");
        filterConfigEntity.setProcessIdColumnName("process_id");

        filterConfigService.addFilterConfig(filterConfigEntity);
        filterConfigEntity.setId((long)2);
        filterConfigService.addFilterConfig(filterConfigEntity);

        mockMvc.perform(get("/rest/filters-config")
                .contentType(APPLICATION_JSON_UTF8)
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());



    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    public void addFilterConfig() throws Exception {

        FilterConfigEntity filterConfigEntity = new FilterConfigEntity();
        filterConfigEntity.setId((long) 1);
        filterConfigEntity.setFilterName("TEST");
        filterConfigEntity.setProcessDefinitionKey("ATTESTATION");
        filterConfigEntity.setSqlQuery("SELECT * FROM test_table");
        filterConfigEntity.setProcessIdColumnName("process_id");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String   requestJson= ow.writeValueAsString(filterConfigEntity);

        mockMvc.perform(post("/rest/filters-config")
                .contentType(APPLICATION_JSON_UTF8)
                .accept(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk());


    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    public void updateFilterConfig() throws Exception {

        FilterConfigEntity filterConfigEntity = new FilterConfigEntity();
        filterConfigEntity.setFilterName("TEST");
        filterConfigEntity.setProcessDefinitionKey("ATTESTATION");
        filterConfigEntity.setSqlQuery("SELECT * FROM test_table");
        filterConfigEntity.setProcessIdColumnName("process_id");

        this.filterConfigService.addFilterConfig(filterConfigEntity);

        filterConfigEntity.setFilterName("test update");

        long id = this.filterConfigService.getFiltersConfig().get(0).getId();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String   requestJson= ow.writeValueAsString(filterConfigEntity);

        mockMvc.perform(put("/rest/filters-config/"+id)
                .contentType(APPLICATION_JSON_UTF8)
                .accept(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk());


    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    public void deleteFilterConfig() throws Exception {

        FilterConfigEntity filterConfigEntity = new FilterConfigEntity();
        filterConfigEntity.setFilterName("TEST");
        filterConfigEntity.setProcessDefinitionKey("ATTESTATION");
        filterConfigEntity.setSqlQuery("SELECT * FROM test_table");
        filterConfigEntity.setProcessIdColumnName("process_id");

        this.filterConfigService.addFilterConfig(filterConfigEntity);

        long id = this.filterConfigService.getFiltersConfig().get(0).getId();

        mockMvc.perform(delete("/rest/filters-config/"+id)
                .contentType(APPLICATION_JSON_UTF8)
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

    }

    @Before
    public void init() {
        FunctionalDatabaseConfigEntity functionalDatabaseConfigEntity = new FunctionalDatabaseConfigEntity();
        functionalDatabaseConfigEntity.setId((long) 1);
        functionalDatabaseConfigEntity.setDatabaseUrl("jdbc:h2:mem:activiti-admin;MODE=MYSQL;DB_CLOSE_DELAY=1000");
        functionalDatabaseConfigEntity.setDatabaseSchema("");
        functionalDatabaseConfigEntity.setDatabaseUsername("sa");
        functionalDatabaseConfigEntity.setActive(true);
        functionalDatabaseConfigEntity.setDatabasePassword(this.encryptingService.encrypt(""));
        DatabaseConfigEvent databaseConfigEvent = new DatabaseConfigEvent(functionalDatabaseConfigEntity);
        this.jdbcTemplateHolder.handleDatabaseChangeEvent(databaseConfigEvent);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    public void getFilterColumnsNames() throws Exception {

        FilterConfigEntity filterConfigEntity = new FilterConfigEntity();
        filterConfigEntity.setFilterName("TEST");
        filterConfigEntity.setProcessDefinitionKey("ATTESTATION");
        filterConfigEntity.setSqlQuery("select * from functional_database_config");
        filterConfigEntity.setProcessIdColumnName("process_id");

        this.filterConfigService.addFilterConfig(filterConfigEntity);
        String processDefinitionKey = this.filterConfigService.getFiltersConfig().get(0).getProcessDefinitionKey();

        mockMvc.perform(get("/rest/Filters-config/columnsnames/"+processDefinitionKey)
                .contentType(APPLICATION_JSON_UTF8)
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    public void getSearchedDataByFilter() throws Exception {

        FilterConfigEntity filterConfigEntity = new FilterConfigEntity();
        filterConfigEntity.setFilterName("TEST");
        filterConfigEntity.setProcessDefinitionKey("ATTESTATION");
        filterConfigEntity.setSqlQuery("select * from functional_database_config");
        filterConfigEntity.setProcessIdColumnName("process_id");

        this.filterConfigService.addFilterConfig(filterConfigEntity);
        String processDefinitionKey = this.filterConfigService.getFiltersConfig().get(0).getProcessDefinitionKey();
        String processIdColumnName = "id";
        List<Triple> filtersData = new ArrayList<>();
        Triple triple = new Triple();
        triple.setKey("database_username");
        triple.setValue("nidhal");
        triple.setType("String");
        filtersData.add(triple);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String   requestJson= ow.writeValueAsString(filtersData);

        mockMvc.perform(post("/rest/filter-data/"+processDefinitionKey+"/"+processIdColumnName)
                .contentType(APPLICATION_JSON_UTF8)
                .accept(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk());



    }


}
