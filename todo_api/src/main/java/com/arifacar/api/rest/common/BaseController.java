package com.arifacar.api.rest.common;

import com.arifacar.api.security.UserAdapter;
import com.arifacar.domain.model.constants.ResponseCodes;
import com.arifacar.domain.model.generic.GenericInfoResponse;
import com.arifacar.domain.model.user.User;
import com.arifacar.service.user.UserService;
import com.arifacar.service.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

abstract public class BaseController {

    protected final UserService userService;
    protected final MessageSource messageSource;

    @Autowired
    public BaseController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    protected <T> GenericInfoResponse<T> getSuccessGenericInfoResponse(T response, String desciption) {
        GenericInfoResponse<T> genericInfoResponse = new GenericInfoResponse<>();
        genericInfoResponse.setResponse(response);
        genericInfoResponse.setStatusCode(ResponseCodes.SUCCESS);
        genericInfoResponse.setStatusDesc(desciption);
        return genericInfoResponse;
    }

    protected String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserAdapter) {
            return ((UserAdapter) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    protected User getCurrentUser() {
        String username = getCurrentUsername();
        return userService.findByUsername(username);
    }

    protected String getMessage(String messageCode) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return messageSource.getMessage(messageCode, null, AppUtil.getLocale(attributes));
    }
}
