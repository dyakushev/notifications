package ru.bia.traffic.notifications.event.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import ru.bia.traffic.notifications.event.component.SchedulerJobFactory;

import javax.sql.DataSource;
import java.util.Properties;


@Configuration
public class QuartzConfig {

    private DataSource dataSource;

    private ApplicationContext applicationContext;

    private QuartzProperties quartzProperties;

    @Autowired
    public void setDataSource(@Qualifier("quartzDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void setQuartzProperties(QuartzProperties quartzProperties) {
        this.quartzProperties = quartzProperties;
    }

    /**
     * create scheduler factory
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {

        SchedulerJobFactory jobFactory = new SchedulerJobFactory();
        jobFactory.setApplicationContext(applicationContext);

        Properties properties = new Properties();
        properties.putAll(quartzProperties.getProperties());

        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setOverwriteExistingJobs(true);
        factory.setDataSource(dataSource);
        factory.setQuartzProperties(properties);
        factory.setJobFactory(jobFactory);
        return factory;
    }

}
