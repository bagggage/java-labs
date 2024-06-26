package com.example.lab.dto;

import com.example.lab.entity.Git;
import com.example.lab.entity.Link;
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
    private List<LinkDto> links; 

    public UserDto(User entity, boolean includeRepositories) {
        id = entity.getId();
        name = entity.getName();
        username = entity.getUsername();
        email = entity.getEmail();

        if (!includeRepositories) {
            return;
        }

        repositories = new ArrayList<>();

        if (entity.getOwnedRepositories() != null) {
            for (Git git : entity.getOwnedRepositories()) {
                if (Boolean.TRUE.equals(git.getIsPublic())) {
                    repositories.addLast(new GitDto(git, false));
                }
            }
        }

        if (entity.getContributing() != null) {
            for (Git git : entity.getContributing()) {
                if (Boolean.TRUE.equals(git.getIsPublic())) {
                    repositories.addLast(new GitDto(git, true));
                }
            }
        }

        links = new ArrayList<>();

        if (entity.getLinks() != null) {
            for (Link link : entity.getLinks()) {
                links.add(new LinkDto(link));
            }
        }
    }
}
