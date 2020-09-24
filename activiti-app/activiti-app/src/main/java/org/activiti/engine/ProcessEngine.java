//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.activiti.engine;

import org.activiti.form.api.FormRepositoryService;

public interface ProcessEngine {
    String VERSION = "7.0.0.0";

    String getName();

    void close();

    RepositoryService getRepositoryService();

    RuntimeService getRuntimeService();

    FormService getFormService();

    TaskService getTaskService();

    HistoryService getHistoryService();

    IdentityService getIdentityService();

    ManagementService getManagementService();

    DynamicBpmnService getDynamicBpmnService();

    ProcessEngineConfiguration getProcessEngineConfiguration();

    FormRepositoryService getFormEngineRepositoryService();

    org.activiti.form.api.FormService getFormEngineFormService();
}
