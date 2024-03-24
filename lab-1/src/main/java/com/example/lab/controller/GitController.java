package com.example.lab.controller;

import com.example.lab.dto.GitDto;
import com.example.lab.dto.PageDto;
import com.example.lab.dto.UserDto;
import com.example.lab.entity.Git;
import com.example.lab.entity.User;
import com.example.lab.exceptions.NotFoundException;
import com.example.lab.service.GitService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/repos")
public class GitController {
    private GitService service;

    public GitController(GitService service) {
        this.service = service;
    }

    @GetMapping("/{username}")
    public List<GitDto> getGitByOwner(@PathVariable(name = "username") String username) {
        List<Git> gits = service.findGitsByOwnerUsername(username);

        if (gits.isEmpty()) throw new NotFoundException();

        List<GitDto> result = new ArrayList<>(gits.size());

        for (Git git : gits) {
            result.add(new GitDto(git, false));
        }

        return result;
    }

    @GetMapping("/{username}/{name}")
    public GitDto getGitByOwnerAndName(@PathVariable(name = "username") String username,
                                    @PathVariable(name = "name") String name) {
        Git git = service.findGitByNameAndUsername(name, username).orElseThrow(() ->
                                                new NotFoundException());

        return new GitDto(git, false);
    }

    @GetMapping("/search")
    public PageDto<GitDto> searchGitsByNameAndUser(@RequestParam(name = "name") String name,
                                                @RequestParam(name = "user", required = false) String username,
                                                @RequestParam(name = "p", required = false) Integer pageNumber) {
        Page<Git> page;

        if (username == null) {
            page = service.searchGitsByName(name, (pageNumber == null ? 0 : pageNumber));
        }
        else {
            page = service.searchGitsByNameAndUser(name, username, (pageNumber == null ? 0 : pageNumber));
        }

        if (page.isEmpty()) throw new NotFoundException();

        List<GitDto> dtoList = new ArrayList<GitDto>(page.getSize());

        for (Git git : page) {
            dtoList.add(new GitDto(git, true));
        }

        PageDto<GitDto> dtoPage = new PageDto<>(dtoList, page.getPageable(), page.getTotalPages());

        return dtoPage;
    }

    @PatchMapping("/{username}/{name}/contribute")
    public UserDto addContributor(@PathVariable(name = "username") String username,
                                @PathVariable(name = "name") String name,
                                @RequestParam(name = "contributor") String contributorUsername) {
        User contributor = service.addContributorByRepositoryName(name, username, contributorUsername);

        if (contributor == null) {
            throw new NotFoundException();
        }

        return new UserDto(contributor, false);
    }
}
