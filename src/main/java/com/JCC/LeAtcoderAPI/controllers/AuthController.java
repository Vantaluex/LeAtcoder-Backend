package com.JCC.LeAtcoderAPI.controllers;

import com.JCC.LeAtcoderAPI.Model.ControllerObjects.CompleteBody;
import com.JCC.LeAtcoderAPI.Model.ServiceObjects.Limits;
import com.JCC.LeAtcoderAPI.Model.User.User;
import com.JCC.LeAtcoderAPI.security.JwtService;
import com.JCC.LeAtcoderAPI.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

    private Cookie createTokenCookie(String token) {
        Cookie cookie = new Cookie("jwt-token", token);
        cookie.setMaxAge(Limits.REFRESH_EXPIRY_IN_SECONDS);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    @GetMapping("/complete")
    public ResponseEntity<?> completeLogin(
            @RequestParam(name = "token") String token,
            HttpServletResponse response
            ) {
        String userId = jwtService.extractToken(token);
        if (userId == null) return new ResponseEntity<>("token is invalid", HttpStatus.FORBIDDEN);
        User user = userService.getAllUserInfo(userId);
        if (user == null) return new ResponseEntity<>("user not found in database",HttpStatus.BAD_REQUEST);

        String refreshToken = jwtService.createTokenBySubAndExpiry(user._id(), Limits.REFRESH_EXPIRY_IN_SECONDS);
        Cookie cookie = createTokenCookie(refreshToken);
        response.addCookie(cookie);

        String accessToken = jwtService.createTokenBySubAndExpiry(user._id(), Limits.ACCESS_EXPIRY_IN_SECONDS);
        return ResponseEntity.ok(new CompleteBody(accessToken));
    }
}
