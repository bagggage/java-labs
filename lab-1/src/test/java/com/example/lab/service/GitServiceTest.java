package com.example.lab.service;

import com.example.lab.cache.Cache;
import com.example.lab.entity.Git;
import com.example.lab.entity.User;
import com.example.lab.repository.GitRepository;
import com.example.lab.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GitServiceTest {
    @Mock
    private GitRepository gitRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Cache<String, List<Git>> gitsByUserCache;

    @InjectMocks
    private GitService gitService;

    @Test
    void testFindGitById() {
        Long gitId = 1L;
        Git git = new Git();
        git.setId(gitId);

        when(gitRepository.findById(gitId)).thenReturn(Optional.of(git));

        Optional<Git> result = gitService.findGitById(gitId);

        assertTrue(result.isPresent());
        assertEquals(git, result.get());
    }

    @Test
    void testFindGitsByOwnerUsername() {
        String username = "testUser";
        List<Git> gits = new ArrayList<>();

        when(gitRepository.findAllByOwnerUsername(username)).thenReturn(gits);

        List<Git> result = gitService.findGitsByOwnerUsername(username);

        assertEquals(gits, result);
    }

    @Test
    void testFindGitByNameAndUsername() {
        String name = "testGit";
        String username = "testUser";
        Git git = new Git();
        when(gitRepository.findByNameAndOwnerUsername(name, username)).thenReturn(Optional.of(git));

        Optional<Git> result = gitService.findGitByNameAndUsername(name, username);

        assertTrue(result.isPresent());
        assertEquals(git, result.get());
    }

    @Test
    void testSaveRepositories() {
        List<Git> repositories = new ArrayList<>();
        Git git = new Git();
        git.setOwner(new User());
        repositories.add(git);

        when(gitRepository.saveAll(repositories)).thenReturn(repositories);

        gitService.saveRepositories(repositories);

        verify(gitRepository, times(1)).saveAll(repositories);
    }

    @Test
    void testSaveRepository() {
        Git git = new Git();
        git.setOwner(new User());

        when(gitRepository.save(git)).thenReturn(git);

        gitService.saveRepository(git);

        verify(gitRepository, times(1)).save(git);
    }

    @SuppressWarnings("unchecked")
    @Test
    void testSearchGitsByName() {
        String name = "testGit";
        Page<Git> page = mock(Page.class);
        when(gitRepository.searchByName("%" + name + "%", PageRequest.of(0, GitService.PAGE_SIZE))).thenReturn(page);

        Page<Git> result = gitService.searchGitsByName(name, 0);

        assertEquals(page, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    void testSearchGitsByNameAndUser() {
        String name = "testGit";
        String username = "testUser";
        Page<Git> page = mock(Page.class);
        when(gitRepository.searchByNameAndUser("%" + name + "%", username, PageRequest.of(0, GitService.PAGE_SIZE))).thenReturn(page);

        Page<Git> result = gitService.searchGitsByNameAndUser(name, username, 0);

        assertEquals(page, result);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    void testAddContributorByRepositoryName() {
        String repositoryName = "testRepo";
        String ownerUsername = "owner";
        String contributorUsername = "contributor";

        User owner = new User();
        owner.setUsername(ownerUsername);

        User contributor = new User();
        contributor.setUsername(contributorUsername);
        contributor.setId(1L);
        contributor.setContributing(new ArrayList());
    
        Git git = new Git();
        git.setOwner(owner);
        owner.setOwnedRepositories(List.of(git));

        when(userRepository.findByUsername(contributorUsername)).thenReturn(Optional.of(contributor));
        when(gitRepository.findByNameAndOwnerUsername(repositoryName, ownerUsername)).thenReturn(Optional.of(git));

        User result = gitService.addContributorByRepositoryName(repositoryName, ownerUsername, contributorUsername);

        assertNotNull(result);
        assertTrue(result.getContributing().contains(git));
        verify(userRepository, times(1)).save(contributor);
    }
}