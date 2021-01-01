package com.arifacar.api.security;

import com.arifacar.domain.model.constants.Constants;
import com.arifacar.domain.model.constants.ResponseCodes;
import com.arifacar.domain.model.constants.ResponseMessages;
import com.arifacar.domain.model.generic.GenericInfoResponse;
import com.arifacar.domain.model.security.LoginRequest;
import com.arifacar.domain.model.user.User;
import com.arifacar.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

// @Order(2)
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final CustomUserDetailsService customUserDetailsService;

    private UserService userService;
    private final String TOKEN_SECRET = "h4of9eh48vmg02nfu30v27yen295hfj65";

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                CustomUserDetailsService customUserDetailsService, UserService userService) {
        this.customUserDetailsService = customUserDetailsService;
        this.userService = userService;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {
            LoginRequest creds = new ObjectMapper().readValue(req.getInputStream(), LoginRequest.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {

        String userName = ((UserAdapter) authResult.getPrincipal()).getUsername();
        UserAdapter userDto = (UserAdapter) customUserDetailsService.loadUserByUsername(userName);

        String token = Jwts.builder()
                .setSubject(userDto.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + Constants.ONE_HOUR_AS_MS))
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET)
                .compact();

        response.addHeader("Token", token);

        userService.saveLoginInfo(token, request.getHeader(JWTAuthorizationFilter.DEVICE_INFO_HEADER), userDto.getUser().getId());
        response.setContentType(Constants.APPLICATION_JSON_CHARSET_UTF_8);
        response.getWriter().write(new ObjectMapper().writeValueAsString(getUserGenericInfoResponse(userDto.getUser(), token)));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed)
            throws IOException {
        GenericInfoResponse<UserAdapter> genericInfoResponse = new GenericInfoResponse<>();
        genericInfoResponse.setStatusCode(ResponseCodes.FAIL_WITH_POPUP);
        genericInfoResponse.setStatusDesc(ResponseMessages.WRONG_USERNAME_OR_PASS);
        genericInfoResponse.setDevelopmentDesc(failed.getMessage());
        response.setContentType(Constants.APPLICATION_JSON_CHARSET_UTF_8);
        response.getWriter().write(new ObjectMapper().writeValueAsString(genericInfoResponse));
    }

    private GenericInfoResponse<User> getUserGenericInfoResponse(User user, String token) {
        GenericInfoResponse<User> genericInfoResponse = new GenericInfoResponse<>();
        genericInfoResponse.setStatusCode(ResponseCodes.SUCCESS);
        genericInfoResponse.setToken(token);
        genericInfoResponse.setResponse(user);
        return genericInfoResponse;
    }
}
