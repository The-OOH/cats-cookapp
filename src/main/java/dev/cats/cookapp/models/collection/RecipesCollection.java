package dev.cats.cookapp.models.collection;

import dev.cats.cookapp.models.recipe.Recipe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "collections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipesCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic(optional = false)
    @Column(nullable = false)
    private String userId;

    @Basic(optional = false)
    @Column(nullable = false)
    private String name;

    @Basic(optional = false)
    @Column(nullable = false)
    private String description;

    @Column(name = "created_at", updatable = false, nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Basic(optional = false)
    @Column(nullable = false)
    private Timestamp updatedAt = new Timestamp(System.currentTimeMillis());

    @OneToMany(
            mappedBy = "collection",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<CollectionRecipe> collectionRecipes = new HashSet<>();

    public void addRecipe(Recipe recipe) {
        CollectionRecipe cr = new CollectionRecipe(this, recipe);
        this.collectionRecipes.add(cr);
    }

    public void removeRecipe(Recipe recipe) {
        this.collectionRecipes.removeIf(cr -> cr.getRecipe().equals(recipe));
    }

    @Transient
    public Set<Recipe> getRecipes() {
        return this.collectionRecipes.stream()
                .map(CollectionRecipe::getRecipe)
                .collect(Collectors.toSet());
    }
}
