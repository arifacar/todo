package com.arifacar.service.common;

import com.arifacar.domain.util.AppConfig;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
abstract public class BaseService {

    protected int getCommonPageSize() {
        return AppConfig.getCommonPageSize();
    }

    protected PageRequest getPageRequest(int page) {
        return PageRequest.of(page - 1, getCommonPageSize());
    }

}
