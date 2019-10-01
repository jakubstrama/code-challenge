package com.hsbc.codechallenge.service;

import com.hsbc.codechallenge.exceptions.runtime.UserNotFoundException;
import com.hsbc.codechallenge.persistence.PostRepository;
import com.hsbc.codechallenge.persistence.entity.PostEntity;
import com.hsbc.codechallenge.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    public List<PostEntity> getWall(String handle) {
        UserEntity user = userService.findUser(handle);

        return postRepository.getPosts()
                .stream()
                .filter(post -> post.getAuthor().equals(user))
                .sorted(Comparator.comparing(PostEntity::getTimestamp, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public List<PostEntity> getTimeline(String handle) {
        UserEntity user = userService.findUser(handle);

        return postRepository.getPosts()
                .stream()
                .filter(post -> post.getAuthor().getFollowers().contains(user))
                .sorted(Comparator.comparing(PostEntity::getTimestamp, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public PostEntity newPost(String message, String handle) {
        UserEntity author;
        try {
            author = userService.findUser(handle);
        } catch (UserNotFoundException e) {
            author = userService.createUser(handle);
        }

        PostEntity newPost = new PostEntity(UUID.randomUUID(), author, message);
        newPost.setTimestamp(LocalDateTime.now());

        return postRepository.addPost(newPost);
    }

}
