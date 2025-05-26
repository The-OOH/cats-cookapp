package dev.cats.cookapp.models.recipe;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

@Getter
@Setter
@Entity
@Table(name = "ingredients")
@Indexed
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic(optional = false)
    @Column(nullable = false)
    private String name;
}