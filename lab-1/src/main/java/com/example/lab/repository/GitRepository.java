package com.example.lab.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lab.entity.Git;

public interface GitRepository extends JpaRepository<Git, Long> {
    Optional<Git> findByName(String name);
}
