package com.arifacar.api.rest.user;

import com.arifacar.api.rest.common.BaseController;
import com.arifacar.domain.model.common.EnumValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {


    @RequestMapping("/test")
    public EnumValue findValueByCategoryIdAndValueId() {
        return new EnumValue("a", "b", 1L);
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
