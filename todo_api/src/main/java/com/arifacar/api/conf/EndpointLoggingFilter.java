package com.arifacar.api.conf;

import com.arifacar.api.util.RequestWrapper;
import com.arifacar.api.util.ResponseWrapper;
import com.arifacar.domain.model.constants.Constants;
import com.arifacar.domain.model.generic.GenericResponse;
import com.arifacar.domain.model.service.log.RestLog;
import com.arifacar.domain.util.PropertyUtil;
import com.arifacar.service.util.LoggerUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class EndpointLoggingFilter implements Filter {

    private final LoggerUtil loggerUtil;
    private final ObjectMapper objectMapper;
    private static final String INSTALLATION_ID = "installationid";

    private static Logger log = LogManager.getLogger(com.arifacar.api.conf.EndpointLoggingFilter.class.getName());

    @Autowired
    public EndpointLoggingFilter(LoggerUtil loggerUtil, ObjectMapper objectMapper, PropertyUtil propertyUtil) {
        this.loggerUtil = loggerUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request.getContentType().contains("multipart/form-data")){
            chain.doFilter(request, response);
            return;
        }

        updateEncodings(request, response);

        HttpServletRequest req = (HttpServletRequest) request;
        RequestWrapper requestWrapper = new RequestWrapper(req);
        ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);

        processRequest(requestWrapper);

        chain.doFilter(requestWrapper, responseWrapper);

        responseWrapper.flushBuffer();
        processResponse(req, responseWrapper);

    }

    private void updateEncodings(ServletRequest request, ServletResponse response) {
        try {
            if (response.getCharacterEncoding() == null) {
                response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            }

            if (request.getCharacterEncoding() == null) {
                request.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            }
        } catch (UnsupportedEncodingException e) {
            loggerUtil.logError(this.getClass(), Constants.APP_NAME + "updateEncodings", "encoding error", e);
        }
    }

    private void processRequest(RequestWrapper request) {
        ThreadContext.put("start-time", String.valueOf(System.currentTimeMillis()));
        ThreadContext.put(Constants.TL_TRXID_KEY, UUID.randomUUID().toString());
        ThreadContext.put(Constants.REST_HEADER_CONVERSATIONID, request.getHeader(Constants.REST_HEADER_CONVERSATIONID));
        ThreadContext.put(Constants.TL_ENDPOINT_KEY, request.getRequestURI());
        ThreadContext.put("request-entity", request.getBody());
    }

    private void processResponse(HttpServletRequest request, ResponseWrapper responseWrapper) {
        try {

            final String stTime = ThreadContext.get("start-time");

            if (StringUtils.isEmpty((stTime)))
                return;

            if ("true".equals(ThreadContext.get(Constants.TC_IGNORE_ENDPOINT))) {
                return;
            }

            RestLog restLog = getRestLog(request);

            if (!StringUtils.isEmpty(request.getQueryString())) {
                restLog.setQueryParams(objectMapper.writeValueAsString(request.getQueryString()));
            }

            restLog.setRequestBody(ThreadContext.get("request-entity"));

            byte[] copy = responseWrapper.getCopy();
            String responseStr = new String(copy, responseWrapper.getCharacterEncoding());

            setResponse(restLog, responseStr);
            restLog.setInstallationId(request.getHeader(INSTALLATION_ID));

            loggerUtil.logDebug(restLog);
        } catch (Exception e) {
            loggerUtil.logError(this.getClass(), Constants.APP_NAME + "filter",
                    "Something went wrong on rest conversion", e);
        } finally {
            ThreadContext.clearAll(); // clear the context on exit
        }
    }

    private GenericResponse getEntity(String responseStr) {
        GenericResponse entity = null;

        if (StringUtils.isEmpty(responseStr))
            return null;

        try {
            entity = objectMapper.readValue(responseStr, GenericResponse.class);
        } catch (IOException e) {
            loggerUtil.logError(this.getClass(), Constants.APP_NAME + "getEntity",
                    "error on response mapping:" + responseStr, e);
        }

        return entity;
    }

    private void setResponse(RestLog restLog, String responseStr) {
        try {
            if (!"true".equals(ThreadContext.get(Constants.TC_IGNORE_REST_RESPONSE)))
                restLog.setResponse(responseStr);
        } catch (Exception e) {
            loggerUtil.logError(this.getClass(), Constants.APP_NAME + "setResponse",
                    "IOException", e);
        }
    }

    private String getRequestBody(String maskingLabelJSON) {
        String requestBody = ThreadContext.get("request-entity");
        return requestBody;
    }

    private RestLog getRestLog(HttpServletRequest request)
            throws com.fasterxml.jackson.core.JsonProcessingException {
        RestLog restLog = new RestLog();
        String headers = objectMapper.writeValueAsString(getHeaders(request));
        restLog.setHeaders(headers);
        return restLog;
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> map = new HashMap<>();

        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String key = headerNames.nextElement();
                map.put(key, request.getHeader(key));
            }
        }
        return map;
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.debug(getClass() + " > initializing filter");
    }

    @Override
    public void destroy() {
        loggerUtil.logDebug(getClass(), Constants.APP_NAME + "destroy", "destroy filter");
    }

}
