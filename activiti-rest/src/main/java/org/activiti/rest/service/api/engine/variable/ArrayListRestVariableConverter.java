package org.activiti.rest.service.api.engine.variable;

import org.activiti.engine.ActivitiIllegalArgumentException;

import java.util.ArrayList;

public class ArrayListRestVariableConverter implements RestVariableConverter {

    @Override
    public String getRestTypeName() {
        return "ArrayList";
    }

    @Override
    public Class<?> getVariableType() {
        return ArrayList.class;
    }

    @Override
    public Object getVariableValue(RestVariable result) {
        if (result.getValue() != null) {
            if (!(result.getValue() instanceof ArrayList)) {
                throw new ActivitiIllegalArgumentException("Converter can only convert integers");
            }
            return (ArrayList)result.getValue();
        }
        return null;
    }

    @Override
    public void convertVariableValue(Object variableValue, RestVariable result) {
        if (variableValue != null) {
            if (!(variableValue instanceof ArrayList)) {
                throw new ActivitiIllegalArgumentException("Converter can only convert integers");
            }
            result.setValue(variableValue);
        } else {
            result.setValue(null);
        }
    }

}
