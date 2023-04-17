package com.nishant.consumer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nishant.consumer.dto.HotelSearchDTO;
import com.nishant.consumer.dto.HotelShortlisted;
import com.nishant.consumer.service.HotelService;
import com.nishant.consumer.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/consumer/user")
public class UserController {

    Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    UserService userService;

    // Search All Hotels
    @GetMapping(path = "/all")
    public ResponseEntity<Object> getAllUsers(){
        logger.info("Request Received, Get All Users");
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

}
