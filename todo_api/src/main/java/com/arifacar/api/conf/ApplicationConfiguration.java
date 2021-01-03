package com.arifacar.api.conf;

import com.arifacar.domain.model.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class ApplicationConfiguration {

    public static final String MESSAGES = "messages";

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames(MESSAGES);
        messageSource.setDefaultEncoding(Constants.UTF_8);
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }
}