package org.activiti.rest.mock;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MockExecutionEvent {

    private String executedServiceName;
}
