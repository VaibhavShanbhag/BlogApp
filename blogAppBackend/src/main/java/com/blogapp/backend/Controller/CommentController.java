package com.blogapp.backend.Controller;

import com.blogapp.backend.Entity.Comment;
import com.blogapp.backend.Entity.Post;
import com.blogapp.backend.Service.CommentService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<String> createComment(@RequestBody Comment comment){
        Comment comments = null;
        try {
            comments = commentService.createComment(comment);
            return new ResponseEntity(comments, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in creating comment");
        }
    }

    @DeleteMapping("/{commentid}")
    public ResponseEntity<String> deleteComment(@PathVariable String commentid){
        try{
            commentService.deleteComment(commentid);
            return ResponseEntity.ok("Deleted Comment Successfully");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in deleting comment");
        }
    }

    @GetMapping("/post/{postid}")
    public ResponseEntity<String> getAllCommentsPost(@PathVariable String postid){
        try{
            List<Comment> results = commentService.getAllCommentsPost(postid);
            return new ResponseEntity(results,HttpStatus.OK);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No comments present in the post");
        }
    }

    @PutMapping("/{commentid}")
    public ResponseEntity<String> updateComment(@PathVariable String commentid, @RequestBody Comment comment){
        Comment comments = null;
        try {
            comments = commentService.updateComment(commentid,comment);
            return new ResponseEntity(comments,HttpStatus.OK);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot edit comments of this post");
        }
    }


}
