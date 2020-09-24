package com.activiti.service.functionalMapping.Exception;

public class InvalidSelectQueryException extends RuntimeException{

    public InvalidSelectQueryException(String errorMessage){
        super(errorMessage);
    }
}
