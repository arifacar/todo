package com.arifacar.domain.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class Utils {

    private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";

    private static Environment environment;

    @Autowired
    public Utils(Environment environment) {
        com.arifacar.domain.util.Utils.environment = environment;
    }

    public static String getApplicationProperty(String key) {
        return environment.getProperty(key);
    }

    public static <T> T getApplicationProperty(String key, Class<T> targetType) {
        return environment.getProperty(key, targetType);
    }

    public static <E> void addAll(List<E> list, Collection<? extends E> c) {
        if (c != null) {
            list.addAll(c);
        }
    }
}
