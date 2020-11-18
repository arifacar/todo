package com.arifacar.domain.model.constants;


public class Constants {

    private Constants() {
        throw new IllegalStateException(ErrorConstants.UTILITY_CLASS);
    }

    public static final String APP_NAME = "JavaMaster.";
    public static final String JAVAMASTER_PACKAGE = "com.arifacar";

    public static final String TL_TRXID_KEY = "trxId";
    public static final String TL_INSTALL_ID = "installationId";
    public static final String TL_ENDPOINT_KEY = "endpoint";
    public static final String REST_HEADER_CONVERSATIONID = "conversationId";
    public static final String REST_HEADER_XFORWARDEDFOR = "x-forwarded-for";

    public static final String TC_IGNORE_REST_RESPONSE = "javamaster-ignore-rest-response";
    public static final String TC_IGNORE_WS_RESPONSE = "javamaster-ignore-ws-response";
    public static final String TC_IGNORE_ENDPOINT = "javamaster-ignore-endpoint";
    public static final String TC_IGNORE_MASKING_WS = "javamaster-ignore-masking-ws";
    public static final String TC_IGNORE_MASKING_REST = "javamaster-ignore-masking-rest";
}
