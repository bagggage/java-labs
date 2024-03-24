package com.example.lab.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.lab.entity.Git;

public interface GitRepository extends JpaRepository<Git, Long> {
    List<Git> findAllByOwnerUsername(String username);
    Optional<Git> findByNameAndOwnerUsername(String name, String username);

    @Query(value = "SELECT g FROM Git g WHERE g.name iLIKE ?1")
    Page<Git> searchByName(String name, Pageable pageable);

    @Query(value = "SELECT g FROM Git g OUTER JOIN g.contributors c WHERE g.name iLIKE ?1 AND (g.owner.username = ?2 OR c.username = ?2)")
    Page<Git> searchByNameAndUser(String name, String username, Pageable pageable);
}
