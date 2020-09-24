package com.activiti.domain.events;

import com.activiti.domain.functionalMapper.FunctionalDatabaseConfigEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DatabaseConfigEvent {

    private FunctionalDatabaseConfigEntity functionalDatabaseConfigEntity;
}
