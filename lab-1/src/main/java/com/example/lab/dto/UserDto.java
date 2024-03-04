package com.example.lab.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.lab.entity.Git;
import com.example.lab.entity.User;

import lombok.Data;

@Data
public class UserDto {
    private Long id;

    private String name;
    private String username;
    private String email;

    private List<GitDto> repositories;

    public UserDto(User entity, boolean includeRepositories) {
        id = entity.id;
        name = entity.name;
        username = entity.username;
        email = entity.email;

        if (includeRepositories) {
            repositories = new ArrayList<>();

            for (Git git : entity.ownedRepositories) {
                if (git.isPublic) repositories.addLast(new GitDto(git, false));
            }

            for (Git git : entity.contributing) {
                if (git.isPublic) repositories.addLast(new GitDto(git, true));
            }
        }
    }
}
