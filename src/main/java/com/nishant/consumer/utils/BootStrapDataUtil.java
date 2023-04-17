package com.nishant.consumer.utils;

import com.nishant.consumer.entity.User;
import com.nishant.consumer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BootStrapDataUtil {

    @Autowired
    UserRepository userRepository;

    @PostConstruct
    private void createFlightBootstrapData(){
        userRepository.save(User.builder().userId(1L).username("admin").build());
    }
}
