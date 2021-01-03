package com.arifacar.domain.model.service.log;

public interface BaseLog {

    default JavaMasterLog.Type getLogType() {
        if (this instanceof RestLog) {
            return JavaMasterLog.Type.REST;
        } else if (this instanceof GeneralLog) {
            return JavaMasterLog.Type.LOG;
        }
        return null;
    }
}
