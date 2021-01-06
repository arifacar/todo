package com.arifacar.service.user;

import com.arifacar.domain.model.constants.Constants;
import com.arifacar.domain.model.user.User;
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
public class UserService extends BaseService implements IUserService<User> {

    private final UserRepository userRepository;
    private final UserLoginService userLoginService;
    private final AwsFileService awsFileService;

    @Autowired
    public UserService(UserRepository userRepository, UserLoginService userLoginService,
                       AwsFileService awsFileService) {
        this.userRepository = userRepository;
        this.userLoginService = userLoginService;
        this.awsFileService = awsFileService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return update(user, null);
    }

    @Override
    public User update(User user, MultipartFile image) {
        User userPersistent = findById(user.getId());
        StringUtil.trimUser(user);

        if (user.getUsername() != null) {
            existUserName(user);
            userPersistent.setUsername(user.getUsername());
        }

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

        uplaodImage(image, userPersistent);

        return userRepository.save(userPersistent);
    }

    private void uplaodImage(MultipartFile image, User userPersistent) {
        if (image != null) {
            try {
                String url = awsFileService.uploadSingleFile(userPersistent, image, Constants.UPLOAD_PROFILE);
                if (!StringUtils.isEmpty(url)) {
                    userPersistent.setProfilePic(url);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<User> findAll(int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, getCommonPageSize());
        return userRepository.findAll(pageRequest).getContent();
    }

    @Override
    public boolean existUserName(User user) {
        User persistedUser = findByUsername(user.getUsername());
        Assert.isTrue(persistedUser == null, user.getUsername() + " kullanıcı adı başkası tarafından tarafından kullanılıyor.");
        return false;
    }

    @Override
    public void delete(User currentUser) {
        userLoginService.deleteAllByUserId(currentUser.getId());
        awsFileService.deleteSingleFile(currentUser, currentUser.getProfilePic(), Constants.UPLOAD_PROFILE);
        userRepository.delete(currentUser);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void verifyEmail(User currentUser) {
        System.out.println("SEND VERIFY EMAIL");
        // emailService.sendEmail(currentUser); // TODO: SEND VERIFY EMAIL
    }

    @Override
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        Assert.isTrue(user.isPresent(), "User not found.");
        return user.get();
    }

    @Override
    public User findByUsernameOrEmail(String username, String email) {
        return userRepository.findTopByUsernameOrEmail(username, email);
    }
}

