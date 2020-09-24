package org.activiti.test.services;

import com.activiti.service.functionalMapping.Exception.InvalidQueryException;
import com.activiti.service.functionalMapping.Exception.InvalidSelectQueryException;
import com.activiti.service.functionalMapping.FunctionalMappingService;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.Select;
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

import java.io.StringReader;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ApplicationTestConfiguration.class)
@Transactional
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:reset.sql")
})
public class QueryCheckingServiceTest {

    @Autowired
    FunctionalMappingService functionalMappingService;

    @Test(expected = InvalidSelectQueryException.class)
    public void checkQueryIsSelect() throws JSQLParserException {
        String query = "Delete from test;";
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        net.sf.jsqlparser.statement.Statement statement = null;
        statement = parserManager.parse(new StringReader(query));
        if (statement instanceof Select == false) throw new InvalidSelectQueryException("Invalid SQL select query");
    }
    @Test(expected = InvalidQueryException.class)
    public void checkQueryIsValid() {

        String query = "Delete * from test;";
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        net.sf.jsqlparser.statement.Statement statement = null;
        try {
            statement = parserManager.parse(new StringReader(query));
        } catch (JSQLParserException e) {
            throw  new InvalidQueryException("Invalid Sql query");
        }
    }

    @Test
    public void validSelectQuery(){

        String query = "Select * from test where id =5;";
        boolean isSelect = false;
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        net.sf.jsqlparser.statement.Statement statement = null;
        try {
            statement = parserManager.parse(new StringReader(query));
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }

        if (statement instanceof Select) isSelect = true;
        assertTrue(isSelect);

    }

}
