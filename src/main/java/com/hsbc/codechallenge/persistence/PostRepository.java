package com.hsbc.codechallenge.persistence;

import com.hsbc.codechallenge.persistence.entity.PostEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostRepository {

    private List<PostEntity> posts;

    public PostRepository() {
        this.posts = new ArrayList<>();
    }

    public List<PostEntity> getPosts() {
        return posts;
    }

    public PostEntity addPost(PostEntity postEntity) {
        this.posts.add(postEntity);
        return  postEntity;
    }

}
