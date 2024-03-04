package com.example.lab.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "links")
public class Link {
    public enum Type {
        GITHUB,
        GITLAB,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name = "type")
    public Type type;

    @Column(name = "url")
    public String url;

    @JsonIgnore
    @ManyToOne
    public User user;
}
