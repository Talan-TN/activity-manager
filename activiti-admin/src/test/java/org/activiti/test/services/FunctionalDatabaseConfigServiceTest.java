package org.activiti.test.services;

import com.activiti.service.functionalMapping.FunctionalDatabaseConfigService;
import com.activiti.web.rest.dto.FunctionalDatabaseConfigDTO;
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
public class FunctionalDatabaseConfigServiceTest {

    @Autowired
    private FunctionalDatabaseConfigService functionalDatabaseConfigService;

    @Test
    public void  save(){
        FunctionalDatabaseConfigDTO functionalDatabaseConfigDTO = new FunctionalDatabaseConfigDTO();
        functionalDatabaseConfigDTO.setDatabaseUrl("url1");
        functionalDatabaseConfigDTO.setDatabaseUsername("user1");
        functionalDatabaseConfigDTO.setDatabasePassword("password1");
        functionalDatabaseConfigDTO.setDatabaseSchema("schema1");
        functionalDatabaseConfigDTO.setActive(false);

        this.functionalDatabaseConfigService.save(functionalDatabaseConfigDTO);

        assertEquals(1, this.functionalDatabaseConfigService.findAll().size());
    }

    @Test
    public void findOne(){
        FunctionalDatabaseConfigDTO functionalDatabaseConfigDTO = new FunctionalDatabaseConfigDTO();
        functionalDatabaseConfigDTO.setDatabaseUrl("url1");
        functionalDatabaseConfigDTO.setDatabaseUsername("user1");
        functionalDatabaseConfigDTO.setDatabasePassword("password1");
        functionalDatabaseConfigDTO.setDatabaseSchema("schema1");
        functionalDatabaseConfigDTO.setActive(true);
        functionalDatabaseConfigService.save(functionalDatabaseConfigDTO);

        boolean active = this.functionalDatabaseConfigService.findAll().get(0).isActive();
        assertNotNull(this.functionalDatabaseConfigService.findActive(active));
    }

    @Test
    public void findAll(){
        FunctionalDatabaseConfigDTO functionalDatabaseConfigDTO = new FunctionalDatabaseConfigDTO();
        functionalDatabaseConfigDTO.setDatabaseUrl("url1");
        functionalDatabaseConfigDTO.setDatabaseUsername("user1");
        functionalDatabaseConfigDTO.setDatabasePassword("password1");
        functionalDatabaseConfigDTO.setDatabaseSchema("schema1");
        functionalDatabaseConfigDTO.setActive(false);
        functionalDatabaseConfigService.save(functionalDatabaseConfigDTO);

        assertEquals(1, this.functionalDatabaseConfigService.findAll().size());

    }

    @Test
    public void update(){

        FunctionalDatabaseConfigDTO functionalDatabaseConfigDTO = new FunctionalDatabaseConfigDTO();
        functionalDatabaseConfigDTO.setDatabaseUrl("url1");
        functionalDatabaseConfigDTO.setDatabaseUsername("user1");
        functionalDatabaseConfigDTO.setDatabasePassword("password1");
        functionalDatabaseConfigDTO.setDatabaseSchema("schema1");
        functionalDatabaseConfigDTO.setActive(false);
        functionalDatabaseConfigService.save(functionalDatabaseConfigDTO);
        Long id = this.functionalDatabaseConfigService.findAll().get(0).getId();
        functionalDatabaseConfigDTO.setId(id);

        this.functionalDatabaseConfigService.update(functionalDatabaseConfigDTO);
        assertNotNull(this.functionalDatabaseConfigService.findAll().get(0).getId());

    }

    @Test
    public void delete(){
        FunctionalDatabaseConfigDTO functionalDatabaseConfigDTO = new FunctionalDatabaseConfigDTO();
        functionalDatabaseConfigDTO.setDatabaseUrl("url1");
        functionalDatabaseConfigDTO.setDatabaseUsername("user1");
        functionalDatabaseConfigDTO.setDatabasePassword("password1");
        functionalDatabaseConfigDTO.setDatabaseSchema("schema1");
        functionalDatabaseConfigDTO.setActive(false);
        functionalDatabaseConfigService.save(functionalDatabaseConfigDTO);
        Long id = this.functionalDatabaseConfigService.findAll().get(0).getId();
        this.functionalDatabaseConfigService.deleteFunctionalDatabaseConfig(id);
        assertEquals(0, this.functionalDatabaseConfigService.findAll().size());
    }
}
