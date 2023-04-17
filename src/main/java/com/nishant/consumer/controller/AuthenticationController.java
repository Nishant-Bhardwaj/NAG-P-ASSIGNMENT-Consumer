package com.nishant.consumer.controller;

import com.nishant.consumer.dto.AuthRequest;
import com.nishant.consumer.dto.AuthResponse;
import com.nishant.consumer.security.JwtUtil;
import com.nishant.consumer.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/consumer/v1/auth")
public class AuthenticationController {

    @Autowired
    LoginService loginService;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Object> loginRequest(@RequestBody AuthRequest authRequest){
        // Authentication done by below service method
        UserDetails userDetails  = loginService.authenticateUser(authRequest);

        // Generate token:
        String token = jwtUtil.generateToken(userDetails);

        AuthResponse authResponse
                = new AuthResponse(token);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
