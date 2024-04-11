package com.example.lab.service;

import com.example.lab.dto.GithubRepositoryDto;
import com.example.lab.entity.Git;
import com.example.lab.entity.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GithubServiceTest {
    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    private GithubService githubService;

    @BeforeEach
    public void setUp() {
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);        

        githubService = new GithubService(webClientBuilder);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    void testGetRepositoriesByUsername_NoRepositories() {
        User user = new User();
        String username = "testUser";

        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);

        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);

        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(Collections.emptyList()));

        List<Git> result = githubService.getRepositoriesByUsername(user, username);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    void testGetRepositoriesByUsername_WithRepositories() {
        User user = new User();
        String username = "testUser";
        List<GithubRepositoryDto> repositories = Collections.singletonList(new GithubRepositoryDto());

        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);

        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);

        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(repositories));

        List<Git> result = githubService.getRepositoriesByUsername(user, username);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testGetUrlByUsername() {
        String username = "testUser";
        String result = githubService.getUrlByUsername(username);

        assertNotNull(result);
        assertEquals("https://github.com/testUser", result);
    }
}