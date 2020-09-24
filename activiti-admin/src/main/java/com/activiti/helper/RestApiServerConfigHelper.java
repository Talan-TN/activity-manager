package com.activiti.helper;

import com.activiti.domain.ServerConfig;
import com.activiti.repository.ServerConfigRepository;
import com.activiti.web.rest.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public final class RestApiServerConfigHelper {

    @Autowired
    protected ServerConfigRepository configRepository;

    public ServerConfig retrieveServerConfig() {
        List<ServerConfig> serverConfigs = configRepository.findAll();

        if (serverConfigs == null) {
            throw new BadRequestException("No server config found");
        }

        if (serverConfigs.size() > 1) {
            throw new BadRequestException("Only one server config allowed");
        }


        return serverConfigs.get(0);
    }
}
