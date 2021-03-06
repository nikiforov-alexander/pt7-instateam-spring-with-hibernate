package com.techdegree.instateam.config;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;

@Configuration
public class DataConfig {
    @Autowired
    private Environment environment;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        Resource config =
                new ClassPathResource("hibernate.cfg.xml");
        LocalSessionFactoryBean localSessionFactoryBean =
                new LocalSessionFactoryBean();
        localSessionFactoryBean.setConfigLocation(config);
        localSessionFactoryBean.setPackagesToScan(
                environment.getProperty("instateam.entity.package")
        );
        localSessionFactoryBean.setDataSource(dataSource());
        return localSessionFactoryBean;
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();
        // Driver class name
        ds.setDriverClassName(
                environment.getProperty("instateam.db.driver")
        );
        ds.setUrl(
                environment.getProperty("instateam.db.url")
        );
        ds.setUsername(
                environment.getProperty("instateam.db.username")
        );
        ds.setPassword(
                environment.getProperty("instateam.db.password")
        );
        return ds;
    }
}
