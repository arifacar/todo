package com.arifacar.api.rest.user;

import com.arifacar.api.rest.common.BaseController;
import com.arifacar.api.util.Validator;
import com.arifacar.domain.model.generic.GenericInfoResponse;
import com.arifacar.domain.model.user.User;
import com.arifacar.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserController(UserService userService, MessageSource messageSource, BCryptPasswordEncoder bCryptPasswordEncoder) {
        super(userService, messageSource);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping(value = "/create")
    public GenericInfoResponse<User> create(@RequestBody User user, final HttpServletRequest request) {
        Validator.validateCreateUser(user, request);
        Validator.validateUserExists(user, userService);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User createdUser = userService.create(user);
        return getSuccessGenericInfoResponse(createdUser, getMessage("user.create"));
    }

    @PostMapping(value = "/update")
    public GenericInfoResponse<User> update(@RequestBody User user) {
        Validator.validateUpdateUser(user);
        user.setId(getCurrentUser().getId());
        User updatedUser = userService.update(user);
        return getSuccessGenericInfoResponse(updatedUser, getMessage("user.update"));
    }

    @PostMapping(value = "/updateWithProfile")
    public GenericInfoResponse<User> updateWithProfile(@RequestPart(value = "user", required = false) MultipartFile userAsFile,
                                                       @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        User user = userAsFile != null ? new ObjectMapper().readValue(userAsFile.getBytes(), User.class) : null;
        Validator.validateUpdateUser(user);
        user.setId(getCurrentUser().getId());
        User updatedUser = userService.update(user, image);
        return getSuccessGenericInfoResponse(updatedUser, getMessage("user.update"));
    }

    @GetMapping(value = "/delete")
    public GenericInfoResponse<User> delete() {
        User currentUser = getCurrentUser();
        userService.delete(currentUser);
        return getSuccessGenericInfoResponse(null, getMessage("user.delete"));
    }

    @GetMapping(value = "/current")
    public GenericInfoResponse<User> current() {
        return getSuccessGenericInfoResponse(getCurrentUser(), null);
    }

    @PostMapping(value = "/exist")
    public GenericInfoResponse<Boolean> usernameExist(@RequestBody User user) {
        boolean exist = userService.existUserName(user);
        return getSuccessGenericInfoResponse(exist, getMessage("user.available"));
    }

    @GetMapping(value = "/verifyEmail")
    public GenericInfoResponse<User> verifyEmail() {
        userService.verifyEmail(getCurrentUser());
        return getSuccessGenericInfoResponse(null, getMessage("user.verify.email"));
    }

    // TODO: will be just admin service
    @GetMapping(value = "/all/{page}")
    public GenericInfoResponse<List<User>> findAll(@PathVariable("page") int page) {
        List<User> userList = userService.findAll(page);
        return getSuccessGenericInfoResponse(userList, "Page: " + page);
    }

}
