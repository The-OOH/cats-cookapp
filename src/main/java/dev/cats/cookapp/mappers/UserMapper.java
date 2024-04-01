package dev.cats.cookapp.mappers;

import dev.cats.cookapp.dto.request.UserRequest;
import dev.cats.cookapp.dto.response.UserResponse;
import dev.cats.cookapp.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toDto(User user);
    User toEntity(UserRequest userRequest);
}
