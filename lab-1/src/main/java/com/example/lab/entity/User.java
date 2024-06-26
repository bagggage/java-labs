package com.example.lab.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Git> ownedRepositories;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.REFRESH)
    private List<Git> contributing;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Link> links;

    @Column(name = "password")
    private String passwordHash;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder gitHashCodeBuilder = new HashCodeBuilder();

        for (Git git : contributing) {
            gitHashCodeBuilder.append(git.getId());
        }

        gitHashCodeBuilder.append(-1); // Divider   

        for (Git git : ownedRepositories) {
            gitHashCodeBuilder.append(git.getId());
        }

        return new HashCodeBuilder()
                    .append(id)
                    .append(name)
                    .append(username)
                    .append(email)
                    .append(passwordHash)
                    .append(ownedRepositories)
                    .append(gitHashCodeBuilder.toHashCode())
                    .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }

        User other = (User) o;

        if (this.links != other.links
            || (this.links != null && this.links.size() != other.links.size())) {
            return false;
        }

        if (this.ownedRepositories != other.ownedRepositories
            || (this.links != null && this.ownedRepositories.size() != other.ownedRepositories.size())) {
            return false;
        }

        if (this.contributing != other.contributing
            || (this.links != null && this.contributing.size() != other.contributing.size())) {
            return false;
        }

        if (this.links != null) {
            for (int i = 0; i < this.links.size(); ++i) {
                if (this.links.get(i).getId() != other.links.get(i).getId()) {
                    return false;
                }
            }
        }

        if (this.ownedRepositories != null) {
            for (int i = 0; i < this.ownedRepositories.size(); ++i) {
                if (this.ownedRepositories.get(i).getId() != other.ownedRepositories.get(i).getId()) {
                    return false;
                }
            }
        }

        if (this.contributing != null) {
            for (int i = 0; i < this.contributing.size(); ++i) {
                if (this.contributing.get(i).getId() != other.contributing.get(i).getId()) {
                    return false;
                }
            }
        }

        return new EqualsBuilder()
                    .append(this.id, other.id)
                    .append(this.name, other.name)
                    .append(this.username, other.username)
                    .append(this.email, other.email)
                    .append(this.passwordHash, other.passwordHash)
                    .isEquals();
    }
}
