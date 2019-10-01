package com.hsbc.codechallenge.persistence;

import com.hsbc.codechallenge.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRepository {

    private List<UserEntity> users;

    public UserRepository() {
        this.users = new ArrayList<>();
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public UserEntity addUser(UserEntity user) {
        this.users.add(user);

        return user;
    }

    public void addFollower(UserEntity followed, UserEntity follower) {
        followed.getFollowers().add(follower);
    }

    public void removeFollower(UserEntity followed, UserEntity follower) {
        followed.getFollowers().remove(follower);
    }
}
