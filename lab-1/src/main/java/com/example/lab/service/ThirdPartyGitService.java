package com.example.lab.service;

import com.example.lab.entity.Git;
import com.example.lab.entity.User;
import java.util.List;

public interface ThirdPartyGitService {
    public String getUrlByUsername(String username);
    
    public List<Git> getRepositoriesByUsername(User user, String username);
}
