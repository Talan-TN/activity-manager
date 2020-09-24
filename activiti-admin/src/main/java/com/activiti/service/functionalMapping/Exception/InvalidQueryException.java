package com.activiti.service.functionalMapping.Exception;

public class InvalidQueryException extends RuntimeException {

    public InvalidQueryException(String errorMessage){
        super(errorMessage);
    }
}
