package com.arifacar.api.security;

import com.arifacar.domain.model.user.User;
import com.arifacar.domain.repository.user.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("username can not be null");
        }

        User user = userRepository.findTopByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with %s doesn't exist!", username));
        }

        return new UserAdapter(user);
    }
}