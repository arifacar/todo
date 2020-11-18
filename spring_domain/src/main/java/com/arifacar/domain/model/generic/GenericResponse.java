package com.arifacar.domain.model.generic;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class GenericResponse implements Serializable {

    private int statusCode;

    private String statusDesc;

    private String developmentDesc;

    private String token;

    private String username;

    private String updateUrl;
}