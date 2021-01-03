package com.arifacar.domain.util;

public class AppConfig {

    public static final String PAGESIZE_COMMON = "pagesize.common";

    public static int getCommonPageSize() {
        return Integer.parseInt(PropertyUtil.getApplicationProperty(PAGESIZE_COMMON));
    }
}
