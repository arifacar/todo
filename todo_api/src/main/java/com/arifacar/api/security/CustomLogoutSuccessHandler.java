package com.arifacar.api.security;

import com.arifacar.domain.model.constants.Constants;
import com.arifacar.domain.model.constants.ResponseCodes;
import com.arifacar.domain.model.generic.GenericResponse;
import com.arifacar.service.user.UserLoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final UserLoginService userLoginService;

    public CustomLogoutSuccessHandler(UserLoginService userLoginService) {
        this.userLoginService = userLoginService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String token = request.getHeader(JWTAuthorizationFilter.HEADER_STRING);

        userLoginService.deleteLoginInfoByAuthToken(token);

        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setStatusCode(ResponseCodes.SUCCESS_WITH_POPUP);
        genericResponse.setStatusDesc("Tekrar bekleriz :)");
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(Constants.APPLICATION_JSON_CHARSET_UTF_8);
        response.getWriter().write(new ObjectMapper().writeValueAsString(genericResponse));

    }
}