package com.example.lab.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "repos")
public class Git {
    public enum Language {
        A,
        ASSEMBLER,
        B,
        BASIC,
        C,
        CSS,
        CSHARP,
        CPP,
        OBJECT_C,
        SWIFT,
        KOTLIN,
        FSHARP,
        FORTRAN,
        JAVA,
        JAVA_SCRIPT,
        PASCAL,
        PYTHON,
        HTML,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name = "name", unique = true)
    public String name;

    @Column(name = "public")
    public Boolean isPublic = true;

    @Column(name = "lang")
    public Language language;

    @Column(name = "git", unique = true)
    public String gitUrl;

    @JsonIgnore
    @ManyToOne
    public User owner;

    @ManyToMany(mappedBy = "contributing", fetch = FetchType.LAZY)
    public List<User> contributors;
}
