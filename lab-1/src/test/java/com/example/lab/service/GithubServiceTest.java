package com.example.lab.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

@ExtendWith(MockitoExtension.class)
public class GithubServiceTest {
    private GithubService githubService;

    @BeforeEach
    public void setUp() {
        githubService = new GithubService(WebClient.builder());
    }

    @Test
    public void testGetUrlByUsername() {
        assertEquals(githubService.getUrlByUsername("test"), "https://github.com/test");
    }
}
