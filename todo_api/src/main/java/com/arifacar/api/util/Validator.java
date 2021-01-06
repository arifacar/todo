package com.arifacar.api.util;

import com.arifacar.api.security.JWTAuthorizationFilter;
import com.arifacar.domain.model.user.User;
import com.arifacar.service.user.UserService;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class Validator {

    public static void validateCreateUser(User user, HttpServletRequest request) {
        Assert.isTrue(!StringUtils.isEmpty(request.getHeader(JWTAuthorizationFilter.DEVICE_INFO_HEADER)), "Device information must be sent with User-Agent.");
        Assert.isTrue(!StringUtils.isEmpty(user.getName()), "Please enter your name.");
        Assert.isTrue(!StringUtils.isEmpty(user.getSurname()), "Please enter your last name");
        Assert.isTrue(user.getName().matches("^[A-Za-zİıŞşÜüĞğÇçÖö].{0,24}$"), "Your name contains some characters that are not allowed.");
        Assert.isTrue(user.getSurname().matches("^[A-Za-zİıŞşÜüĞğÇçÖö].{0,24}$"), "Your surname contains some characters that are not allowed.");
        Assert.isTrue(!StringUtils.isEmpty(user.getUsername()), "Username cannot be blank.");
        Assert.isTrue(user.getUsername().matches("^[A-Za-z0-9]+(?:[\\.][A-Za-z0-9]+)*$"), "Username contains some characters that are not allowed");
        Assert.isTrue(!StringUtils.isEmpty(user.getEmail()), "Please enter e-mail address.");
        Assert.isTrue(user.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}+$"), "Please enter a valid e-mail address.");
    }

    public static void validateUserExists(User user, UserService userService){
        User checkUser = userService.findByUsernameOrEmail(user.getUsername(), user.getEmail());
        Assert.isNull(checkUser, "Username or e-mail is used by another member.");
    }

    public static void validateUpdateUser(User user) {
        Assert.isTrue(user != null, "Something went wrong");

        if (user.getUsername() != null) {
            Assert.isTrue(user.getUsername().matches("^[A-Za-z0-9]+(?:[\\.][A-Za-z0-9]+)*$"),
                    "Username contains some characters that are not allowed");
        }

        if (!StringUtils.isEmpty(user.getEmail())) {
            Assert.isTrue(user.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}+$"),
                    "Please enter a valid e-mail address.");
        }
    }
}
