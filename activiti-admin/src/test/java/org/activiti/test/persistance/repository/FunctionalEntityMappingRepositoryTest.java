package org.activiti.test.persistance.repository;

import com.activiti.domain.functionalMapper.FunctionalEntityMapping;
import com.activiti.repository.FunctionalEntityMappingRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ApplicationTestConfiguration.class)
@Transactional
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:reset.sql")
})
public class FunctionalEntityMappingRepositoryTest {

    @Autowired
    private FunctionalEntityMappingRepository functionalEntityMappingRepository;

    @Test
    public void findAll(){
        FunctionalEntityMapping functionalEntityMapping1 = new FunctionalEntityMapping();
        functionalEntityMapping1.setProcessDefinitionKey("processkey1");
        functionalEntityMapping1.setName("testProcessKey1");
        functionalEntityMapping1.setSqlQuery("select * from test");
        functionalEntityMappingRepository.save(functionalEntityMapping1);

        FunctionalEntityMapping functionalEntityMapping2 = new FunctionalEntityMapping();
        functionalEntityMapping2.setProcessDefinitionKey("processkey2");
        functionalEntityMapping2.setName("testProcessKey2");
        functionalEntityMapping2.setSqlQuery("select * from test2");
        functionalEntityMappingRepository.save(functionalEntityMapping2);
        List<FunctionalEntityMapping> functionalEntityMappingList = this.functionalEntityMappingRepository.findAll();
        assertEquals(2,functionalEntityMappingList.size());
    }

    @Test
    public void findOne(){
        FunctionalEntityMapping functionalEntityMapping1 = new FunctionalEntityMapping();
        functionalEntityMapping1.setProcessDefinitionKey("processkey1");
        functionalEntityMapping1.setName("testProcessKey1");
        functionalEntityMapping1.setSqlQuery("select * from test");
        functionalEntityMappingRepository.save(functionalEntityMapping1);

        Optional<FunctionalEntityMapping> functionalEntityMapping = functionalEntityMappingRepository.findByProcessDefinitionKey("testProcessKey1");
        if (functionalEntityMapping.isPresent()){
            assertEquals("testProcessKey1", functionalEntityMapping.get().getProcessDefinitionKey());
        }
    }

    @Test
    public void delete(){
        FunctionalEntityMapping functionalEntityMapping1 = new FunctionalEntityMapping();
        functionalEntityMapping1.setProcessDefinitionKey("processkey1");
        functionalEntityMapping1.setName("testProcessKey1");
        functionalEntityMapping1.setSqlQuery("select * from test");
        functionalEntityMappingRepository.save(functionalEntityMapping1);

        Long id = functionalEntityMappingRepository.findAll().get(0).getId();

        functionalEntityMappingRepository.delete(id);
        assertEquals(0,functionalEntityMappingRepository.findAll().size());
    }
}
