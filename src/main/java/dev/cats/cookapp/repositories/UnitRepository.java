package dev.cats.cookapp.repositories;

import dev.cats.cookapp.models.unit.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnitRepository extends JpaRepository<Unit, Long> {
    List<Unit> findTop10ByNameIgnoreCaseContaining(String namePart);
}
