package com.arifacar.api.rest.user;

import com.arifacar.api.rest.common.BaseController;
import com.arifacar.common.service.user.UserService;
import com.arifacar.domain.model.common.EnumValue;
import com.arifacar.domain.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @RequestMapping("/test")
    public EnumValue findValueByCategoryIdAndValueId() {
        return new EnumValue("a", "b", 1L);
    }

    @Autowired
    private UserService userService;

    @Secured(ROLE_ADMIN)
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<User> findAll() {
        return userService.findAll();
    }


}
