package com.arifacar.domain.model.constants;

import java.util.concurrent.TimeUnit;

public class CacheConstants {

    private CacheConstants() {
        throw new IllegalStateException(ErrorConstants.UTILITY_CLASS);
    }

    // CACHE KEYS
    public static final String USER_LIST = "USER_LIST";
    public static final String TODO_ITEM_LIST = "TODO_ITEM_LIST";

    public static final long DEFAULT_EXPIRE_TIME = 24;
    public static final TimeUnit DEFAULT_EXPIRE_TIME_UNIT = TimeUnit.HOURS;

}