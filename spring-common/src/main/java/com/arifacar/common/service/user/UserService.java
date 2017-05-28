package com.arifacar.common.service.user;

import com.arifacar.common.exception.EntityNotFoundException;
import com.arifacar.domain.model.user.User;
import com.arifacar.domain.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserById(Long id) {
        User user = userRepository.findOne(id);
        if (user == null)
            throw new EntityNotFoundException(User.class, Math.toIntExact(id));

        return user;
    }


}
