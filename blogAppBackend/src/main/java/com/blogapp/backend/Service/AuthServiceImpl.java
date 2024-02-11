package com.blogapp.backend.Service;

import com.blogapp.backend.Entity.User;
import com.blogapp.backend.Repository.UserRepository;
import com.blogapp.backend.Utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    @Override
    public String login(String username, String password) {
        var authToken = new UsernamePasswordAuthenticationToken(username,password);
        var authenticate = authenticationManager.authenticate(authToken);

        if(authenticate == null)
            throw new RuntimeException("Bad Credentials");

        return JwtUtils.generateToken(((UserDetails)(authenticate.getPrincipal())).getUsername());
    }

    @Override
    public User register(String username, String emailid, String password) {
        if(userService.existsByUsername(username)){
            throw new RuntimeException("User Already Exists");
        }

        var encodedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setEmailid(emailid);
        user.setUsername(username);
        user.setPassword(encodedPassword);
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        user.setTimestamp(timeStamp);

        return userService.createUser(user);
    }
}
