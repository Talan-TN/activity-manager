package com.activiti.conf;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {

    private static final String PROTOCOL = "AJP/1.3";

    @Value("${tomcat.ajp.port}")
    private int ajpPort;

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        Connector ajpConnector = new Connector(PROTOCOL);
        ajpConnector.setPort(ajpPort);
        tomcat.addAdditionalTomcatConnectors(ajpConnector);
        return tomcat;
    }
}
