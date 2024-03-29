package com.blogapp.backend.Controller;

import com.blogapp.backend.DTO.AuthRequestDto;
import com.blogapp.backend.DTO.RegisterAuthResponseDto;
import com.blogapp.backend.Entity.User;
import com.blogapp.backend.Service.AuthService;
import com.blogapp.backend.Service.UserService;
import com.blogapp.backend.Utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequestDto authRequestDto, HttpServletResponse response) {
        try {
            var jwtToken = authService.login(authRequestDto.username(), authRequestDto.password());
            var user = userService.getUserByUsername(authRequestDto.username());
            long cookieMaxAgeInSeconds = 1 * 60 * 60;

            ResponseCookie cookie = ResponseCookie.from("token", jwtToken)
                    .httpOnly(true)
                    .sameSite("None")
                    .path("/")
                    .maxAge((int) cookieMaxAgeInSeconds)
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            Map<String, String> responseBody = Map.of(
                    "userid", String.valueOf(user.getUserid()),
                    "username", user.getUsername(),
                    "email", user.getEmailid()
            );

            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid username or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterAuthResponseDto> register(@RequestBody AuthRequestDto authRequestDto) {
        try {
            var user = authService.register(authRequestDto.username(), authRequestDto.email(), authRequestDto.password());
            var authResponse = new RegisterAuthResponseDto(user, AuthStatus.USER_CREATED_SUCCESSFULLY);
            return ResponseEntity.status(HttpStatus.OK).body(authResponse);
        } catch (Exception e) {
            var authResponse = new RegisterAuthResponseDto(null, AuthStatus.USER_ALREADY_EXITS);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(authResponse);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext(); // Clear security context

        // Expire or delete the authentication token cookie on the client side
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok("Logout successful");
    }

    @GetMapping("/refetch")
    public ResponseEntity<String> refetch(@CookieValue(name = "token", required=false) String token){
        if(token == null || token.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token verification failed");
        }
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(JwtUtils.getSignKey())
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            var user = userService.getUserByUsername(username);
            ObjectMapper objectMapper = new ObjectMapper();

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(user));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token verification failed: " + e.getMessage());
        }
    }
}
