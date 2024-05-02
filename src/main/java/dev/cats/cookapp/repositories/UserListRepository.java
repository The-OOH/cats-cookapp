package dev.cats.cookapp.repositories;

import dev.cats.cookapp.models.UserList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserListRepository extends JpaRepository<UserList, Long> {
    Optional<UserList> findByName(String name);
    Optional<UserList> findByNameAndUserId(String name, Long userId);
    List<UserList> findAllByUserId(Long userId);
    void deleteById(Long id);
}