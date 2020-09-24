package org.activiti.test.services;

import com.activiti.domain.events.DatabaseConfigEvent;
import com.activiti.domain.functionalMapper.FunctionalDatabaseConfigEntity;
import com.activiti.helper.Couple;
import com.activiti.helper.JdbcTemplateHolder;
import com.activiti.service.FunctionalFilter.FunctionalFilterEntityFetcherService;
import com.activiti.service.activiti.EncryptingService;
import com.activiti.service.functionalMapping.FunctionalDatabaseConfigService;
import org.activiti.test.ApplicationTestConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ApplicationTestConfiguration.class)
@Transactional
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:addtable.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:reset.sql")
})
public class FunctionalFilterEntityFetcherServiceTest {

    @Autowired
    private FunctionalFilterEntityFetcherService functionalFilterEntityFetcherService;

    @Autowired
    private FunctionalDatabaseConfigService functionalDatabaseConfigService;

    @Autowired
    private JdbcTemplateHolder jdbcTemplateHolder;

    @Autowired
    private EncryptingService encryptingService;


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
    public void getColumnsNamesFromTable() {

        List<Couple> columns = this.functionalFilterEntityFetcherService.getTableColumnsNamesAndTypes("SELECT * FROM test_table");
        assertEquals(3, columns.size());


    }

    @Before
    public void init2() {
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
    public void getSearchedDataByFilter(){
        String processIdColumnName = "id";
        String query = "SELECT * from test_table";
        List<String> ids = this.functionalFilterEntityFetcherService.getSearchedDataByFilter(query, processIdColumnName);
        assertNotNull(ids);
    }



}
