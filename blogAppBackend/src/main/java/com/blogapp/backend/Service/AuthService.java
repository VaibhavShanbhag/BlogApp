package com.blogapp.backend.Service;

import com.blogapp.backend.Entity.User;

public interface AuthService {
    String login(String username, String password);

    User register(String username, String email, String password);
}
