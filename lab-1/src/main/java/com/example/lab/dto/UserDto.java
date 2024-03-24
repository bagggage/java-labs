package com.example.lab.dto;

import com.example.lab.entity.Git;
import com.example.lab.entity.User;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class UserDto {
    private Long id;

    private String name;
    private String username;
    private String email;

    private List<GitDto> repositories;

    public UserDto(User entity, boolean includeRepositories) {
        id = entity.getId();
        name = entity.getName();
        username = entity.getUsername();
        email = entity.getEmail();

        if (includeRepositories) {
            repositories = new ArrayList<>();

            for (Git git : entity.getOwnedRepositories()) {
                if (Boolean.TRUE.equals(git.getIsPublic())) {
                    repositories.addLast(new GitDto(git, false));
                }
            }

            for (Git git : entity.getContributing()) {
                if (Boolean.TRUE.equals(git.getIsPublic())) {
                    repositories.addLast(new GitDto(git, true));
                }
            }
        }
    }
}
