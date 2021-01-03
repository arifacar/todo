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
    public static final long ONE_HOUR_AS_MS = 86400000000L;

    public static final String UPLOAD_PROFILE = "profile_pictures";
    public static final String UPLOAD_OTHER = "other_images";

    public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";
    public static final String UTF_8 = "UTF-8";

    public static final String KEY_DELIMETER = "_";

}
