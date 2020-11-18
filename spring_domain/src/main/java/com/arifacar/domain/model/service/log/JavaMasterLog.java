package com.arifacar.domain.model.service.log;

import com.arifacar.domain.model.constants.Constants;
import lombok.Data;
import org.apache.logging.log4j.ThreadContext;

import java.util.Date;

@Data
public class JavaMasterLog {

    private String trxId;
    private String endpoint;
    private Type type;
    private String convId;
    private Object log;
    private Date logDate = new Date();

    public JavaMasterLog(Type type, Object log) {
        this.endpoint = ThreadContext.get(Constants.TL_ENDPOINT_KEY);
        this.trxId = ThreadContext.get(Constants.TL_TRXID_KEY);
        this.convId = ThreadContext.get(Constants.REST_HEADER_CONVERSATIONID);
        this.type = type;
        this.log = log;
    }

    public enum Type {
        REST, LOG
    }
}
