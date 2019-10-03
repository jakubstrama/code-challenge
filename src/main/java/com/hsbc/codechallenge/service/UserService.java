package com.hsbc.codechallenge.service;

import com.hsbc.codechallenge.exceptions.runtime.UserAlreadyFollowedException;
import com.hsbc.codechallenge.exceptions.runtime.UserNotFollowedException;
import com.hsbc.codechallenge.exceptions.runtime.UserNotFoundException;
import com.hsbc.codechallenge.persistence.UserRepository;
import com.hsbc.codechallenge.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity findUser(String handle) {
        return userRepository.getUsers()
                .stream()
                .filter(user -> user.getHandle().equals(handle))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(handle));
    }

    public void followUser(String followedHandle, String followerHandle) {
        UserEntity followed = findUser(followedHandle);
        UserEntity follower = findUser(followerHandle);

        if (followed.getFollowers().contains(follower)) {
            throw new UserAlreadyFollowedException();
        }

        userRepository.addFollower(followed, follower);
    }

    public void unFollowUser(String followedHandle, String followerHandle) {
        UserEntity followed = findUser(followedHandle);
        UserEntity follower = findUser(followerHandle);

        if (!followed.getFollowers().contains(follower)) {
            throw new UserNotFollowedException();
        }

        userRepository.removeFollower(followed, follower);
    }

    public UserEntity createUser(String handle) {
        return userRepository.addUser(UserEntity.of(UUID.randomUUID(), handle));
    }

}
