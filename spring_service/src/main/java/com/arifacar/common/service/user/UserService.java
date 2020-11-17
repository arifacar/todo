package com.arifacar.common.service.user;

import com.arifacar.domain.model.user.User;
import com.arifacar.domain.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        users.sort(Comparator.comparing(User::getUsername));
        return users;
    }
}
