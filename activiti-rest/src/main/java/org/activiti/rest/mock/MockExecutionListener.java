package org.activiti.rest.mock;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class MockExecutionListener {

    @Autowired
    private EventBus eventBus;

    @PostConstruct
    public void init() {
        this.eventBus.register(this);
    }

    @Subscribe
    public void handleMockExecutionEvent(MockExecutionEvent mockExecutionEvent){
        log.info("Handling mock execution event : "+mockExecutionEvent.getExecutedServiceName());
    }
}
