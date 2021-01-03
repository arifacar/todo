package com.arifacar.service.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class AppUtil {

    public static String getHeaderAttribute(RequestAttributes attributes, String key) {
        if (attributes == null) {
            return null;
        }

        ServletRequestAttributes attr = (ServletRequestAttributes) attributes;
        HttpServletRequest request = attr.getRequest();

        return request.getHeader(key);
    }

    public static Locale getLocale(RequestAttributes attributes) {
        String language = getHeaderAttribute(attributes, "Accept-Language");

        Locale locale;
        if ("tr".equalsIgnoreCase(language)) {
            locale = new Locale("tr", "TR");
        } else {
            locale = Locale.ENGLISH;
        }
        return locale;
    }

}
