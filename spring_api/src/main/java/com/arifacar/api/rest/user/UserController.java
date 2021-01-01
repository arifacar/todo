package com.arifacar.api.rest.user;

import com.arifacar.api.rest.common.BaseController;
import com.arifacar.api.util.Validator;
import com.arifacar.domain.model.generic.GenericInfoResponse;
import com.arifacar.domain.model.user.User;
import com.arifacar.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        super(userService);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping(value = "/create")
    public GenericInfoResponse<User> create(@RequestBody User user, final HttpServletRequest request) {
        Validator.validateCreateUser(user, request);
        Validator.validateUserExists(user, userService);
        user.setPassword(bCryptPasswordEncoder.encode(DEFAULT_PASSWORD));
        User createdUser = userService.create(user);
        return getSuccessGenericInfoResponse(createdUser, "Welcome to Todo-App");
    }

    @PostMapping(value = "/update")
    public GenericInfoResponse<User> update(@RequestBody User user, final HttpServletRequest request) {
        Validator.validateUpdateUser(user);
        User updatedUser = userService.update(getCurrentUser(), user, null);
        return getSuccessGenericInfoResponse(updatedUser, "User information has been updated successfully.");
    }

    @PostMapping(value = "/updateWithProfile")
    public GenericInfoResponse<User> updateWithProfile(@RequestPart(value = "user", required = false) MultipartFile userAsFile,
                                                       @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        User user = userAsFile != null ? new ObjectMapper().readValue(userAsFile.getBytes(), User.class) : null;
        Validator.validateUpdateUser(user);
        User updatedUser = userService.update(getCurrentUser(), user, image);
        return getSuccessGenericInfoResponse(updatedUser, "User information has been updated successfully.");
    }

    @GetMapping(value = "/delete")
    public GenericInfoResponse<User> delete() {
        User currentUser = getCurrentUser();
        userService.delete(currentUser);
        return getSuccessGenericInfoResponse(null, "User information has been deleted successfully.");
    }

    @GetMapping(value = "/current")
    public GenericInfoResponse<User> current() {
        return getSuccessGenericInfoResponse(getCurrentUser(), null);
    }

    @PostMapping(value = "/exist")
    public GenericInfoResponse<Boolean> usernameExist(@RequestBody User user) {
        boolean exist = userService.existUserName(user);
        return getSuccessGenericInfoResponse(exist, "username is available");
    }

    @GetMapping(value = "/verifyEmail")
    public GenericInfoResponse<User> verifyEmail() {
        userService.verifyEmail(getCurrentUser());
        return getSuccessGenericInfoResponse(null, "Verification email has been sent. Please check your mailbox.");
    }

    // TODO: will be just admin service
    @GetMapping(value = "/all/{page}")
    public GenericInfoResponse<List<User>> findAll(@PathVariable("page") int page) {
        List<User> userList = userService.findAll(page);
        return getSuccessGenericInfoResponse(userList, "Page: " + page);
    }

}
