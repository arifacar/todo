package com.arifacar.service.common;

import com.arifacar.domain.model.constants.Constants;
import com.arifacar.domain.util.Utils;
import org.springframework.stereotype.Component;

@Component
abstract public class BaseService {

    public int getPageSizeCommon() {
        return Integer.parseInt(Utils.getApplicationProperty(Constants.PAGESIZE_COMMON));
    }

    public int getPageSizeCommon(String propertyKey) {
        return Integer.parseInt(Utils.getApplicationProperty(propertyKey));
    }
}
