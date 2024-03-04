package com.example.lab.service;

import java.util.List;

import com.example.lab.entity.Git;
import com.example.lab.entity.User;

public interface ThirdPartyGitService {
    public String getUrlByUsername(String username);
    public List<Git> getRepositoriesByUsername(User user, String username);
}
