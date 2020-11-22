package com.arifacar.api.security;

import com.arifacar.domain.model.constants.Constants;
import com.arifacar.domain.model.constants.ResponseCodes;
import com.arifacar.domain.model.constants.ResponseMessages;
import com.arifacar.domain.model.generic.GenericInfoResponse;
import com.arifacar.domain.model.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.annotation.Order;
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

@Order(2)
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final CustomUserDetailsService customUserDetailsService;

    private final String TOKEN_SECRET = "h4of9eh48vmg02nfu30v27yen295hfj65";

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
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
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
                                            FilterChain chain, Authentication auth) throws IOException {

        String userName = ((UserAdapter) auth.getPrincipal()).getUsername();
        UserAdapter userDto = (UserAdapter) customUserDetailsService.loadUserByUsername(userName);

        String token = Jwts.builder()
                .setSubject(String.valueOf(userDto.getUsername()))
                .setExpiration(new Date(System.currentTimeMillis() + Constants.ONE_HOUR_AS_MS))
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET)
                .compact();

        res.addHeader("Token", token);

        res.getWriter().write(new ObjectMapper().writeValueAsString(getUserGenericInfoResponse(
                userDto.getUser(), token)));

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed)
            throws IOException {
        GenericInfoResponse<UserAdapter> genericInfoResponse = new GenericInfoResponse<>();
        genericInfoResponse.setStatusCode(ResponseCodes.FAIL_WITH_POPUP);
        genericInfoResponse.setStatusDesc(ResponseMessages.WRONG_USERNAME_OR_PASS);
        genericInfoResponse.setDevelopmentDesc(failed.getMessage());
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
