package org.activiti.test.persistance.repository;

import com.activiti.domain.functionalMapper.FunctionalDatabaseConfigEntity;
import com.activiti.repository.FunctionalDatabaseConfigRepository;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ApplicationTestConfiguration.class)
@Transactional
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:reset.sql")
})
public class FunctionalDatabaseConfigRepositoryTest {

    @Autowired
    private FunctionalDatabaseConfigRepository functionalDatabaseConfigRepository;

    @Test
    public void findAll(){
        FunctionalDatabaseConfigEntity functionalDatabaseConfig1 = new FunctionalDatabaseConfigEntity();
        functionalDatabaseConfig1.setDatabaseUsername("user1");
        functionalDatabaseConfig1.setDatabasePassword("password1");
        functionalDatabaseConfig1.setDatabaseSchema("schema1");
        functionalDatabaseConfig1.setDatabaseUrl("url1");
        functionalDatabaseConfig1.setActive(false);
        functionalDatabaseConfigRepository.save(functionalDatabaseConfig1);

        FunctionalDatabaseConfigEntity functionalDatabaseConfig2 = new FunctionalDatabaseConfigEntity();
        functionalDatabaseConfig2.setDatabaseUsername("user1");
        functionalDatabaseConfig2.setDatabasePassword("password2");
        functionalDatabaseConfig2.setDatabaseSchema("schema2");
        functionalDatabaseConfig2.setDatabaseUrl("url2");
        functionalDatabaseConfig2.setActive(false);
        functionalDatabaseConfigRepository.save(functionalDatabaseConfig2);

        List<FunctionalDatabaseConfigEntity> functionalDatabaseConfigEntities = this.functionalDatabaseConfigRepository.findAll();
        assertEquals(2,functionalDatabaseConfigEntities.size());
    }

    @Test
    public void findOne(){
        FunctionalDatabaseConfigEntity functionalDatabaseConfig1 = new FunctionalDatabaseConfigEntity();
        functionalDatabaseConfig1.setDatabaseUsername("user1");
        functionalDatabaseConfig1.setDatabasePassword("password1");
        functionalDatabaseConfig1.setDatabaseSchema("schema1");
        functionalDatabaseConfig1.setDatabaseUrl("url1");
        functionalDatabaseConfig1.setActive(false);
        functionalDatabaseConfigRepository.save(functionalDatabaseConfig1);
        Long id = this.functionalDatabaseConfigRepository.findAll().get(0).getId();
        FunctionalDatabaseConfigEntity functionalDatabaseConfigEntity = this.functionalDatabaseConfigRepository.findOne(id);
        assertNotNull(functionalDatabaseConfigEntity);

    }

    @Test
    public void delete(){
        FunctionalDatabaseConfigEntity functionalDatabaseConfig1 = new FunctionalDatabaseConfigEntity();
        functionalDatabaseConfig1.setDatabaseUsername("user1");
        functionalDatabaseConfig1.setDatabasePassword("password1");
        functionalDatabaseConfig1.setDatabaseSchema("schema1");
        functionalDatabaseConfig1.setActive(false);
        functionalDatabaseConfig1.setDatabaseUrl("url1");
        functionalDatabaseConfigRepository.save(functionalDatabaseConfig1);
        Long id = this.functionalDatabaseConfigRepository.findAll().get(0).getId();
        functionalDatabaseConfigRepository.delete(id);
        assertEquals(0, functionalDatabaseConfigRepository.findAll().size());
    }




}
