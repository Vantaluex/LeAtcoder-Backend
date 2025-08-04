package com.JCC.LeAtcoderAPI.security;

import com.JCC.LeAtcoderAPI.Model.User.User;
import com.JCC.LeAtcoderAPI.repositories.UserRepository;
import com.JCC.LeAtcoderAPI.services.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.IOException;

public class Oauth2SuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {
    @Autowired
    private UserService userService;
    @Autowired
    private Dotenv dotenv;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String googleId = oAuth2User.getAttribute("sub");

        User user = userService.findOrCreateByGoogleId(googleId);
        String tempToken = jwtService.createTempToken(user);

        String redirectUrl = dotenv.get("AUTH_REDIRECT_URL");
        response.sendRedirect(redirectUrl + "?tempToken=" + tempToken);
    }
}
