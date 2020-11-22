package com.arifacar.service.user;

import com.arifacar.domain.model.user.LoginInfo;
import com.arifacar.domain.model.user.User;
import com.arifacar.domain.repository.user.LoginInfoRepository;
import com.arifacar.domain.repository.user.UserRepository;
import com.arifacar.service.common.BaseService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService extends BaseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginInfoRepository loginInfoRepository;

    /**
     * Get all the users with their titles, expertise and departments
     */
    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        Assert.isTrue(user.isPresent(), "Kullanıcı bulunamadı.");
        return user.get();
    }

    public LoginInfo getUserLoginInfo(String authToken) {
        return loginInfoRepository.findTopByAuthTokenOrderByIdDesc(authToken);
    }

    @Transactional
    public void deleteLoginInfoByAuthToken(String token) {
        loginInfoRepository.deleteLoginInfoByAuthToken(token);
    }

    @Transactional
    public LoginInfo saveUserLoginInfo(LoginInfo loginInfo) {
        return loginInfoRepository.save(loginInfo);
    }

}

