package com.nishant.consumer.service;

import com.nishant.consumer.dto.AuthRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {

    UserDetails authenticateUser(AuthRequest authRequest);
}
