package com.example.lab.controller;

import com.example.lab.dto.LinkDto;
import com.example.lab.dto.UserDto;
import com.example.lab.entity.Link;
import com.example.lab.entity.User;
import com.example.lab.exceptions.NotFoundException;
import com.example.lab.service.UserService;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            service
            .findUserById(id)
            .orElseThrow(NotFoundException::new),
            true
        );
    }

    @GetMapping("current")
    public UserDto getCurrentUser() {
        return new UserDto(service.findUserByUsername(
            SecurityContextHolder.getContext().getAuthentication().getName()
            ).orElseThrow(NotFoundException::new), true);
    }

    @GetMapping("/bulk/id")
    public List<UserDto> getUsersByIds(@RequestBody List<Long> ids) {
        return service.findUsersByIdsBulk(ids).stream()
            .map(user -> {
                if (user == null) {
                    return null;
                } else {
                    return new UserDto(user, false);
                }
            })
            .toList();
    }

    @GetMapping("/{username}")
    public UserDto getUserByUsername(@PathVariable(name = "username") String username) {
        return new UserDto(
            service
            .findUserByUsername(username)
            .orElseThrow(NotFoundException::new),
            true
        );
    }

    @PostMapping(value = "/add", consumes = {"application/json"})
    public UserDto addUser(@RequestBody User user) {
        return new UserDto(service.addUser(user), false);
    }

    @PostMapping("/{username}/link")
    public LinkDto linkWithThirdPartyService(@PathVariable String username, 
                                @RequestParam(name = "service") String thirdPartyService,
                                @RequestParam(name = "username") String thirdPartyUsername) {    
        User targetUser = service.findUserByUsername(username).orElseThrow(
            NotFoundException::new
        );

        return new LinkDto(service.linkUser(targetUser,
            Link.Service.valueOf(thirdPartyService), thirdPartyUsername
        ));
    }

    @PatchMapping("/{username}")
    public UserDto updateUser(@PathVariable(name = "username") String username,
                            @RequestBody User user) {
        User updatedUser = service.updateUser(username, user);

        return new UserDto(updatedUser, false);
    }

    @DeleteMapping("/{username}")
    public UserDto deleteUser(@PathVariable(name = "username") String username) {
        return new UserDto(
            service
            .removeUserByUsername(username),
            false
        );
    }
}
