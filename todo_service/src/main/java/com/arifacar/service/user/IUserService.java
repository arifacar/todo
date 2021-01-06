package com.arifacar.service.user;

import com.arifacar.domain.model.user.User;
import com.arifacar.service.generic.ICrudService;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService<T> extends ICrudService<T> {

    User update(User user, MultipartFile image);

    boolean existUserName(User user);

    User findByUsername(String username);

    void verifyEmail(User currentUser);

    User findByUsernameOrEmail(String username, String email);
}