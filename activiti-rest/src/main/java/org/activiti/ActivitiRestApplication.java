package org.activiti;

import com.google.common.eventbus.EventBus;
import org.activiti.engine.impl.history.HistoryLevel;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@ComponentScan
@SpringBootApplication
public class ActivitiRestApplication {



    public static void main(String args[]) {
        SpringApplication.run(ActivitiRestApplication.class, args);
    }

    @Autowired
    ApplicationContext applicationContext;


    @Value("${activiti.databaseSchemaUpdate}")
    private String databaseSchemaUpdate = "false";

    @Value("${activiti.databaseSchema}")
    private String activitiDatabaseSchema= "public";

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties activitiDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource activitiDataSource() {
        return activitiDataSourceProperties().initializeDataSourceBuilder().type(BasicDataSource.class).build();
    }


    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(
            PlatformTransactionManager transactionManager, SpringAsyncExecutor springAsyncExecutor) {
        SpringProcessEngineConfiguration engineConfiguration = new SpringProcessEngineConfiguration();
        engineConfiguration.setDataSource(activitiDataSource());
        engineConfiguration.setTransactionManager(transactionManager);
        engineConfiguration.setAsyncExecutor(springAsyncExecutor);
        engineConfiguration.setDatabaseSchemaUpdate(this.databaseSchemaUpdate);
        engineConfiguration.setAsyncExecutorActivate(false);
        engineConfiguration.setDatabaseSchema(activitiDatabaseSchema);
        engineConfiguration.setDbHistoryUsed(true);

        engineConfiguration.setHistoryLevel(HistoryLevel.FULL);

        return engineConfiguration;
    }

    @Bean
    public EventBus eventBus(){
        return new EventBus();
    }
}
