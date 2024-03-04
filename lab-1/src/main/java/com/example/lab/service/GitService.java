package com.example.lab.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.lab.entity.Git;
import com.example.lab.entity.User;
import com.example.lab.repository.GitRepository;
import com.example.lab.repository.UserRepository;

@Service
public class GitService {
    private GitRepository repository;
    private UserRepository userRepository;

    public GitService(GitRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public Optional<Git> findGitById(Long id) {
        return this.repository.findById(id);
    }

    public Optional<Git> findGitByName(String name) {
        return this.repository.findByName(name);
    }

    public void saveRepositories(List<Git> repositories) {
        repository.saveAll(repositories);
    }

    public User addContributorByRepositoryName(String repositoryName, String contributorUsername) {
        User contributor = userRepository.findByUsername(contributorUsername).orElse(null);
        Git gitRepository = repository.findByName(repositoryName).orElse(null);

        if (contributor == null || gitRepository == null) return null;

        contributor.getContributing().addLast(gitRepository);
        userRepository.save(contributor);

        return contributor;
    }
}
