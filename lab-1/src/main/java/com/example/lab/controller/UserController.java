package com.example.lab.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.lab.dto.UserDto;
import com.example.lab.entity.Link;
import com.example.lab.entity.User;
import com.example.lab.service.UserService;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.http.HttpStatus;
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
    public UserDto getUserById(@RequestParam(name = "id") Long id) {
        return new UserDto(
            service.
            findUserById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)),
            true
        );
    }

    @GetMapping("/{username}")
    public UserDto getUserByUsername(@PathVariable(name = "username") String username) {
        return new UserDto(
            service
            .findUserByUsername(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)),
            true
        );
    }

    @PostMapping(value = "/add", consumes = {"application/json"})
    public UserDto addUser(@RequestBody User user) {
        return new UserDto(service.addUser(user), false);
    }

    @PostMapping("/{username}/link")
    public Link linkWithThirdPartyService(
        @PathVariable String username, 
        @RequestParam(name = "service") Link.Service type,
        @RequestParam(name = "username") String thirdPartyUsername) {
        User targetUser = service.findUserByUsername(username).orElse(null);

        if (targetUser == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return service.linkUser(targetUser, type, thirdPartyUsername);
    }

    @PatchMapping("/{username}")
    public UserDto updateUser(@PathVariable(name = "username") String username, @RequestBody User user) {
        User updatedUser = service.updateUser(username, user);

        if (updatedUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return new UserDto(updatedUser, false);
    }

    @DeleteMapping("/{username}")
    public UserDto deleteUser(@PathVariable(name = "username") String username) {
        return new UserDto(
            service
            .removeUserByUsername(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)),
            false
        );
    }
}
