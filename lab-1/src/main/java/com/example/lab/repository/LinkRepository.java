package com.example.lab.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lab.entity.Link;
import com.example.lab.entity.Link.Service;
import com.example.lab.entity.User;

public interface LinkRepository extends JpaRepository<Link, Long> {
    Optional<Link> findByUserAndService(User user, Service service);
}
