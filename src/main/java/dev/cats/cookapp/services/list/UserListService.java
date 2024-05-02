package dev.cats.cookapp.services.list;

import dev.cats.cookapp.dto.request.UserListRequest;
import dev.cats.cookapp.dto.response.UserListResponse;

import java.util.List;

public interface UserListService {
    List<UserListResponse> getAllLists();
    UserListResponse getList(String name);
    UserListResponse getFavesList();
    UserListResponse createList(UserListRequest userListRequest);
    UserListResponse updateList(UserListRequest userListRequest);
    void deleteList(Long id);
}
