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
import lombok.Data;

@Data
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
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "public")
    private Boolean isPublic = true;

    @Column(name = "lang")
    private Language language;

    @Column(name = "git", unique = true)
    private String gitUrl;

    @JsonIgnore
    @ManyToOne
    private User owner;

    @ManyToMany(mappedBy = "contributing", fetch = FetchType.LAZY)
    private List<User> contributors;
}
