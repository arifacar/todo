package com.arifacar.api.security;

import com.arifacar.domain.model.user.LoginInfo;
import com.arifacar.domain.model.user.User;
import com.arifacar.service.user.UserLoginService;
import com.arifacar.service.user.UserService;
import com.arifacar.service.util.StringUtil;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

@EnableAsync
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    UserService userService;
    UserLoginService userLoginService;

    private static final Logger log = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

    public static final String TOKEN_SECRET = "h4of9eh48vmg02nfu30v27yen295hfj65";
    public static final Long TOKEN_EXPIRE = Long.parseLong("31556952000"); //Token will be expire in a year (365*24*60*60*1000)
    public static final String HEADER_STRING = "Token";
    public static final String DEVICE_INFO_HEADER = "User-Agent";

    public JWTAuthorizationFilter(AuthenticationManager authManager, UserService userService,
                                  UserLoginService userLoginService) {
        super(authManager);
        this.userService = userService;
        this.userLoginService = userLoginService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (StringUtil.isNothing(header)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            try {
                LoginInfo loginInfo = userLoginService.getUserLoginInfo(token);
                if (loginInfo != null) {
                    User user = userService.findById(loginInfo.getUserId());
                    return new UsernamePasswordAuthenticationToken(user.getUsername(), null, new ArrayList<>());
                }
                return null;
            } catch (ExpiredJwtException exception) {
                log.warn("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
            } catch (UnsupportedJwtException exception) {
                log.warn("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
            } catch (MalformedJwtException exception) {
                log.warn("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
            } catch (SignatureException exception) {
                log.warn("Request to parse JWT with invalid signature : {} failed : {}", token, exception.getMessage());
            } catch (IllegalArgumentException exception) {
                log.warn("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
            }
        }
        return null;
    }


    /**
     * @param request
     * @param username
     * @param userId
     * @param userLoginService
     * @return
     */
    public static String auth(HttpServletRequest request, String username, Long userId, UserLoginService userLoginService) {
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRE))
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET)
                .compact();
        userLoginService.saveLoginInfo(token, request.getHeader(JWTAuthorizationFilter.DEVICE_INFO_HEADER), userId);
        return token;
    }

}