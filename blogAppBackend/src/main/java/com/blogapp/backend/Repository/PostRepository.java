package com.blogapp.backend.Repository;

import com.blogapp.backend.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    @Query("SELECT e FROM Post e WHERE LOWER(e.title) LIKE %:search%")
    List<Post> findByTitleContainingIgnoreCase(String search);
    List<Post> findPostByUserid(String userid);
}
