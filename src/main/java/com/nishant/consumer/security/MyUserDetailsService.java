package com.nishant.consumer.security;

import com.nishant.consumer.constant.Constants;
import com.nishant.consumer.exception.CustomBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException {

        // One demo admin user:

        if("admin".equals(user)){
            return new User(user, passwordEncoder.encode("admin"), new ArrayList<>());
        }
        else
            throw new CustomBadRequestException(Constants.INVALID_CREDENTIALS);
    }

}
