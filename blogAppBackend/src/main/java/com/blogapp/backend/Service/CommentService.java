package com.blogapp.backend.Service;

import com.blogapp.backend.Entity.Comment;
import com.blogapp.backend.Repository.CommentRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRespository commentRespository;

    public Comment createComment(Comment comment){
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        comment.setTimestamp(timeStamp);
        return commentRespository.save(comment);
    }

    public boolean deleteComment(String commentid) throws Exception {
        Comment comment = commentRespository.findById(commentid).orElseThrow(()-> new Exception("No Comment Found"));
        commentRespository.delete(comment);
        return true;
    }

    public List<Comment> getAllCommentsPost(String postid){
        List<Comment> result = commentRespository.findCommentsByPostid(postid);
        if(result.size() == 0){
            throw new RuntimeException("No comments present in the post");
        }
        return result;
    }

    public Comment updateComment(String commentid, Comment comment) throws Exception {
        Comment updateComment = commentRespository.findById(commentid).orElseThrow(()->new Exception("Post not exist with post id: " + commentid));

        updateComment.setComment(comment.getComment());
        updateComment.setAuthor(comment.getAuthor());
        updateComment.setUserid(comment.getUserid());
        updateComment.setPostid(comment.getPostid());
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        updateComment.setTimestamp(timeStamp);

        return commentRespository.save(updateComment);
    }
}
