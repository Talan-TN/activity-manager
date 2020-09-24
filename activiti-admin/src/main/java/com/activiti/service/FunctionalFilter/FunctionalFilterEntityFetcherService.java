package com.activiti.service.FunctionalFilter;

import com.activiti.helper.Couple;
import com.activiti.helper.JdbcTemplateHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FunctionalFilterEntityFetcherService {

    @Autowired
    private JdbcTemplateHolder jdbcTemplateHolder;

    private final Logger log = LoggerFactory.getLogger(FunctionalFilterEntityFetcherService.class);


    public List<Couple> getTableColumnsNamesAndTypes(String sql){
        List<Couple> result = new ArrayList<>();
        JdbcTemplate jdbcTemplate = this.jdbcTemplateHolder.getJdbcTemplate();
        jdbcTemplate.setMaxRows(1);
        log.info("get max rows: " + jdbcTemplate.getMaxRows());
        this.jdbcTemplateHolder.getJdbcTemplate().query(sql,resultSet -> {

            for(int columnIndex= 1 ; columnIndex <= resultSet.getMetaData().getColumnCount(); columnIndex++){
                String columnName = resultSet.getMetaData().getColumnName(columnIndex);
                String columnDataType = resultSet.getMetaData().getColumnClassName(columnIndex);
                result.add(new Couple(columnName, columnDataType));
            }
        });
        jdbcTemplate.setMaxRows(-1);
        log.info("get max rows: " + jdbcTemplate.getMaxRows());
        return result;
    }

    public List<String> getSearchedDataByFilter(String sql, String processIdColumnName){
        List<String>  result = new ArrayList<>();
        List<Couple> columnsNames = this.getTableColumnsNamesAndTypes(sql);
        JdbcTemplate jdbcTemplate = this.jdbcTemplateHolder.getJdbcTemplate();
        this.jdbcTemplateHolder.getJdbcTemplate().query(sql, resultSet -> {

            for(int columnIndex= 1 ; columnIndex <= resultSet.getMetaData().getColumnCount(); columnIndex++){

                if ( resultSet.getMetaData().getColumnName(columnIndex).equals(processIdColumnName)){
                result.add(resultSet.getString(columnIndex));}
            }
        });
        return result;
    }

}
