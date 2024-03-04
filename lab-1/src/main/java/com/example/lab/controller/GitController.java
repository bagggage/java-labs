package com.example.lab.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.lab.entity.Git;
import com.example.lab.entity.User;
import com.example.lab.service.GitService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/repos")
public class GitController {
    private GitService service;

    public GitController(GitService service) {
        this.service = service;
    }

    @GetMapping
    public Git getGitByName(@RequestParam(name = "name") String name) {
        return service.findGitByName(name).orElse(null);
    }

    @PatchMapping("/{name}/contribute")
    public User addContributor(@PathVariable(name = "name") String name, @RequestParam(name = "username") String username) {
        return service.addContributorByRepositoryName(name, username);
    }
}
