package com.example.lab.service;

import com.example.lab.entity.Git;
import com.example.lab.entity.Link;
import com.example.lab.entity.User;
import com.example.lab.exceptions.InvalidParamsException;
import com.example.lab.exceptions.NotFoundException;
import com.example.lab.exceptions.UndoneException;
import com.example.lab.repository.LinkRepository;
import com.example.lab.repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
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
class UserServiceTest {
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
    void testFindUserById() {
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
    void testFindUserByUsername() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testFindUsersByIdsBulk_EmptyList() {
        List<Long> ids = new ArrayList<>();

        List<User> result = userService.findUsersByIdsBulk(ids);

        assertEquals(0, result.size());
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    void testFindUsersByIdsBulk_AllFound() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user1.setId(1L);
        user2.setId(2L);
        user3.setId(3L);

        when(userRepository.findAllByIds(ids)).thenReturn(Arrays.asList(user1, user2, user3));

        List<User> result = userService.findUsersByIdsBulk(ids);

        assertEquals(3, result.size());
        assertEquals(user1, result.get(0));
        assertEquals(user2, result.get(1));
        assertEquals(user3, result.get(2));
    }

    @Test
    void testFindUsersByIdsBulk_SomeNotFound() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        User user1 = new User();
        User user3 = new User();
        user1.setId(1L);
        user3.setId(3L);

        when(userRepository.findAllByIds(ids)).thenReturn(Arrays.asList(user1, user3));

        List<User> result = userService.findUsersByIdsBulk(ids);

        assertEquals(3, result.size());
        assertEquals(user1, result.get(0));
        assertEquals(null, result.get(1));
        assertEquals(user3, result.get(2));
    }

    @Test
    void testAddUser() {
        User newUser = new User();
        newUser.setUsername("testUser");

        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(newUser)).thenReturn(newUser);

        assertEquals(newUser, userService.addUser(newUser));

        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(Optional.of(newUser));

        assertThrows(InvalidParamsException.class, () -> userService.addUser(newUser));
    }

    @Test
    void testUpdateUser() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        user.setEmail("@mail.com");
        user.setName("new name");
        user.setContributing(new ArrayList<>());
        user.setOwnedRepositories(new ArrayList<>());

        User updatedUser = new User();
        updatedUser.setUsername("newUsername");
        updatedUser.setContributing(new ArrayList<>());
        updatedUser.setOwnedRepositories(new ArrayList<>());

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(updatedUser);

        User result = userService.updateUser(username, updatedUser);

        assertEquals(updatedUser, result);
        assertEquals(updatedUser.getUsername(), result.getUsername());

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(username, updatedUser));
    }

    @Test
    void testRemoveUserByUsername() {
        String username = "testUser";
        User user = new User();
        user.setOwnedRepositories(Collections.emptyList());
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userRepository.existsById(user.getId())).thenReturn(true);

        assertThrows(UndoneException.class, () -> userService.removeUserByUsername(username));

        List<Git> ownedRepos = new ArrayList<>();
        Git git = new Git();
        git.setContributors(new ArrayList<>());
        ownedRepos.add(git);

        User contributor = new User();
        contributor.setContributing(new ArrayList<>(ownedRepos));
        git.getContributors().add(contributor);
        user.setOwnedRepositories(ownedRepos);

        when(userRepository.existsById(user.getId())).thenReturn(false);

        User result = userService.removeUserByUsername(username);

        assertEquals(user, result);
        verify(userRepository, times(2)).delete(user);
        verify(userRepository, times(1)).save(contributor);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.removeUserByUsername(username));
    }

    @Test
    void testLinkUser() {
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
