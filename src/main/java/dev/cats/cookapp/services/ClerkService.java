package dev.cats.cookapp.services;

import com.clerk.backend_api.Clerk;
import com.clerk.backend_api.models.components.User;
import com.clerk.backend_api.models.operations.GetUserResponse;
import com.clerk.backend_api.models.errors.ClerkErrors;
import dev.cats.cookapp.dtos.UserDetails;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ClerkService {
    @Value("${clerk.secret-key}")
    private String clerkSecretKey;
    private Clerk sdk;

    @PostConstruct
    public void init() {
        sdk = Clerk.builder()
                .bearerAuth(this.clerkSecretKey)
                .build();
    }

    public Optional<UserDetails> getUserDetailsById(final String id) {
        if (null == id) {
            return Optional.empty();
        }
        try {
            final GetUserResponse res = this.sdk.users()
                    .get()
                    .userId(id)
                    .call();
            if (res.user().isPresent() && res.user().get().username().isPresent()) {
                return Optional.of(this.getUserDetails(res.user().get()));
            } else {
                return Optional.empty();
            }
        } catch (final ClerkErrors e) {
            return Optional.empty();
        } catch (final Exception e) {
            throw new RuntimeException("Error while getting user name by id: "
                    + e.getMessage(), e);
        }
    }

    public List<UserDetails> listAllUsers() {
        try {
            final var res = this.sdk.users()
                    .list()
                    .call();
            return res.userList()
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(this::getUserDetails
                    )
                    .collect(Collectors.toList());
        } catch (final ClerkErrors e) {
            throw new RuntimeException("Clerk API error: "
                    + e.getMessage(), e);
        } catch (final Exception e) {
            throw new RuntimeException("Error while getting users: "
                    + e.getMessage(), e);
        }
    }

    private UserDetails getUserDetails(final User u) {
        final String first = u.firstName().isPresent() ? u.firstName().get() : "";
        final String last = u.lastName().isPresent() ? u.lastName().get() : "";
        final String username = u.username().isPresent() ? u.username().get() : "";

        String fullName = Stream.of(first, last)
                .filter(s -> null != s && !s.isEmpty())
                .collect(Collectors.joining(" "));

        if (fullName.isEmpty()) {
            fullName = null != username && !username.isEmpty() ? username : null;
        }

        return UserDetails.builder()
                .id(u.id().orElse(null))
                .name(fullName)
                .build();
    }
}
