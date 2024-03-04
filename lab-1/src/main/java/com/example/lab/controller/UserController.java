package com.example.lab.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.lab.entity.Link;
import com.example.lab.entity.User;
import com.example.lab.service.UserService;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/users")
public class UserController {
    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public User getUserById(@RequestParam(name = "id") Long id) {
        return service.findUserById(id).orElse(null);
    }

    @GetMapping("/{username}")
    public User getUserByUsername(@PathVariable(name = "username") String username) {
        return service.findUserByUsername(username).orElse(null);
    }

    @PostMapping(value = "/add", consumes = {"application/json"})
    public User addUser(@RequestBody User user) {
        return service.addUser(user);
    }

    @PostMapping("/{username}/link")
    public Link putMethodName(
        @PathVariable String username, 
        @RequestParam(name = "type") Link.Type type,
        @RequestParam(name = "url") String url) {
        User targetUser = service.findUserByUsername(username).orElse(null);

        // User not found
        if (targetUser == null) return null;

        return service.linkUser(targetUser, type, url);
    }

    @PatchMapping("/{username}")
    public User updateUser(@PathVariable(name = "username") String username, @RequestBody User user) {
        return service.updateUser(username, user);
    }

    @DeleteMapping("/{username}")
    public List<User> deleteUser(@PathVariable(name = "username") String username) {
        return service.removeUserByUsername(username);
    }
}
