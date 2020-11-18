package com.arifacar.domain.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PropertyUtil {

    private Environment environment;

    @Autowired
    public PropertyUtil(Environment environment) {
        this.environment = environment;
    }

    public String getApplicationProperty(String key) {
        return environment.getProperty(key);
    }

}
