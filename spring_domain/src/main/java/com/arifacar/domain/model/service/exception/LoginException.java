package com.arifacar.domain.model.service.exception;

import com.arifacar.domain.model.constants.ResponseCodes;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LoginException extends RuntimeException {

    private int statusCode;

    private String statusDesc;

    private String developmentDesc;

    public LoginException() {
        this.statusCode = ResponseCodes.FAIL_WITH_POPUP;
        this.statusDesc = "Lutfen tekrar giris yapiniz. ";
        this.developmentDesc = "Lutfen tekrar giris yapiniz. ";
    }
}



