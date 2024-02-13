package com.blogapp.backend.Controller;

import com.blogapp.backend.DTO.AuthRequestDto;
import com.blogapp.backend.DTO.RegisterAuthResponseDto;
import com.blogapp.backend.Entity.Post;
import com.blogapp.backend.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/")
    public ResponseEntity<String> getAllPost(@RequestParam String search){
        try{
            List<Post> results = postService.getAllPostSearch(search);
            return new ResponseEntity(results,HttpStatus.OK);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<String> getSinglePostDetailsById(@PathVariable String id){
        var post = postService.getSinglePostDetailsById(id);
        if(post == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post Details Not Found");
        }

        return new ResponseEntity(post,HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<String> getPostBySpecificUserById(@PathVariable String id){
        try{
            List<Post> results = postService.getPostBySpecificUserById(id);
            return new ResponseEntity(results,HttpStatus.OK);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No Posts Found By User");
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadPhotoPost(@RequestPart("file") MultipartFile file){
        try {
            String fileUrlLink = postService.uploadFileToGCS(file);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("photo",fileUrlLink));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file" + e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestBody Post post){
        Post posts = null;
        try {
            posts = postService.createPost(post);
            return new ResponseEntity(posts,HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in creating post");
        }
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<String> updatePost(@PathVariable String id, @RequestBody Post post){
        Post posts = null;
        try {
            posts = postService.updatePost(id,post);
            return new ResponseEntity(posts,HttpStatus.OK);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Post not exist with post id");
        }
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<String> deletePost(@PathVariable String id){
        try{
            postService.deletePost(id);
            return ResponseEntity.ok("Deleted Post Successfully");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in deleting post");
        }
    }
}
