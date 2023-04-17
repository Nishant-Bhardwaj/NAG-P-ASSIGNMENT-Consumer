package com.nishant.consumer.service.impl;

import com.nishant.consumer.constant.Constants;
import com.nishant.consumer.dto.AuthRequest;
import com.nishant.consumer.security.JwtUtil;
import com.nishant.consumer.security.MyUserDetailsService;
import com.nishant.consumer.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Override
    public UserDetails authenticateUser(AuthRequest authRequest) {
        String username = authRequest.getUsername();
        try {

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());

            authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        }catch (Exception e){
            if(e.getMessage().equalsIgnoreCase("Bad credentials")){
                throw new BadCredentialsException(Constants.INVALID_CREDENTIALS);
            }
            // In case user is not registered:
            else {
                throw new BadCredentialsException(Constants.INVALID_CREDENTIALS);
            }
        }

        // If no exception/ user authenticated :
        return myUserDetailsService.loadUserByUsername(authRequest.getUsername());
    }

}
