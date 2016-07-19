package com.techdegree.instateam.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AppConfig {
    @Autowired
    private Environment mEnvironment;
}
