package com.example.lab.controller;

import com.example.lab.dto.UserDto;
import com.example.lab.entity.Link;
import com.example.lab.entity.User;
import com.example.lab.exceptions.NotFoundException;
import com.example.lab.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void testGetUserById_Found() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userService.findUserById(userId)).thenReturn(Optional.of(user));

        UserDto result = userController.getUserById(userId);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
    }

    @Test
    void testGetUserById_NotFound() {
        Long userId = 1L;

        when(userService.findUserById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userController.getUserById(userId));
    }

    @Test
    void testGetUserByUsername_Found() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        when(userService.findUserByUsername(username)).thenReturn(Optional.of(user));

        UserDto result = userController.getUserByUsername(username);

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test
    void testGetUserByUsername_NotFound() {
        String username = "testUser";

        when(userService.findUserByUsername(username)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userController.getUserByUsername(username));
    }

    @Test
    void testAddUser() {
        User user = new User();

        when(userService.addUser(user)).thenReturn(user);

        UserDto result = userController.addUser(user);

        assertNotNull(result);
    }

    @Test
    void testLinkWithThirdPartyService_Found() {
        String username = "testUser";
        String thirdPartyService = "GITHUB";
        String thirdPartyUsername = "thirdPartyUser";
        Link link = new Link();

        when(userService.findUserByUsername(username)).thenReturn(Optional.of(new User()));
        when(userService.linkUser(any(User.class), any(Link.Service.class), anyString())).thenReturn(link);

        Link result = userController.linkWithThirdPartyService(username, thirdPartyService, thirdPartyUsername);

        assertNotNull(result);
    }

    @Test
    void testLinkWithThirdPartyService_NotFound() {
        String username = "testUser";
        String thirdPartyService = "GITHUB";
        String thirdPartyUsername = "thirdPartyUser";

        when(userService.findUserByUsername(username)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userController.linkWithThirdPartyService(username, thirdPartyService, thirdPartyUsername));
    }

    @Test
    void testUpdateUser() {
        String username = "testUser";
        User user = new User();

        when(userService.updateUser(username, user)).thenReturn(user);

        UserDto result = userController.updateUser(username, user);

        assertNotNull(result);
    }

    @Test
    void testDeleteUser() {
        String username = "testUser";
        User user = new User();

        when(userService.removeUserByUsername(username)).thenReturn(user);

        UserDto result = userController.deleteUser(username);

        assertNotNull(result);
    }
}
