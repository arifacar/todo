package com.arifacar.domain.repository.user;

import com.arifacar.domain.model.user.LoginInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginInfoRepository extends CrudRepository<LoginInfo, Long> {

    LoginInfo findTopByAuthTokenOrderByIdDesc(String authToken);

    void delete(LoginInfo loginInfo);

    void deleteLoginInfoByAuthToken(String authToken);

}
