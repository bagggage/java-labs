package com.example.lab.dto;

import com.example.lab.entity.Git;
import com.example.lab.entity.User;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class GitDto {
    private Long id;

    private String name;

    private UserDto owner;
    private List<Long> contributorIds;

    private String gitUrl;
    private String language;

    public GitDto(Git entity, boolean includeOwner) {
        id = entity.getId();
        name = entity.getName();

        if (includeOwner) {
            owner = new UserDto(entity.getOwner(), false);
        }

        contributorIds = new ArrayList<>();

        if (entity.getContributors() != null) {
            for (User contributor : entity.getContributors()) {
                contributorIds.addLast(contributor.getId());
            }
        }

        gitUrl = entity.getGitUrl();
        language = entity.getLanguage();
    }
}
