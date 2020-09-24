package org.activiti.rest.mock;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FakeTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("Executing task listener "+delegateTask.getExecution().getCurrentFlowElement().getName());
    }
}
