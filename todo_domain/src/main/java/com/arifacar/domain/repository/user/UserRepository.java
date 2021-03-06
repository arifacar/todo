package com.arifacar.domain.repository.user;

import com.arifacar.domain.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Page<User> findAll(Pageable pageable);

    User findTopByUsername(String username);

    User findByUsername(String username);

    User findTopByUsernameOrEmail(String username, String email);
}
