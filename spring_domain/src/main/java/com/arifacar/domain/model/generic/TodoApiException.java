package com.arifacar.domain.model.generic;

import lombok.Data;
import org.springframework.util.StringUtils;

@SuppressWarnings("serial")
@Data
public class TodoApiException extends Exception {

    private int statusCode;

    private String statusDesc;

    private String developmentDesc;

    public TodoApiException(int statusCode) {
        this.statusCode = statusCode;
        this.statusDesc = "İşleminizi gerçekleştiremiyoruz.";
    }

    public TodoApiException(int statusCode, String statusDesc) {
        this.statusCode = statusCode;
        this.statusDesc = statusDesc;
    }

    public TodoApiException(int statusCode, String statusDesc, String developmentDesc) {
        this.statusCode = statusCode;
        this.statusDesc = StringUtils.isEmpty(statusDesc) ? "İşleminizi gerçekleştiremiyoruz." : statusDesc;
        this.developmentDesc = developmentDesc;
    }
}