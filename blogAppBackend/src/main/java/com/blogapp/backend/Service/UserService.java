package com.blogapp.backend.Service;

import com.blogapp.backend.Entity.Comment;
import com.blogapp.backend.Entity.Post;
import com.blogapp.backend.Entity.User;
import com.blogapp.backend.Repository.CommentRespository;
import com.blogapp.backend.Repository.PostRepository;
import com.blogapp.backend.Repository.UserRepository;
import com.blogapp.backend.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserDetailsService userDetailsService = null;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRespository commentRespository;
    @Autowired
    private PostRepository postRepository;

    public User createUser(User user){
        return userRepository.save(user);
    }

    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        if(user == null)
            throw new UsernameNotFoundException("Username Not Found");

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

    public boolean deleteUser(String username) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new Exception("No User Found");
        userRepository.delete(user);

        List<Post> posts = postRepository.findPostByUserid(String.valueOf(user.getUserid()));
        if(posts.size() != 0){
            for(Post post: posts){
                postRepository.delete(post);
            }
        }

        List<Comment> comments = commentRespository.findCommentsByUserid(String.valueOf(user.getUserid()));
        if(comments.size() != 0){
            for(Comment comment: comments){
                commentRespository.delete(comment);
            }
        }

        return true;
    }

    public User updateUser(Long userid, User user) throws Exception {
        User updateUser = userRepository.findByUserid(userid);

        if(updateUser == null)
            throw new Exception("No user Found");
        updateUser.setEmailid(user.getEmailid());
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        updateUser.setTimestamp(timeStamp);

        return userRepository.save(updateUser);
    }
}
