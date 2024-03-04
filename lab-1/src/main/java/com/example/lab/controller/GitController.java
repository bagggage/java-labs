package com.example.lab.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.lab.dto.GitDto;
import com.example.lab.dto.UserDto;
import com.example.lab.entity.Git;
import com.example.lab.entity.User;
import com.example.lab.service.GitService;

import org.springframework.http.HttpStatus;
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
    public GitDto getGitByName(@RequestParam(name = "name") String name) {
        Git git = service.findGitByName(name).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return new GitDto(git, true);
    }

    @PatchMapping("/{name}/contribute")
    public UserDto addContributor(@PathVariable(name = "name") String name, @RequestParam(name = "username") String username) {
        User contributor = service.addContributorByRepositoryName(name, username);

        if (contributor == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return new UserDto(contributor, false);
    }
}
