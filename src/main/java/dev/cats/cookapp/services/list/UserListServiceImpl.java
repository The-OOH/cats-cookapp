package dev.cats.cookapp.services.list;

import dev.cats.cookapp.dto.request.UserListRequest;
import dev.cats.cookapp.dto.response.UserListResponse;
import dev.cats.cookapp.mappers.UserListMapper;
import dev.cats.cookapp.models.UserList;
import dev.cats.cookapp.models.User;
import dev.cats.cookapp.repositories.RecipeRepository;
import dev.cats.cookapp.repositories.UserListRepository;
import dev.cats.cookapp.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserListServiceImpl implements UserListService {
    private final UserListRepository userListRepository;
    private final UserService userService;
    private final UserListMapper userListMapper;
    private final RecipeRepository recipeRepository;

    @Override
    public List<UserListResponse> getAllLists() {
        var userEntity = getCurrentUser();
        return userListRepository.findAllByUserId(userEntity.getId()).stream().map(userListMapper::toDto).toList();
    }

    @Override
    public UserListResponse getList(String name) {
        var user = getCurrentUser();
        return userListMapper.toDto(userListRepository.findByNameAndUserId(name, user.getId()).orElseThrow
                (() -> new RuntimeException("List not found")));
    }

    @Override
    public UserListResponse getFavesList() {
        var user = getCurrentUser();
        return userListMapper.toDto(userListRepository.findByNameAndUserId("Faves", user.getId()).orElseThrow
                (() -> new RuntimeException("Faves list not found")));
    }

    @Override
    public UserListResponse createList(UserListRequest userListRequest) {
        var user = getCurrentUser();
        if (userListRepository.findByNameAndUserId(userListRequest.name(), user.getId()).isPresent()) {
            throw new RuntimeException("List with this name already exists");
        }
        var list = new UserList();
        list.setName(userListRequest.name());
        var recipes = recipeRepository.findAllById(userListRequest.recipeIds());
        list.setRecipes(new HashSet<>(recipes));
        userListRepository.save(list);
        return userListMapper.toDto(list);
    }

    @Override
    public UserListResponse updateList(UserListRequest userListRequest) {
        var user = getCurrentUser();
        var list = userListRepository.findByNameAndUserId(userListRequest.name(), user.getId()).orElseThrow(() ->
                new RuntimeException("List not found"));
        list.setName(userListRequest.name());
        var recipes = recipeRepository.findAllById(userListRequest.recipeIds());
        list.setRecipes(new HashSet<>(recipes));
        userListRepository.save(list);
        return userListMapper.toDto(list);
    }

    @Override
    public void deleteList(Long id) {
        userListRepository.deleteById(id);
    }

    private User getCurrentUser() {
        return userService.getCurrentUser().orElseThrow(() -> new RuntimeException("User not found"));
    }
}
