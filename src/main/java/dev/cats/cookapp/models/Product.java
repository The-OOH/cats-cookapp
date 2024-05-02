package dev.cats.cookapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "products")
@Indexed
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "aisle")
    private String aisle;

    @Column(name = "image")
    private String image;

    @Column(name = "name", nullable = false)
    @FullTextField
    private String name;

    @OneToMany(mappedBy = "product")
    private Set<RecipeIngredient> recipeIngredients = new HashSet<>();

}