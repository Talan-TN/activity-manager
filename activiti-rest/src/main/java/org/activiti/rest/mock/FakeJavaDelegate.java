package org.activiti.rest.mock;

import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FakeJavaDelegate implements JavaDelegate {

    @Autowired
    private EventBus eventBus;

    @Override
    public void execute(DelegateExecution execution) {
        log.info("Fake Delegate executed : "+execution.getCurrentFlowElement().getName());
        eventBus.post(new MockExecutionEvent(execution.getCurrentFlowElement().getName()));
    }
}
