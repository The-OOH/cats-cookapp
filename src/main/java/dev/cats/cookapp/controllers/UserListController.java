package dev.cats.cookapp.controllers;

import dev.cats.cookapp.dto.request.UserListRequest;
import dev.cats.cookapp.dto.response.UserListResponse;
import dev.cats.cookapp.services.list.UserListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/lists")
@RequiredArgsConstructor
public class UserListController {
    private final UserListService userListService;

    @GetMapping
    public ResponseEntity<List<UserListResponse>> getAllLists() {
        return ResponseEntity.ok(userListService.getAllLists());
    }

    @GetMapping("/search")
    public ResponseEntity<UserListResponse> getList(@RequestParam String name) {
        return ResponseEntity.ok(userListService.getList(name));
    }

    @GetMapping("/faves")
    public ResponseEntity<UserListResponse> getFavesList() {
        return ResponseEntity.ok(userListService.getFavesList());
    }

    @PostMapping
    public ResponseEntity<UserListResponse> createList(@RequestBody UserListRequest userListRequest) {
        return ResponseEntity.ok(userListService.createList(userListRequest));
    }

    @PutMapping
    public ResponseEntity<UserListResponse> updateList(@RequestBody UserListRequest userListRequest) {
        return ResponseEntity.ok(userListService.updateList(userListRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable Long id) {
        userListService.deleteList(id);
        return ResponseEntity.noContent().build();
    }


}
