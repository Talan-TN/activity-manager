package com.activiti.service.FunctionalFilter.Exception;

public class FilterConfigNotFoundException extends  RuntimeException{

    public FilterConfigNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
