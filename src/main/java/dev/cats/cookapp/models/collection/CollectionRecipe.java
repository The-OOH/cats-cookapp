package dev.cats.cookapp.models.collection;

import dev.cats.cookapp.models.recipe.Recipe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "collection_recipes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CollectionRecipe {
    @EmbeddedId
    private CollectionRecipeKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("collectionId")
    @JoinColumn(name="collection_id")
    private RecipesCollection collection;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recipeId")
    @JoinColumn(name="recipe_id")
    private Recipe recipe;

    @Column(name="added_at", updatable = false, nullable = false)
    @CreationTimestamp
    private Timestamp addedAt;

    public CollectionRecipe(final RecipesCollection coll, final Recipe rec) {
        collection = coll;
        recipe = rec;
        id = new CollectionRecipeKey(coll.getId(), rec.getId());
    }
}
