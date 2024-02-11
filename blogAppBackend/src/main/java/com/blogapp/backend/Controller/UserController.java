package com.blogapp.backend.Controller;

import com.blogapp.backend.Entity.User;
import com.blogapp.backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/register")
//    public User createUser(@RequestBody User user){
//        return userService.createUser(user);
//    }
//
//    @GetMapping("/getallusers")
//    public List<User> getAllUsers(){
//        return userService.getAllUsers();
//    }
//
//    @GetMapping("/getUser/{id}")
//    public Optional<User> getUserById(@PathVariable Long id){
//        return userService.getUserById(id);
//    }

}
