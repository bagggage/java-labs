package com.example.lab.repository;

import com.example.lab.entity.Link;
import com.example.lab.entity.Link.Service;
import com.example.lab.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link, Long> {
    Optional<Link> findByUserAndService(User user, Service service);
}
