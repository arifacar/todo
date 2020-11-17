package com.arifacar.domain.repository.user;

import com.arifacar.domain.model.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findAll();
}
