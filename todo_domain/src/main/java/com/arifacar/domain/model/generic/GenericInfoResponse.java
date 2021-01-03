package com.arifacar.domain.model.generic;

import lombok.Data;

@Data
public class GenericInfoResponse<T> extends GenericResponse {

    private T response;

    public GenericInfoResponse(T response) {
        super();
    }

    public GenericInfoResponse() {
        super();
    }

}
