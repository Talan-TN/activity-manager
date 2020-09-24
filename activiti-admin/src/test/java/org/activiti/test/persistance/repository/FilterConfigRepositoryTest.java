package org.activiti.test.persistance.repository;

import com.activiti.domain.FilterConfigEntity;
import com.activiti.repository.FilterConfigRepository;
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

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ApplicationTestConfiguration.class)
@Transactional
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:reset.sql")
})
public class FilterConfigRepositoryTest {

    @Autowired
    private FilterConfigRepository filterConfigRepository;

    @Test
    public void findByProcessDefinitionKey() {
        FilterConfigEntity filterConfigEntity = new FilterConfigEntity();
        filterConfigEntity.setId((long) 1);
        filterConfigEntity.setProcessIdColumnName("process_id");
        filterConfigEntity.setSqlQuery("select * from mytable");
        filterConfigEntity.setProcessDefinitionKey("ATTESTATION");
        filterConfigEntity.setFilterName("Attestation filter");

        this.filterConfigRepository.save(filterConfigEntity);
        Optional<FilterConfigEntity> result = this.filterConfigRepository.findByProcessDefinitionKey("ATTESTATION");
        assertNotNull(result);
    }

    @Test
    public void findAll(){
        FilterConfigEntity filterConfigEntity = new FilterConfigEntity();
        filterConfigEntity.setId((long) 1);
        filterConfigEntity.setProcessIdColumnName("process_id");
        filterConfigEntity.setSqlQuery("select * from mytable");
        filterConfigEntity.setProcessDefinitionKey("ATTESTATION");
        filterConfigEntity.setFilterName("Attestation filter");
        this.filterConfigRepository.save(filterConfigEntity);
        filterConfigEntity.setId((long) 2);
        this.filterConfigRepository.save(filterConfigEntity);
        int result = this.filterConfigRepository.findAll().size();
        assertEquals(2, result);
    }

    @Test
    public void findOne(){
        FilterConfigEntity filterConfigEntity = new FilterConfigEntity();
        filterConfigEntity.setProcessIdColumnName("process_id");
        filterConfigEntity.setSqlQuery("select * from mytable");
        filterConfigEntity.setProcessDefinitionKey("ATTESTATION");
        filterConfigEntity.setFilterName("Attestation filter");
        this.filterConfigRepository.save(filterConfigEntity);
        long id = this.filterConfigRepository.findAll().get(0).getId();
        FilterConfigEntity result = this.filterConfigRepository.findOne(id);
        assertNotNull(result);
    }

    @Test
    public void delete(){
        FilterConfigEntity filterConfigEntity = new FilterConfigEntity();
        filterConfigEntity.setProcessIdColumnName("process_id");
        filterConfigEntity.setSqlQuery("select * from mytable");
        filterConfigEntity.setProcessDefinitionKey("ATTESTATION");
        filterConfigEntity.setFilterName("Attestation filter");
        this.filterConfigRepository.save(filterConfigEntity);
        this.filterConfigRepository.delete(this.filterConfigRepository.findAll().get(0).getId());
        int result = this.filterConfigRepository.findAll().size();
        assertEquals(0, result);

    }


}
