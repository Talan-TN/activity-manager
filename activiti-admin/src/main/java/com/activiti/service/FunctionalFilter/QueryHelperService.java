package com.activiti.service.FunctionalFilter;

import com.activiti.helper.Triple;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class QueryHelperService {

    public StringBuilder addConditionToQuery(List<Triple> filtersData, String updatedQuery) {
        if (  updatedQuery.charAt(updatedQuery.length()-1) == ';') {
            updatedQuery = updatedQuery.substring(0,updatedQuery.length()-1);
        }
        StringBuilder finalUpdatedQuery = new StringBuilder(updatedQuery);
        if (!filtersData.isEmpty() && !(updatedQuery.toUpperCase().contains("WHERE"))){
            finalUpdatedQuery.append(" WHERE ");
        } else {
            finalUpdatedQuery.append(" AND ");
        }

        filtersData.forEach(data -> {
            if (data.getType() instanceof String){
                finalUpdatedQuery.append(data.getKey()).append("=").append("'").append(data.getValue()).append("' AND ");
            } else if (data.getType() instanceof Integer || data.getType() instanceof Double || data.getType() instanceof BigInteger || data.getType() instanceof Float){
                finalUpdatedQuery.append(data.getKey()).append("=").append(data.getValue()).append(" AND ");
            } else if (data.getType() == "Date"){
                finalUpdatedQuery.append(data.getKey()).append("=").append(data.getValue()).append(" AND ");
            }
        });
        if (finalUpdatedQuery.lastIndexOf("AND") == finalUpdatedQuery.length()- 4 ) {
            finalUpdatedQuery.delete(finalUpdatedQuery.length()-5, finalUpdatedQuery.length()-1);
        }
        return finalUpdatedQuery;
    }
}
