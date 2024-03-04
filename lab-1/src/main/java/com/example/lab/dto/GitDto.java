package com.example.lab.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.lab.entity.Git;
import com.example.lab.entity.User;

import lombok.Data;

@Data
public class GitDto {
    private Long id;

    private String name;

    private UserDto owner;
    private List<Long> contributorIds;

    private String gitUrl;
    private Git.Language language;

    public GitDto(Git entity, Boolean includeOwner) {
        id = entity.getId();
        name = entity.getName();

        if (includeOwner) owner = new UserDto(entity.getOwner(), false);

        contributorIds = new ArrayList<>();
        contributorIds.addLast(entity.getId());

        for (User contributor : entity.getContributors()) {
            contributorIds.addLast(contributor.getId());
        }

        language = entity.getLanguage();

        gitUrl = entity.getGitUrl();
    }
}
