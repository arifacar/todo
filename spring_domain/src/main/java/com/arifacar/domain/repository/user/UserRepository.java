package com.arifacar.domain.repository.user;

import com.arifacar.domain.model.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findTopByUsername(String username);

}
