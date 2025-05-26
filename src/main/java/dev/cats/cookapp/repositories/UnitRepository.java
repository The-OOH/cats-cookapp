package dev.cats.cookapp.repositories;

import dev.cats.cookapp.models.unit.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit, Long> {
}
