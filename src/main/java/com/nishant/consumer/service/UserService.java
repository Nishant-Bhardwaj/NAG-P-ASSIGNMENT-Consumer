package com.nishant.consumer.service;

import com.nishant.consumer.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<User> getAllUsers();
}
