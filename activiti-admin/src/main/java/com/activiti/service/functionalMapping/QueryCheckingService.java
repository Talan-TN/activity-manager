package com.activiti.service.functionalMapping;


import com.activiti.service.functionalMapping.Exception.InvalidQueryException;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import org.springframework.stereotype.Service;

import java.io.StringReader;

@Service
public class QueryCheckingService {

    public boolean checkQueryIsSelect(String query) {

        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        try {
            Statement statement = parserManager.parse(new StringReader(query));
            if (statement instanceof Select) {
                return true;
            }
        } catch (JSQLParserException e) {
            throw new InvalidQueryException("Invalid Sql Query");
        }
        return false;
    }
}
