package com.arifacar.domain.model.generic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GenericResponse implements Serializable {

    private int statusCode;

    private String statusDesc;

    private String developmentDesc;

    private String token;

    private String username;

    private String updateUrl;
}