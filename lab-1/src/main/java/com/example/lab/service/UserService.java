package com.example.lab.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.lab.entity.Git;
import com.example.lab.entity.Link;
import com.example.lab.entity.User;
import com.example.lab.repository.LinkRepository;
import com.example.lab.repository.UserRepository;

@Service
public class UserService {
    private UserRepository repository;
    private LinkRepository linkRepository;

    private GitService gitService;
    private GithubService githubService;

    public UserService(UserRepository repository, LinkRepository linkRepository, GitService gitService, GithubService githubService) {
        this.repository = repository;
        this.linkRepository = linkRepository;
        this.gitService = gitService;
        this.githubService = githubService;
    }

    public Optional<User> findUserById(Long id) {
        return repository.findById(id);
    }

    public Optional<User> findUserByUsername(String username) {
        return repository.findByUsername(username);
    }

    public User addUser(User user) {
        if (user.getId() != null || repository.findByUsername(user.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return repository.save(user);
    }

    public User updateUser(String username, User userData) {
        User targetUser = repository.findByUsername(username).orElse(null);

        if (targetUser == null) return null;

        if (userData.getUsername() != null && !userData.getUsername().isEmpty()
                && repository.findByUsername(userData.getUsername()).isEmpty()) {
            targetUser.setUsername(userData.getUsername());
        }

        if (userData.getName() != null && !userData.getName().isEmpty()) targetUser.setName(userData.getName());
        if (userData.getEmail() != null && !userData.getEmail().isEmpty()) targetUser.setName(userData.getEmail());

        return repository.save(targetUser);
    }

    public Optional<User> removeUserByUsername(String username) {
        return repository.removeByUsername(username);
    }

    public Link linkUser(User user, Link.Service linkService, String username) {
        // Link already exists
        if (linkRepository.findByUserAndType(user, linkService).isPresent()) return null;

        ThirdPartyGitService thirdGitService;
        
        if (linkService == Link.Service.GITHUB) {
            thirdGitService = githubService;
        } else {
            // Unsuported service
            return null;
        }

        List<Git> repositories = thirdGitService.getRepositoriesByUsername(user, username);

        if (!repositories.isEmpty()) gitService.saveRepositories(repositories);

        Link link = new Link();

        link.setService(linkService);
        link.setUrl(thirdGitService.getUrlByUsername(username));
        link.setUser(user);

        return linkRepository.save(link);
    }
}
