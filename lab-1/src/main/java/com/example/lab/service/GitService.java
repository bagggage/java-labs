package com.example.lab.service;

import com.example.lab.cache.Cache;
import com.example.lab.entity.Git;
import com.example.lab.entity.User;
import com.example.lab.repository.GitRepository;
import com.example.lab.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class GitService {
    private GitRepository repository;
    private UserRepository userRepository;

    private static final int CACHE_MAX_SIZE = 40;
    public static final int PAGE_SIZE = 5;

    private Cache<String, List<Git>> gitsByUserCache = new Cache<>(CACHE_MAX_SIZE);

    public GitService(GitRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public Optional<Git> findGitById(Long id) {
        return repository.findById(id);
    }

    public List<Git> findGitsByOwnerUsername(String username) {
        return gitsByUserCache.tryGet(username).orPut(() ->
            repository.findAllByOwnerUsername(username)
        );
    }

    public Optional<Git> findGitByNameAndUsername(String name, String username) {
        return repository.findByNameAndOwnerUsername(name, username);
    }

    public void saveRepositories(List<Git> repositories) {
        repositories.stream().filter(
            git -> gitsByUserCache.isCached(git.getOwner().getName()))
            .forEach(git -> gitsByUserCache.uncache(git.getOwner().getName()));

        repository.saveAll(repositories);
    }

    public void saveRepository(Git git) {
        gitsByUserCache.uncache(git.getOwner().getUsername());
        repository.save(git);
    }

    public Page<Git> searchGitsByName(String name, int pageNumber) {
        return repository.searchByName('%' + name + '%', PageRequest.of(pageNumber, PAGE_SIZE));
    }

    public Page<Git> searchGitsByNameAndUser(String name, String username, int pageNumber) {
        return repository.searchByNameAndUser('%' + name + '%',
                                            username,
                                            PageRequest.of(pageNumber, PAGE_SIZE)
        );
    }

    public User addContributorByRepositoryName(String repositoryName,
                                            String username,
                                            String contributorUsername) {
        User contributor = userRepository.findByUsername(contributorUsername).orElse(null);
        Git gitRepository = repository.findByNameAndOwnerUsername(repositoryName,
                                                                username).orElse(null);

        if (contributor == null ||
            gitRepository == null || 
            contributor.getId() == gitRepository.getOwner().getId()) {
            return null;
        }

        if (contributor.getContributing()
            .stream()
            .anyMatch(repos -> repos.getId() == gitRepository.getId())) {
            return null;
        }

        contributor.getContributing().add(gitRepository);
        userRepository.save(contributor);

        gitsByUserCache.uncache(username);
        gitsByUserCache.uncache(contributorUsername);

        return contributor;
    }
}
