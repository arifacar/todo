package com.arifacar.api.security;

import com.arifacar.domain.model.constants.ResponseCodes;
import com.arifacar.domain.model.generic.GenericResponse;
import com.arifacar.domain.repository.user.LoginInfoRepository;
import com.arifacar.domain.repository.user.UserRepository;
import com.arifacar.service.user.UserService;
import com.arifacar.service.util.LoggerUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = CustomUserDetailsService.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    LoginInfoRepository loginInfoRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    UserService userService;

    @Autowired
    LoggerUtil loggerUtil;

    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordencoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordencoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) ->
                        response.getOutputStream().println(getGenericResponseStr(response, accessDeniedException)))
                .authenticationEntryPoint((request, response, authException) ->
                        response.getOutputStream().println(getGenericResponseStr(response, authException)))
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .antMatchers("/user/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .formLogin().disable()
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), userService))
                .addFilter(getAuthenticationFilter())
                .logout()
                .clearAuthentication(false)
                .logoutSuccessHandler(new CustomLogoutSuccessHandler(userService));
    }

    private String getGenericResponseStr(HttpServletResponse response, RuntimeException authException) throws IOException {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setStatusCode(ResponseCodes.FAIL_WITH_POPUP);
        genericResponse.setStatusDesc("Lutfen tekrar giris yapiniz.");
        genericResponse.setDevelopmentDesc(authException.getLocalizedMessage());
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), genericResponse);
        return mapper.writeValueAsString(genericResponse);
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordencoder());
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        return new AuthenticationFilter(authenticationManager(), userDetailsService);
    }
}
