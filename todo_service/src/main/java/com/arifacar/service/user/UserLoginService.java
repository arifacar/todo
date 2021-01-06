package com.arifacar.service.user;

import com.arifacar.domain.model.user.LoginInfo;
import com.arifacar.domain.repository.user.LoginInfoRepository;
import com.arifacar.service.common.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class UserLoginService extends BaseService implements IUserLoginService {

    private final LoginInfoRepository loginInfoRepository;

    @Autowired
    public UserLoginService(LoginInfoRepository loginInfoRepository) {
        this.loginInfoRepository = loginInfoRepository;
    }

    @Override
    public LoginInfo getUserLoginInfo(String authToken) {
        return loginInfoRepository.findTopByAuthTokenOrderByIdDesc(authToken);
    }

    @Transactional
    @Override
    public void deleteLoginInfoByAuthToken(String token) {
        loginInfoRepository.deleteLoginInfoByAuthToken(token);
    }

    @Override
    @Transactional
    public LoginInfo saveLoginInfo(String token, String deviceInfo, Long userId) {
        LoginInfo loginInfo = LoginInfo.builder()
                .authToken(token)
                .date(new Timestamp(System.currentTimeMillis()))
                .deviceInfo(deviceInfo)
                .userId(userId)
                .build();

        return saveUserLoginInfo(loginInfo);
    }

    @Transactional
    public LoginInfo saveUserLoginInfo(LoginInfo loginInfo) {
        return loginInfoRepository.save(loginInfo);
    }

    @Override
    public void deleteAllByUserId(Long id) {
        loginInfoRepository.deleteAllByUserId(id);
    }
}

