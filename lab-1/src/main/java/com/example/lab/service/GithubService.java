package com.example.lab.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.lab.dto.GithubRepositoryDto;
import com.example.lab.entity.Git;
import com.example.lab.entity.User;

@Service
public class GithubService implements ThirdPartyGitService {
    private static final String githubUrl = "https://github.com";
    private static final String githubApiUrl = "https://api.github.com";
    private static final String usersApi = "/users";
    private static final String reposApi = "/repos";

    private final WebClient webClient;

    public GithubService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(githubApiUrl).build();
    }

    public List<Git> getRepositoriesByUsername(User user, String username) {
        List<GithubRepositoryDto> repositories = webClient
                                .get()
                                .uri(usersApi + '/' + username + reposApi)
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<List<GithubRepositoryDto>>(){})
                                .block();

        ArrayList<Git> result = new ArrayList<Git>(repositories.size());

        for (GithubRepositoryDto githubRepository : repositories) {
            Git git = new Git();

            git.owner = user;
            git.name = githubRepository.name;
            git.gitUrl = githubRepository.git_url;

            result.add(git);
        }

        return result;
    }

    public String getUrlByUsername(String username) {
        return githubUrl + '/' + username;
    }
}
