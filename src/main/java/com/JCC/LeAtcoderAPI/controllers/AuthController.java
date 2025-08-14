package com.JCC.LeAtcoderAPI.controllers;

import com.JCC.LeAtcoderAPI.Model.ControllerObjects.AuthResponseBody;
import com.JCC.LeAtcoderAPI.Model.ServiceObjects.Limits;
import com.JCC.LeAtcoderAPI.Model.User.User;
import com.JCC.LeAtcoderAPI.security.JwtService;
import com.JCC.LeAtcoderAPI.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

    private Cookie createTokenCookie(String token) {
        Cookie cookie = new Cookie("refresh-token", token);
        cookie.setMaxAge(Limits.REFRESH_EXPIRY_IN_SECONDS);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    // keep in mind default oauth endpoint in spring is /oauth2/authorization/google

    @GetMapping("/complete")
    @ResponseBody
    public ResponseEntity<?> completeLogin(
            @RequestParam(name = "token") String token,
            HttpServletResponse response
            ) {
        String userId = jwtService.extractToken(token);
        if (userId == null) return new ResponseEntity<>("token is invalid", HttpStatus.FORBIDDEN);
        User user = userService.getAllUserInfo(userId);
        if (user == null) return new ResponseEntity<>("user not found in database",HttpStatus.BAD_REQUEST);

        System.out.println("extracted userid: " + user._id());
        String refreshToken = jwtService.createTokenBySubAndExpiry(user._id(), Limits.REFRESH_EXPIRY_IN_SECONDS);
        Cookie cookie = createTokenCookie(refreshToken);
        response.addCookie(cookie);

        String accessToken = jwtService.createTokenBySubAndExpiry(user._id(), Limits.ACCESS_EXPIRY_IN_SECONDS);
        return new ResponseEntity<>(new AuthResponseBody(accessToken), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    @ResponseBody
    public ResponseEntity<?> refresh(
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return new ResponseEntity<>("no cookies sent to refresh", HttpStatus.FORBIDDEN);
        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if ("refresh-token".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
            }
        }
        if (refreshToken == null) return new ResponseEntity<>("refresh token not found in cookies", HttpStatus.FORBIDDEN);

        String userId = jwtService.extractToken(refreshToken);
        if (userId == null) return new ResponseEntity<>("refresh token invalid", HttpStatus.FORBIDDEN);
        if (userService.getAllUserInfo(userId) == null) return new ResponseEntity<>("user not in database", HttpStatus.FORBIDDEN);

        String newRefreshToken = jwtService.createTokenBySubAndExpiry(userId, Limits.REFRESH_EXPIRY_IN_SECONDS);
        Cookie cookie = createTokenCookie(newRefreshToken);
        response.addCookie(cookie);

        String newAccessToken = jwtService.createTokenBySubAndExpiry(userId, Limits.ACCESS_EXPIRY_IN_SECONDS);
        return new ResponseEntity<>(new AuthResponseBody(newAccessToken), HttpStatus.OK);
    }
}
