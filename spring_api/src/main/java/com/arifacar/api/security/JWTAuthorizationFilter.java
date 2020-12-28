package com.arifacar.api.security;

import com.arifacar.domain.model.service.exception.LoginException;
import com.arifacar.domain.model.user.LoginInfo;
import com.arifacar.domain.model.user.User;
import com.arifacar.service.user.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

@EnableAsync
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    UserService userService;

    public static final String TOKEN_SECRET = "h4of9eh48vmg02nfu30v27yen295hfj65";
    //Token will be expire in a year (365*24*60*60*1000)
    public static final Long TOKEN_EXPIRE = Long.parseLong("31556952000");

    public static final String NOT_SAVED_USER = "&NOT_SAVED_USER&";

    public static final String HEADER_STRING = "Token";
    public static final String HEADER_STRING_FIREBASE = "firebaseToken";
    public static final String TOKEN_SEPARATOR = "/---/";
    public static final String DEVICE_INFO_HEADER = "User-Agent";

    private static final Logger log = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

    public JWTAuthorizationFilter(AuthenticationManager authManager, UserService userService) {
        super(authManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null) {
            if (chain == null){
                throw new LoginException();
            }
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
                String tokenString = Jwts.parser()
                        .setSigningKey(TOKEN_SECRET)
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject();

                String [] tokenData = tokenString.split(TOKEN_SEPARATOR);

                /*if(tokenData.length != 3){
                    throw new MalformedJwtException("Token Data Corrupted!");
                }*/

                LoginInfo loginInfo = userService.getUserLoginInfo(token);
                if (loginInfo != null) {
                    User user = userService.findUserById(loginInfo.getUserId());
                    setFirebaseToken(user, request);
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

    @Async
    void setFirebaseToken(User user, HttpServletRequest request) {
        if (user != null) {
            String firebaseToken = request.getHeader(HEADER_STRING_FIREBASE);
            if (StringUtils.isEmpty(firebaseToken)){
                logger.error("Empty Firebase Token, Device:" + request.getHeader(HttpHeaders.USER_AGENT) + "--- User Id: " + user.getId() + " Username: " + user.getUsername());
            }
        }
    }

    /**
     *
     * @param request       HttpRequest Object
     * @param id            Used in Token
     * @param userId        Persisted User Id
     * @param userService   UserService object to save token
     */
    public static String auth(HttpServletRequest request, String id,  Long userId, UserService userService){
        String token = Jwts.builder()
                .setSubject(id +
                        JWTAuthorizationFilter.TOKEN_SEPARATOR+
                        request.getHeader(DEVICE_INFO_HEADER))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRE))
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET)
                .compact();
        LoginInfo loginInfo = saveLoginInfo(token,
                request.getHeader(JWTAuthorizationFilter.DEVICE_INFO_HEADER),
                userId, userService);
        userService.saveUserLoginInfo(loginInfo);
        return token;
    }

    private static LoginInfo saveLoginInfo(String token, String deviceInfo, Long userId, UserService userService) {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setAuthToken(token);
        loginInfo.setDate(new Timestamp(System.currentTimeMillis()));
        loginInfo.setDeviceInfo(deviceInfo);
        loginInfo.setUserId(userId);
        return userService.saveUserLoginInfo(loginInfo);
    }
}