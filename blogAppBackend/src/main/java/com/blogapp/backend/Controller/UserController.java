package com.blogapp.backend.Controller;

import com.blogapp.backend.Entity.Post;
import com.blogapp.backend.Entity.User;
import com.blogapp.backend.Service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<String> getUserByUsername(@PathVariable String username){
        var user = userService.getUserByUsername(username);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Details Not Found");
        }

        return new ResponseEntity(user,HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username){
        try {
            userService.deleteUser(username);
            return ResponseEntity.ok("User Deleted Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in deleting user" + e.getMessage());
        }
    }

    @PutMapping("/{userid}")
    public ResponseEntity<String> updatePost(@PathVariable Long userid, @RequestBody User user){
        User users = null;
        try {
            users = userService.updateUser(userid,user);
            return new ResponseEntity(users,HttpStatus.OK);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User not exist with post id"+e.getMessage());
        }
    }

}
