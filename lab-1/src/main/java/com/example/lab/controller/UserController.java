package com.example.lab.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.lab.model.User;
import com.example.lab.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    private UserService service;

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return service.getUserByUsername(username);
    }

    @PostMapping(value = "/add", consumes = {"application/json"})
    public User addUser(@RequestBody User user) {
        return service.addUser(user);
    }
}
