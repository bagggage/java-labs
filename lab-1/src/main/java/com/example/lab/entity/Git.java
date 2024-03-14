package com.example.lab.entity;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

@Getter
@Setter
@RequiredArgsConstructor
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
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
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

    @ManyToMany(mappedBy = "contributing", cascade = CascadeType.REFRESH)
    private List<User> contributors;

    @Override
    public int hashCode() {
        HashCodeBuilder gitHashCodeBuilder = new HashCodeBuilder();

        for (User user : contributors) {
            gitHashCodeBuilder.append(user.getId());
        }

        return new HashCodeBuilder()
                    .append(id)
                    .append(name)
                    .append(isPublic)
                    .append(language)
                    .append(gitUrl)
                    .append(owner.getId())
                    .append(gitHashCodeBuilder.toHashCode())
                    .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Git)) return false;

        Git other = (Git)o;

        boolean isContributorsEquals = (this.contributors == other.contributors);

        if (!isContributorsEquals && this.contributors != null && other.contributors != null
            && this.contributors.size() == other.contributors.size()) {

            for (int i = 0; i < this.contributors.size(); ++i) {
                if (this.contributors.get(i).getId() != other.contributors.get(i).getId()) return false;
            }
        }

        return new EqualsBuilder()
                    .append(this.id, other.id)
                    .append(this.name, other.name)
                    .append(this.isPublic, other.isPublic)
                    .append(this.owner.getId(), other.owner.getId())
                    .isEquals();
    }
}
