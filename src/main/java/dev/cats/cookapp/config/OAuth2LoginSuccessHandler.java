package dev.cats.cookapp.config;

import dev.cats.cookapp.services.user.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final UserService userService;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication
    ) throws ServletException, IOException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        Map<String, Object> attributes = ((DefaultOAuth2User) authentication.getPrincipal()).getAttributes();
        String email = attributes.getOrDefault("email", "").toString();
        userService.getUser(email)
                .ifPresentOrElse(student -> {
                    authenticateUser(attributes, "ROLE_USER", oAuth2AuthenticationToken);
                }, () -> {
                    handleUserNotFound(attributes, email, oAuth2AuthenticationToken);
                });

        redirectToFrontend(request, response);
    }

    private void authenticateUser(Map<String, Object> attributes,
                                  String role, OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        DefaultOAuth2User newUser = new DefaultOAuth2User(List.of(new SimpleGrantedAuthority(role)),
                attributes, "email");
        Authentication newAuth = new OAuth2AuthenticationToken(newUser, List.of(new SimpleGrantedAuthority(role)),
                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    private void handleUserNotFound(Map<String, Object> attributes, String email, OAuth2AuthenticationToken oAuth2AuthenticationToken) {

    }

    private void redirectToFrontend(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl(frontendUrl);
        super.onAuthenticationSuccess(request, response, SecurityContextHolder.getContext().getAuthentication());
    }
}
