package com.blogapp.backend.Service;

import com.blogapp.backend.Entity.Comment;
import com.blogapp.backend.Entity.Post;
import com.blogapp.backend.Repository.CommentRespository;
import com.blogapp.backend.Repository.PostRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRespository commentRespository;

    @Value("${gcs.bucket.name}")
    private String gcsBucketName;

    public Post createPost(Post post) throws Exception{
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        post.setTimestamp(timeStamp);
        return postRepository.save(post);
    }

    public String uploadFileToGCS(MultipartFile file) throws IOException {
        if (!isImage(file)) {
            throw new IllegalArgumentException("Invalid image file");
        }

        ClassPathResource resource = new ClassPathResource("src/main/resources/crested-photon-411702-64654c860336.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());

        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        String gcsObjectName = "uploads/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();

        BlobInfo blobInfo = BlobInfo.newBuilder(gcsBucketName, gcsObjectName).build();

        byte[] fileBytes = file.getBytes();
        storage.create(blobInfo,fileBytes);

        return storage.get(gcsBucketName, gcsObjectName).getMediaLink();
    }

    private boolean isImage(MultipartFile file) throws IOException {
        Tika tika = new Tika();
        String mimeType = tika.detect(file.getInputStream());
        return mimeType.startsWith("image/");
    }


    public List<Post> getAllPostSearch(String title){
        List<Post> result = postRepository.findByTitleContainingIgnoreCase(title);
        if(result.size() == 0)
            throw new RuntimeException("No Posts Found");

        return result;
    }

    public Post getSinglePostDetailsById(String id){
        return postRepository.findById(id).orElse(null);
    }

    public List<Post> getPostBySpecificUserById(String userid){
        List<Post> result = postRepository.findPostByUserid(userid);
        if(result.size() == 0){
            throw new RuntimeException("No Commen Found By User");
        }
        return result;
    }

    public Post updatePost(String postId, Post post) throws Exception {
        Post updatePost = postRepository.findById(postId).orElseThrow(()->new Exception("Post not exist with post id: " + postId));

        updatePost.setTitle(post.getTitle());
        updatePost.setDes(post.getDes());
        updatePost.setPhoto(post.getPhoto());
        updatePost.setUsername(post.getUsername());
        updatePost.setUserid(post.getUserid());
        updatePost.setCategories(post.getCategories());
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        updatePost.setTimestamp(timeStamp);

        return postRepository.save(updatePost);
    }

    public boolean deletePost(String postid) throws Exception {
        Post post = postRepository.findById(postid).orElseThrow(()-> new Exception("No Posts Found"));
        postRepository.delete(post);
        List<Comment> comments = commentRespository.findCommentsByPostid(postid);
        if(comments.size() != 0){
            for(Comment comment: comments){
                commentRespository.delete(comment);
            }
        }

        return true;
    }
}
