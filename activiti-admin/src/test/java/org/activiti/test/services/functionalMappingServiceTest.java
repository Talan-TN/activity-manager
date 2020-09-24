package org.activiti.test.services;

import com.activiti.domain.functionalMapper.FunctionalEntityMapping;
import com.activiti.service.functionalMapping.FunctionalEntityFetcherService;
import com.activiti.service.functionalMapping.FunctionalMappingService;
import net.sf.jsqlparser.JSQLParserException;
import org.activiti.test.ApplicationTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ApplicationTestConfiguration.class)
@Transactional
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:reset.sql")
})
public class functionalMappingServiceTest {

    @Autowired
    private FunctionalMappingService functionalMappingService;

    @Autowired
    private FunctionalEntityFetcherService functionalEntityFetcherService;


    @Test
    public void addFunctionalEntityMapping() throws Exception {
        FunctionalEntityMapping functionalEntityMapping1 = new FunctionalEntityMapping();

        functionalEntityMapping1.setName("test1");
        functionalEntityMapping1.setProcessDefinitionKey("key1");
        functionalEntityMapping1.setSqlQuery("Select * from test");

        this.functionalMappingService.addFunctionalEntityMapping(functionalEntityMapping1);
        assertNotNull(functionalMappingService.getFunctionalEntitiesMapping().get(0).getId());
    }

    @Test
    public void getFunctionalEntityMapping() throws Exception {
        FunctionalEntityMapping functionalEntityMapping1 = new FunctionalEntityMapping();
        functionalEntityMapping1.setName("test1");
        functionalEntityMapping1.setProcessDefinitionKey("key1");
        functionalEntityMapping1.setSqlQuery("select * from test where processId=:processId");
        functionalMappingService.addFunctionalEntityMapping(functionalEntityMapping1);
        Long id = functionalMappingService.getFunctionalEntitiesMapping().get(0).getId();
        FunctionalEntityMapping functionalEntityMapping = functionalMappingService.getFunctionalEntityMapping(id);
        assertEquals("test1", functionalEntityMapping.getName());

    }

    @Test
    public void getFunctionalEntitiesMapping() throws Exception {

        FunctionalEntityMapping functionalEntityMapping1 = new FunctionalEntityMapping();
        functionalEntityMapping1.setName("test1");
        functionalEntityMapping1.setProcessDefinitionKey("key1");
        functionalEntityMapping1.setSqlQuery("select * from test where processId=:processId");
        functionalMappingService.addFunctionalEntityMapping(functionalEntityMapping1);

        FunctionalEntityMapping functionalEntityMapping2 = new FunctionalEntityMapping();
        functionalEntityMapping2.setName("test2");
        functionalEntityMapping2.setProcessDefinitionKey("key2");
        functionalEntityMapping2.setSqlQuery("select * from test2 where processId=:processId");
        functionalMappingService.addFunctionalEntityMapping(functionalEntityMapping2);

        int size = this.functionalMappingService.getFunctionalEntitiesMapping().size();
        assertEquals(2, size);
    }

    @Test
    public void deleteFunctionalEntityMapping() throws Exception {
        FunctionalEntityMapping functionalEntityMapping1 = new FunctionalEntityMapping();
        functionalEntityMapping1.setName("test1");
        functionalEntityMapping1.setProcessDefinitionKey("key1");
        functionalEntityMapping1.setSqlQuery("select * from test where processId=:processId");
        functionalMappingService.addFunctionalEntityMapping(functionalEntityMapping1);
        Long id = functionalMappingService.getFunctionalEntitiesMapping().get(0).getId();
        functionalMappingService.deleteFunctionalEntityMapping(id);
        assertEquals(0, functionalMappingService.getFunctionalEntitiesMapping().size());
    }
}
