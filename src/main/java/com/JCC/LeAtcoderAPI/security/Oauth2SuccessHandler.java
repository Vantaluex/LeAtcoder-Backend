package com.JCC.LeAtcoderAPI.security;

import com.JCC.LeAtcoderAPI.Model.ServiceObjects.Limits;
import com.JCC.LeAtcoderAPI.Model.User.User;
import com.JCC.LeAtcoderAPI.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Oauth2SuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Value("${auth.redirect.url}")
    private String redirectURL;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String googleId = oAuth2User.getAttribute("sub");

        User user = userService.findOrCreateByGoogleId(googleId);
        String tempToken = jwtService.createTokenBySubAndExpiry(user._id(), Limits.TEMP_EXPIRY_IN_SECONDS);

        response.sendRedirect(redirectURL + "?tempToken=" + tempToken);
    }
}
