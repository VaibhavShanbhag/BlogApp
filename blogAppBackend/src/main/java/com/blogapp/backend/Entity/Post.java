package com.blogapp.backend.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String postid;
    @Column(nullable = false, unique = true)
    private String title;
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String des;
    private String photo;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String userid;
    private String[] categories;
    @Column(nullable = false)
    private String timestamp;
}
