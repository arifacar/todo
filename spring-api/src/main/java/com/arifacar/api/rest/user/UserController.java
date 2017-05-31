package com.arifacar.api.rest.user;

import com.arifacar.api.rest.common.BaseController;
import com.arifacar.domain.model.common.EnumValue;
import com.arifacar.domain.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {


    @RequestMapping("/test")
    public EnumValue findValueByCategoryIdAndValueId() {
        return new EnumValue();
    }

    /*
    @Autowired
    private UserService userService;

    // @Secured(ROLE_ADMIN)
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<User> findAll() {
        return null; //userService.findAll();
    }
    */


}
