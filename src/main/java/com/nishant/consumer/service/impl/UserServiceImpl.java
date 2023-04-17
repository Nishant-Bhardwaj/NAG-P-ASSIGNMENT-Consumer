package com.nishant.consumer.service.impl;


import com.nishant.consumer.entity.User;
import com.nishant.consumer.repository.UserRepository;
import com.nishant.consumer.service.HotelService;
import com.nishant.consumer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    HotelService hotelService;

    @Override
    public List<User> getAllUsers() {

        List<User> userList =  userRepository.findAll();

        userList.forEach(usr ->{
            usr.setShortlistedHotel(hotelService.getShortlistedHotels(usr.getUserId()));
        });

        return userList;
    }
}
