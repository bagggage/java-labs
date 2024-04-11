package com.example.lab.controller;

import com.example.lab.dto.GitDto;
import com.example.lab.dto.PageDto;
import com.example.lab.dto.UserDto;
import com.example.lab.entity.Git;
import com.example.lab.entity.User;
import com.example.lab.exceptions.NotFoundException;
import com.example.lab.service.GitService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitControllerTest {
    @Mock
    private GitService gitService;

    @InjectMocks
    private GitController gitController;

    @Test
    void testGetGitByOwner_NoGits() {
        String username = "testUser";

        when(gitService.findGitsByOwnerUsername(username)).thenReturn(new ArrayList<>());

        assertThrows(NotFoundException.class, () -> gitController.getGitByOwner(username));
    }

    @Test
    void testGetGitByOwner_WithGits() {
        String username = "testUser";
        List<Git> gits = new ArrayList<>();
        Git git = new Git();
        gits.add(git);

        when(gitService.findGitsByOwnerUsername(username)).thenReturn(gits);

        List<GitDto> result = gitController.getGitByOwner(username);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testGetGitByOwnerAndName_Found() {
        String username = "testUser";
        String name = "testRepo";
        Git git = new Git();

        when(gitService.findGitByNameAndUsername(name, username)).thenReturn(Optional.of(git));

        GitDto result = gitController.getGitByOwnerAndName(username, name);

        assertNotNull(result);
    }

    @Test
    void testGetGitByOwnerAndName_NotFound() {
        String username = "testUser";
        String name = "testRepo";

        when(gitService.findGitByNameAndUsername(name, username)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> gitController.getGitByOwnerAndName(username, name));
    }

    @Test
    void testSearchGitsByNameAndUser_NoResults() {
        String name = "testRepo";
        String username = "testUser";
        int pageNumber = 0;
        Page<Git> page = new PageImpl<>(new ArrayList<>());

        when(gitService.searchGitsByNameAndUser(name, username, pageNumber)).thenReturn(page);

        assertThrows(NotFoundException.class, () -> gitController.searchGitsByNameAndUser(name, username, pageNumber));
    }

    @Test
    void testSearchGitsByNameAndUser_WithResults() {
        String name = "testRepo";
        String username = "testUser";
        int pageNumber = 0;
 
        List<Git> gits = new ArrayList<>();

        Git git = new Git();
        git.setOwner(new User());
        git.setName(name);
        gits.add(git);

        Page<Git> page = new PageImpl<>(gits, Pageable.ofSize(1), 1);

        when(gitService.searchGitsByNameAndUser(name, username, pageNumber)).thenReturn(page);

        PageDto<GitDto> result = gitController.searchGitsByNameAndUser(name, username, pageNumber);

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
    }

    @Test
    void testSearchGitsByNameAndUser_WithResultsNoUsername() {
        String name = "testRepo";
        int pageNumber = 0;
 
        List<Git> gits = new ArrayList<>();

        Git git = new Git();
        git.setOwner(new User());
        git.setName(name);
        gits.add(git);

        Page<Git> page = new PageImpl<>(gits, Pageable.ofSize(1), 1);

        when(gitService.searchGitsByName(name, pageNumber)).thenReturn(page);

        PageDto<GitDto> result = gitController.searchGitsByNameAndUser(name, null, pageNumber);

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
    }

    @Test
    void testAddContributor_Found() {
        String username = "testUser";
        String name = "testRepo";
        String contributorUsername = "contributor";
        User contributor = new User();

        when(gitService.addContributorByRepositoryName(name, username, contributorUsername)).thenReturn(contributor);

        UserDto result = gitController.addContributor(username, name, contributorUsername);

        assertNotNull(result);
    }

    @Test
    void testAddContributor_NotFound() {
        String username = "testUser";
        String name = "testRepo";
        String contributorUsername = "contributor";

        when(gitService.addContributorByRepositoryName(name, username, contributorUsername)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> gitController.addContributor(username, name, contributorUsername));
    }
}
