package com.example.lab.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubRepositoryDto {
    public String name;
    
    @JsonProperty("git_url")
    public String gitUrl;
}
