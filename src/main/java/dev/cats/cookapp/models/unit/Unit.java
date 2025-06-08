package dev.cats.cookapp.models.unit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "units")
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "name_plural", nullable = false, unique = true)
    private String abbreviation;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UnitMeasurementType type = UnitMeasurementType.metric;
}
