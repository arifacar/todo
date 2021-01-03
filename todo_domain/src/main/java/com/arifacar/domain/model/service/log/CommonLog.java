package com.arifacar.domain.model.service.log;

import lombok.Data;
import org.apache.logging.log4j.Level;

@Data
public class CommonLog implements BaseLog {

    private String level;

    private String clazz;

    private String methodName;

    private String log;

    public CommonLog() {
    }

    public CommonLog(Level level, String clazz, String methodName, String log) {
        this.level = level.name();
        this.clazz = clazz;
        this.methodName = methodName;
        this.log = log;
    }
}