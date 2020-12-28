package com.arifacar.api.rest.common;

import com.arifacar.api.security.UserAdapter;
import com.arifacar.domain.model.user.User;
import com.arifacar.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

abstract public class BaseController {

    protected final String DEFAULT_PASSWORD = "password";

    protected UserService userService;

    @Autowired
    public BaseController(UserService userService) {
        this.userService = userService;
    }

    public String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserAdapter) {
            return ((UserAdapter) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public User getCurrentUser() {
        String username = getCurrentUsername();
        return userService.findByUsername(username);
    }
}
