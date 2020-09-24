package org.activiti.test.services;

import com.activiti.helper.Triple;
import com.activiti.service.FunctionalFilter.QueryHelperService;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ApplicationTestConfiguration.class)
@Transactional
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:reset.sql")
})
public class QueryHelperServiceTest {

    @Autowired
    private QueryHelperService queryHelperService;

    @Test
    public void addConditionToQuery() {
        String query = "SELECT * from mytable";
        List<Triple> conditions = new ArrayList<>();
        Triple triple1 = new Triple<>();
        triple1.setKey("status");
        triple1.setValue("status1");
        triple1.setType("String");
        conditions.add(triple1);
        Triple triple2 = new Triple<>();
        triple2.setKey("description");
        triple2.setValue("description2");
        triple2.setType("Double");
        conditions.add(triple2);
        String result = this.queryHelperService.addConditionToQuery(conditions, query).toString();
        assertTrue(result.contains("WHERE"));
    }

    @Test
    public void ConditionToQuery() {
        String query = "SELECT * from mytable where id = 10 ";
        List<Triple> conditions = new ArrayList<>();
        Triple triple1 = new Triple<>();
        triple1.setKey("status");
        triple1.setValue("status1");
        triple1.setType("String");
        conditions.add(triple1);
        Triple triple2 = new Triple<>();
        triple2.setKey("description");
        triple2.setValue("description2");
        triple2.setType("Double");
        conditions.add(triple2);
        String result = this.queryHelperService.addConditionToQuery(conditions, query).toString();
        assertTrue(result.toUpperCase().contains("WHERE"));
        assertTrue(result.toUpperCase().contains("AND"));
    }

    @Test
    public void QueryIsValid() {
        String query = "SELECT * from mytable where id = 10 ";
        List<Triple> conditions = new ArrayList<>();
        Triple triple1 = new Triple<>();
        triple1.setKey("status");
        triple1.setValue("status1");
        triple1.setType("String");
        conditions.add(triple1);
        Triple triple2 = new Triple<>();
        triple2.setKey("description");
        triple2.setValue("description2");
        triple2.setType("Double");
        conditions.add(triple2);
        String result = this.queryHelperService.addConditionToQuery(conditions, query).toString();
        String expected = "select * from mytable where id = 10  and status='status1' and description='description2'";
        System.out.println(result.toLowerCase().equals(expected));
        assertTrue(result.toLowerCase().trim().equals(expected));

    }


}
