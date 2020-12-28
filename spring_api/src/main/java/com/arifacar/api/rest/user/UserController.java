package com.arifacar.api.rest.user;

import com.arifacar.api.rest.common.BaseController;
import com.arifacar.api.util.Validator;
import com.arifacar.domain.model.constants.ResponseCodes;
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

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        super(userService);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping(value = "/create")
    public GenericInfoResponse<User> create(@RequestBody User user, final HttpServletRequest request) {
        Validator.validateCreateUser(user, request);
        user.setPassword(bCryptPasswordEncoder.encode(DEFAULT_PASSWORD));
        GenericInfoResponse<User> genericInfoResponse = new GenericInfoResponse<>();
        genericInfoResponse.setResponse(userService.create(user));
        genericInfoResponse.setStatusCode(ResponseCodes.SUCCESS);
        genericInfoResponse.setStatusDesc("Welcome to Todo-App");
        return genericInfoResponse;
    }

    @PostMapping(value = "/update")
    public GenericInfoResponse<User> update(@RequestPart(value = "user", required = false) MultipartFile userAsFile,
                                            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        User user = userAsFile != null ? new ObjectMapper().readValue(userAsFile.getBytes(), User.class) : null;
        Validator.validateUpdateUser(user);
        GenericInfoResponse<User> response = new GenericInfoResponse<>();
        response.setResponse(userService.update(getCurrentUser(), user, image));
        response.setStatusCode(ResponseCodes.SUCCESS);
        response.setStatusDesc("User information has been updated successfully.");
        return response;
    }

    @GetMapping(value = "/delete")
    public GenericInfoResponse<User> delete() {
        User currentUser = getCurrentUser();
        userService.delete(currentUser);
        GenericInfoResponse<User> response = new GenericInfoResponse<>();
        response.setStatusCode(ResponseCodes.SUCCESS);
        response.setStatusDesc("User information has been updated successfully.");
        return response;
    }

    @GetMapping(value = "/current")
    public GenericInfoResponse<User> current() {
        GenericInfoResponse<User> response = new GenericInfoResponse<>();
        response.setStatusCode(ResponseCodes.SUCCESS);
        response.setResponse(getCurrentUser());
        return response;
    }

    @PostMapping(value = "/exist")
    public GenericInfoResponse<Boolean> usernameExist(@RequestBody User user) {
        GenericInfoResponse<Boolean> response = new GenericInfoResponse<>();
        response.setResponse(userService.existUserName(getCurrentUser(), user));
        response.setStatusCode(ResponseCodes.SUCCESS);
        return response;
    }

    @GetMapping(value = "/verifyEmail")
    public GenericInfoResponse<User> verifyEmail() {
        GenericInfoResponse<User> genericInfoResponse = new GenericInfoResponse<>();
        userService.verifyEmail(getCurrentUser());
        genericInfoResponse.setStatusCode(ResponseCodes.SUCCESS);
        genericInfoResponse.setStatusDesc("Verification email has been sent. Please check your mailbox.");
        return genericInfoResponse;
    }

    // TODO: will be just admin service
    @GetMapping(value = "/all/{page}")
    public GenericInfoResponse<List<User>> findAll(@PathVariable("page") int page) {
        GenericInfoResponse<List<User>> response = new GenericInfoResponse<>();
        response.setResponse(userService.findAll(page));
        response.setStatusCode(ResponseCodes.SUCCESS);
        return response;
    }

}
