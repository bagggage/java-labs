package com.example.lab.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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
        return repository.save(user);
    }

    public User updateUser(String username, User userData) {
        User targetUser = repository.findByUsername(username).orElse(null);

        if (targetUser == null) return null;

        if (userData.username != null && userData.username.isEmpty() == false) {
            if (repository.findByUsername(userData.username).isEmpty()) {
                targetUser.username = userData.username;
            }
        }

        if (userData.name != null && userData.name.isEmpty() == false) targetUser.name = userData.name;
        if (userData.email != null && userData.email.isEmpty() == false) targetUser.email = userData.email;

        return repository.save(targetUser);
    }

    public List<User> removeUserByUsername(String username) {
        return repository.removeByUsername(username);
    }

    public Link linkUser(User user, Link.Type linkType, String username) {
        // Link already exists
        if (linkRepository.findByUserAndType(user, linkType).isPresent()) return null;

        ThirdPartyGitService thirdGitService;
        
        switch (linkType) {
            case GITHUB:
                thirdGitService = githubService;
            break;
            default:
                return null; // Unsupported link type
        }

        List<Git> repositories = thirdGitService.getRepositoriesByUsername(user, username);
        gitService.saveRepositories(repositories);

        Link link = new Link();

        link.type = linkType;
        link.url = thirdGitService.getUrlByUsername(username);
        link.user = user;

        return linkRepository.save(link);
    }
}
