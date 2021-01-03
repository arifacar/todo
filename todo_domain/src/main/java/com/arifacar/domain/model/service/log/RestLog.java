package com.arifacar.domain.model.service.log;

import lombok.Data;

@Data
public class RestLog implements BaseLog {

    private String headers;

    private String queryParams;

    private String requestBody;

    private String response;

    private String ip;

    private String installationId;

    private String platform;

    private String version;
}
