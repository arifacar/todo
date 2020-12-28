package com.arifacar.api.security;

import com.arifacar.domain.model.constants.ResponseCodes;
import com.arifacar.domain.model.generic.GenericResponse;
import com.arifacar.domain.model.user.LoginInfo;
import com.arifacar.domain.model.user.User;
import com.arifacar.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final UserService userService;

    public CustomLogoutSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String token = request.getHeader(JWTAuthorizationFilter.HEADER_STRING);
        String tokenString = Jwts.parser()
                .setSigningKey(JWTAuthorizationFilter.TOKEN_SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        userService.deleteLoginInfoByAuthToken(token);
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setStatusCode(ResponseCodes.SUCCESS_WITH_POPUP);
        genericResponse.setStatusDesc("Tekrar bekleriz :)");
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(new ObjectMapper().writeValueAsString(genericResponse));

    }
}