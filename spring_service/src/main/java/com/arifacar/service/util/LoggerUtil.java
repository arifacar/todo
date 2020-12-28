package com.arifacar.service.util;


import com.arifacar.domain.model.service.log.BaseLog;
import com.arifacar.domain.model.service.log.RestLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class LoggerUtil {

    private static Logger logger = LogManager.getLogger(LoggerUtil.class.getName());

    public <T> void logTrace(Class<T> c, String methodName, String log) {
        logger.trace(c.getName() + " > " + methodName + " > " + log);
    }

    public <T> void logDebug(Class<T> c, String methodName, String log) {
        logger.debug(c.getName() + " > " + methodName + " > " + log);
    }

    public void logDebug(RestLog log) {
        logger.debug(log);
    }

    public <T> void logDebug(Class<T> c, String methodName, Object log) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            logger.debug(c.getName() + " > " + methodName + " > " + mapper.writeValueAsString(log));
        } catch (JsonProcessingException e) {
            logger.debug(c.getName() + " > " + methodName + " > " + log.toString());
        }
    }

    public <T> void logInfo(Class<T> c, String methodName, String log) {
        logger.info(c.getName() + " > " + methodName + " > " + log);
    }

    public <T> void logWarn(Class<T> c, String methodName, String log) {
        logger.warn(c.getName() + " > " + methodName + " > " + log);
    }

    public <T> void logError(Class<T> c, String methodName, String log, Throwable t) {
        logger.error(c.getName() + " > " + methodName + " > " + log, t);
    }

    public <T> void logFatal(Class<T> c, String methodName, String log, Throwable t) {
        logger.fatal(c.getName() + " > " + methodName + " > " + log, t);
    }

    public void addLog(BaseLog log) {
        // logRepository.addLog(log);
    }
}
