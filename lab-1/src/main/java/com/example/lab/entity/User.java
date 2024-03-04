package com.example.lab.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name = "name")
    public String name;

    @Column(name = "username", unique = true)
    public String username;

    @Column(name = "email")
    public String email;

    @OneToMany(mappedBy = "owner")
    public List<Git> ownedRepositories;

    @JsonIgnore
    @ManyToMany
    public List<Git> contributing;

    @OneToMany(mappedBy = "user")
    public List<Link> links;
}
