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
        id = entity.id;
        name = entity.name;
        owner = new UserDto(entity.owner, false);
        contributorIds = new ArrayList<>();

        for (User contributor : entity.contributors) {
            contributorIds.addLast(contributor.id);
        }

        language = entity.language;

        gitUrl = entity.gitUrl;
    }
}
