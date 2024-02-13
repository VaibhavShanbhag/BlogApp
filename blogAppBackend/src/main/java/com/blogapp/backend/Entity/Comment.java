package com.blogapp.backend.Entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private String commentid;
    @Column(nullable = false)
    private String comment;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private String postid;
    @Column(nullable = false)
    private String userid;
    @Column(nullable = false)
    private String timestamp;
}
