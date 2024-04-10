package com.example.lab.service;

import com.example.lab.entity.Git;
import com.example.lab.entity.Link;
import com.example.lab.entity.User;
import com.example.lab.exceptions.InvalidParamsException;
import com.example.lab.exceptions.UndoneException;
import com.example.lab.repository.LinkRepository;
import com.example.lab.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private LinkRepository linkRepository;

    @Mock
    private GitService gitService;

    @Mock
    private GithubService githubService;

    @InjectMocks
    private UserService userService;

    @Test
    public void testFindUserById() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserById(userId);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        result = userService.findUserById(userId);

        assertFalse(result.isPresent());
    }

    @Test
    public void testFindUserByUsername() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testAddUser() {
        User newUser = new User();
        newUser.setUsername("testUser");

        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(newUser)).thenReturn(newUser);

        assertEquals(newUser, userService.addUser(newUser));

        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(Optional.of(newUser));

        assertThrows(InvalidParamsException.class, () -> userService.addUser(newUser));
    }

    @Test
    public void testUpdateUser() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        User updatedUser = new User();
        updatedUser.setUsername("newUsername");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User result = userService.updateUser(username, updatedUser);

        assertEquals(updatedUser, result);
        assertEquals(updatedUser.getUsername(), result.getUsername());
    }

    @Test
    public void testRemoveUserByUsername() {
        String username = "testUser";
        User user = new User();
        user.setOwnedRepositories(Collections.emptyList());
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userRepository.existsById(user.getId())).thenReturn(true);

        assertThrows(UndoneException.class, () -> userService.removeUserByUsername(username));

        when(userRepository.existsById(user.getId())).thenReturn(false);

        User result = userService.removeUserByUsername(username);

        assertEquals(user, result);
        verify(userRepository, times(2)).delete(user);
    }

    @Test
    public void testLinkUser() {
        String username = "testUsername";
        User user = new User();
        user.setUsername(username);
    
        Link.Service linkService = Link.Service.GITHUB;
        Git gitRepo = new Git();
        List<Git> gitRepos = new ArrayList<>();
    
        gitRepos.add(gitRepo);

        when(linkRepository.findByUserAndService(user, linkService)).thenReturn(Optional.empty());
        when(githubService.getRepositoriesByUsername(user, username)).thenReturn(gitRepos);
        when(linkRepository.save(any(Link.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Link result = userService.linkUser(user, linkService, username);

        assertNotNull(result);
        assertEquals(linkService, result.getService());
        assertEquals(user, result.getUser());
        verify(gitService, times(1)).saveRepositories(gitRepos);
        verify(linkRepository, times(1)).save(any(Link.class));
    }
}
