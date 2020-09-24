package com.activiti.service.functionalMapping;


import com.activiti.helper.Couple;
import com.activiti.helper.JdbcTemplateHolder;
import com.activiti.helper.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Service
public class FunctionalEntityFetcherService {

    @Autowired
    private JdbcTemplateHolder jdbcTemplateHolder;

    public static final String PROCESS_ID_PLACEHOLDER = ":processId";

    public List<Triple> find(String sql,String processId){
        String processIdParam = "'"+processId+"'";
        String preparedSql = sql.replaceAll(PROCESS_ID_PLACEHOLDER,processIdParam);
        List<Triple> result = new ArrayList<>();
        this.jdbcTemplateHolder.getJdbcTemplate().query(preparedSql,resultSet -> {

           for(int columnIndex= 1 ; columnIndex <= resultSet.getMetaData().getColumnCount(); columnIndex++){
               String columnName = resultSet.getMetaData().getColumnName(columnIndex);
               Object columnValue = resultSet.getObject(columnIndex);
               String columnType = resultSet.getMetaData().getColumnTypeName(columnIndex);
               result.add(new Triple(columnName,columnValue,columnType));
           }
        });
        return result;
    }
}
