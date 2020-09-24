package com.activiti.service;

import com.activiti.helper.JdbcTemplateHolder;
import com.activiti.helper.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AssigneeService {

    private static final String ASSIGNEES_QUERY = "select assignee_id ,  firstname, lastname from public.assignees";

    @Autowired
    private JdbcTemplateHolder jdbcTemplateHolder;

    public List<Triple> getAssignees() {

        JdbcTemplate jdbcTemplate = this.jdbcTemplateHolder.getJdbcTemplate();
        List<Triple>  assignees = new ArrayList<>();
        this.jdbcTemplateHolder.getJdbcTemplate().query(ASSIGNEES_QUERY,resultSet -> {

            while (resultSet.next()){
                long assignee_id = resultSet.getLong("assignee_id");
                String columnDataType = resultSet.getMetaData().getColumnClassName(1);
                String fullName = resultSet.getString(("lastname")).concat(" ").concat(resultSet.getString("firstname"));
                assignees.add(new Triple(assignee_id, fullName, columnDataType));
            }
        });
        return assignees;

    }
}
