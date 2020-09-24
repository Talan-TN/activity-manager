package org.activiti.rest.mock;


import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ActivitiDelegateMocker {

    @Autowired
    private AnnotationConfigEmbeddedWebApplicationContext applicationContext;

    @Autowired
    private FakeJavaDelegate fakeJavaDelegate;

    @Autowired
    private FakeTaskListener fakeTaskListener;

    @Autowired
    private RepositoryService repositoryService;


    public void mockProcessDefinitionServiceTasksAndListeners(String processDefinitionId){
        List<FlowElement> serviceTasks = repositoryService.getBpmnModel(processDefinitionId).getMainProcess().getFlowElements().stream().filter(elem -> elem instanceof ServiceTask).collect(Collectors.toList());
        List<FlowElement> userTasks = repositoryService.getBpmnModel(processDefinitionId).getMainProcess().getFlowElements().stream().filter(elem -> elem instanceof UserTask).collect(Collectors.toList());
        this.mockTaskListeners(userTasks);
        this.mockServiceTasks(serviceTasks);

    }

    private void mockServiceTasks(List<FlowElement> serviceTasks) {
        serviceTasks.stream().forEach(serviceTask -> {
            String serviceName =  ((ServiceTask) serviceTask).getImplementation().replace("${","").replace("}","");
            if(!applicationContext.containsBean(serviceName)){
                log.info("Mocking JavaDelegate "+serviceName);
                applicationContext.getBeanFactory().registerSingleton(serviceName,fakeJavaDelegate);
            }
        });
    }

    private void mockTaskListeners(List<FlowElement> userTasks) {
        userTasks.stream().map(userTask -> ((UserTask)userTask).getTaskListeners()).filter(Objects::nonNull).flatMap(List::stream).forEach(taskListener -> {
            String taskListenerName =  taskListener.getImplementation().replace("${","").replace("}","");
            if(!applicationContext.containsBean(taskListenerName)){
                log.info("Mocking taskListener "+taskListenerName);
                applicationContext.getBeanFactory().registerSingleton(taskListenerName,fakeTaskListener);
            }
        });
    }


}
