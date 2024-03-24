package com.example.lab.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GithubRepositoryDto {
    private String name;
    
    @JsonProperty("git_url")
    private String gitUrl;
    
    private String language;
}
