package com.example.lab.repository;

import com.example.lab.entity.User;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE id IN (:ids)", nativeQuery = true)
    List<User> findAllByIds(@Param("ids") List<Long> ids);

    @Transactional
    List<User> removeByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
