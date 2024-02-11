package com.blogapp.backend.DTO;

import com.blogapp.backend.Controller.AuthStatus;
import com.blogapp.backend.Entity.User;

public record RegisterAuthResponseDto(User user, AuthStatus authStatus) {

}
