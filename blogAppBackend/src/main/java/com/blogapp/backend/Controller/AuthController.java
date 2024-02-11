package com.blogapp.backend.Controller;

import com.blogapp.backend.DTO.AuthRequestDto;
import com.blogapp.backend.DTO.RegisterAuthResponseDto;
import com.blogapp.backend.Service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequestDto authRequestDto, HttpServletResponse response){
        try{
            var jwtToken = authService.login(authRequestDto.username(),authRequestDto.password());
            Cookie cookie = new Cookie("token", jwtToken);
            cookie.setMaxAge(150);
            cookie.setPath("/");
            response.addCookie(cookie);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully Logged In");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

    }

    @PostMapping("/register")
    public ResponseEntity<RegisterAuthResponseDto> register(@RequestBody AuthRequestDto authRequestDto){
        try{
            var user = authService.register(authRequestDto.username(), authRequestDto.email(), authRequestDto.password());
            var authResponse = new RegisterAuthResponseDto(user,AuthStatus.USER_CREATED_SUCCESSFULLY);
            return ResponseEntity.status(HttpStatus.OK).body(authResponse);
        }
        catch (Exception e){
            var authResponse = new RegisterAuthResponseDto(null,AuthStatus.USER_ALREADY_EXITS);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(authResponse);
        }

    }
}
