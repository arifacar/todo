package com.arifacar.service.user;

import com.arifacar.domain.model.constants.Constants;
import com.arifacar.domain.model.user.LoginInfo;
import com.arifacar.domain.model.user.User;
import com.arifacar.domain.repository.user.LoginInfoRepository;
import com.arifacar.domain.repository.user.UserRepository;
import com.arifacar.service.common.AwsFileService;
import com.arifacar.service.common.BaseService;
import com.arifacar.service.util.StringUtil;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService extends BaseService {

    private final UserRepository userRepository;
    private final LoginInfoRepository loginInfoRepository;
    private final AwsFileService awsFileService;

    @Autowired
    public UserService(UserRepository userRepository, LoginInfoRepository loginInfoRepository,
                       AwsFileService awsFileService) {
        this.userRepository = userRepository;
        this.loginInfoRepository = loginInfoRepository;
        this.awsFileService = awsFileService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public User create(User user) {
        return userRepository.save(user);
    }

    public User update(User currentUser, User user, MultipartFile image) {
        User userPersistent = findUserById(currentUser.getId());
        StringUtil.trimUser(user);

        if (user.getUsername() != null) {
            existUserName(currentUser, user);
            userPersistent.setUsername(user.getUsername());
        }

        uplaodImage(currentUser, image, userPersistent);

        if (userPersistent.getEmail() != null && userPersistent.getEmail().equalsIgnoreCase(user.getEmail()) &&
                !userPersistent.isEmailVerified()) {
            System.out.println("SEND VERIFY EMAIL");
            // emailService.sendEmail(currentUser); // TODO: SEND VERIFY EMAIL
        } else if (!StringUtils.isEmpty(user.getEmail())) {
            userPersistent.setEmail(user.getEmail());
            userPersistent.setEmailVerified(false);
            System.out.println("SEND VERIFY EMAIL");
            // emailService.sendEmail(currentUser); // TODO: SEND VERIFY EMAIL
        }

        userPersistent.setName(StringUtils.isEmpty(user.getName()) ? userPersistent.getName() : user.getName());
        userPersistent.setSurname(StringUtils.isEmpty(user.getSurname()) ? userPersistent.getSurname() : user.getSurname());
        userPersistent.setProfilePic(StringUtils.isEmpty(user.getProfilePic()) ? userPersistent.getProfilePic() : user.getProfilePic());

        return userRepository.save(userPersistent);
    }

    private void uplaodImage(User currentUser, MultipartFile image, User userPersistent) {
        if (image != null) {
            try {
                String url = awsFileService.uploadSingleFile(currentUser, image, Constants.UPLOAD_PROFILE);
                if (!StringUtils.isEmpty(url)) {
                    userPersistent.setProfilePic(url);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<User> findAll(int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, getPageSizeCommon());
        return userRepository.findAll(pageRequest).getContent();
    }

    public boolean existUserName(User currentUser, User user) {
        User persistedUser = findByUsername(user.getUsername());
        if (persistedUser == null || !currentUser.getId().equals(persistedUser.getId()))
            Assert.isNull(persistedUser, user.getUsername() + " kullanıcı adı başkası tarafından tarafından kullanılıyor.");
        return false;
    }

    public void delete(User currentUser) {
        loginInfoRepository.deleteAllByUserId(currentUser.getId());
        awsFileService.deleteSingleFile(currentUser, currentUser.getProfilePic(), Constants.UPLOAD_PROFILE);
        userRepository.delete(currentUser);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void verifyEmail(User currentUser) {
        System.out.println("SEND VERIFY EMAIL");
        // emailService.sendEmail(currentUser); // TODO: SEND VERIFY EMAIL
    }

    public LoginInfo getUserLoginInfo(String authToken) {
        return loginInfoRepository.findTopByAuthTokenOrderByIdDesc(authToken);
    }

    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        Assert.isTrue(user.isPresent(), "User not found.");
        return user.get();
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

