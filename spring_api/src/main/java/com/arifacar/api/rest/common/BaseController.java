package com.arifacar.api.rest.common;

import com.arifacar.api.security.UserAdapter;
import com.arifacar.domain.model.constants.ResponseCodes;
import com.arifacar.domain.model.generic.GenericInfoResponse;
import com.arifacar.domain.model.user.User;
import com.arifacar.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

abstract public class BaseController {

    protected final String DEFAULT_PASSWORD = "password";

    protected final UserService userService;

    @Autowired
    public BaseController(UserService userService) {
        this.userService = userService;
    }

    protected  <T> GenericInfoResponse<T> getSuccessGenericInfoResponse(T response, String desciption) {
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
}
