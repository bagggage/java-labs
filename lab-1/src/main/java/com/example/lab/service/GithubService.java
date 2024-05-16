package com.example.lab.service;

import com.example.lab.dto.GithubRepositoryDto;
import com.example.lab.entity.Git;
import com.example.lab.entity.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GithubService implements ThirdPartyGitService {
    private static final String GITHUB_URL = "https://github.com";
    private static final String GITHUP_API_URL = "https://api.github.com";
    private static final String USERS_API = "/users";
    private static final String REPOS_API = "/repos";

    private final WebClient webClient;

    public GithubService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(GITHUP_API_URL).build();
    }

    @Override
    public List<Git> getRepositoriesByUsername(User user, String username) {
        List<GithubRepositoryDto> repositories = webClient
                                .get()
                                .uri(USERS_API + '/' + username + REPOS_API)
                                .header("User-Agent", "Git-Helper-Service")
                                .retrieve()
                                .bodyToMono(new 
                                    ParameterizedTypeReference<List<GithubRepositoryDto>>(){}
                                )
                                .block();

        if (repositories == null) {
            return Collections.emptyList();
        }

        ArrayList<Git> result = new ArrayList<>(repositories.size());

        for (GithubRepositoryDto githubRepository : repositories) {
            Git git = new Git();

            git.setOwner(user);
            git.setName(githubRepository.getName());
            git.setGitUrl(githubRepository.getGitUrl());
            git.setLanguage(githubRepository.getLanguage());

            result.add(git);
        }

        return result;
    }

    @Override
    public String getUrlByUsername(String username) {
        return GITHUB_URL + '/' + username;
    }
}
