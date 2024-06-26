package com.example.lab.controller;

import com.example.lab.dto.LinkDto;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    void testGetUsersByIds_EmptyList() {
        List<Long> ids = new ArrayList<>();

        List<UserDto> result = userController.getUsersByIds(ids);

        assertEquals(0, result.size());
    }

    @Test
    void testGetUsersByIds_AllFound() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        User user3 = new User();
        user3.setId(3L);

        when(userService.findUsersByIdsBulk(ids)).thenReturn(Arrays.asList(user1, user2, user3));

        List<UserDto> result = userController.getUsersByIds(ids);

        assertEquals(3, result.size());
        assertEquals(user1.getId(), result.get(0).getId());
        assertEquals(user2.getId(), result.get(1).getId());
        assertEquals(user3.getId(), result.get(2).getId());
    }

    @Test
    void testGetUsersByIds_SomeNotFound() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        User user1 = new User();
        user1.setId(1L);
        User user3 = new User();
        user3.setId(3L);

        when(userService.findUsersByIdsBulk(ids)).thenReturn(Arrays.asList(user1, null, user3));

        List<UserDto> result = userController.getUsersByIds(ids);

        assertEquals(3, result.size());
        assertEquals(user1.getId(), result.get(0).getId());
        assertEquals(null, result.get(1));
        assertEquals(user3.getId(), result.get(2).getId());
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

        link.setService(Link.Service.GITHUB);
        link.setUrl("github.com");

        when(userService.findUserByUsername(username)).thenReturn(Optional.of(new User()));
        when(userService.linkUser(any(User.class), any(Link.Service.class), anyString())).thenReturn(link);

        LinkDto result = userController.linkWithThirdPartyService(username, thirdPartyService, thirdPartyUsername);

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
