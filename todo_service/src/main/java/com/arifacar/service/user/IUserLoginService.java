package com.arifacar.service.user;

import com.arifacar.domain.model.user.LoginInfo;
import org.springframework.transaction.annotation.Transactional;

public interface IUserLoginService {

    LoginInfo getUserLoginInfo(String authToken);

    @Transactional
    void deleteLoginInfoByAuthToken(String token);

    @Transactional
    LoginInfo saveLoginInfo(String token, String deviceInfo, Long userId);

    void deleteAllByUserId(Long id);
}