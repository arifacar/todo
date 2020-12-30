package com.arifacar.service.common;

import com.arifacar.domain.util.AppConfig;
import org.springframework.stereotype.Component;

@Component
abstract public class BaseService {

    public int getCommonPageSize() {
        return AppConfig.getCommonPageSize();
    }

}
