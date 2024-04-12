package dev.cats.cookapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name", nullable = false)
    private String username;

    public User() {
    }

    public User(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public User(Long id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
    }

    @OneToMany(mappedBy = "created_by")
    private Set<Recipe> recipes = new HashSet<>();

}