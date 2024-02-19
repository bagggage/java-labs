package com.example.lab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lab.dao.UserRepository;
import com.example.lab.model.User;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public User getUserByUsername(String username) {
        return repository.findByUsername(username);
    }

    public User addUser(User user) {
        return repository.save(user);
    }
}
