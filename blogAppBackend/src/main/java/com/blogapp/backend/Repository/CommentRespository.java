package com.blogapp.backend.Repository;

import com.blogapp.backend.Entity.Comment;
import com.blogapp.backend.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRespository extends JpaRepository<Comment,String> {
    List<Comment> findCommentsByPostid(String postid);
    List<Comment> findCommentsByUserid(String userId);
}
