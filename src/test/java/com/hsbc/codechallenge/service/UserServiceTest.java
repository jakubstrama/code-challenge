package com.hsbc.codechallenge.service;

import com.hsbc.codechallenge.exceptions.runtime.UserAlreadyFollowedException;
import com.hsbc.codechallenge.exceptions.runtime.UserNotFollowedException;
import com.hsbc.codechallenge.exceptions.runtime.UserNotFoundException;
import com.hsbc.codechallenge.persistence.UserRepository;
import com.hsbc.codechallenge.persistence.entity.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;


    @Test
    public void testFindUser() {
        // Given
        String handle1 = "user1";
        String handle2 = "user2";
        UserEntity expected = createUser(handle1);
        UserEntity user2 = createUser(handle2);
        List<UserEntity> allUsers = Arrays.asList(expected, user2);

        // When
        when(userRepository.getUsers()).thenReturn(allUsers);
        UserEntity given = userService.findUser(handle1);

        // Then
        assertThat(given).isEqualTo(expected);
    }

    @Test(expected = UserNotFoundException.class)
    public void testFindUser_no_users_found() {
        // Given
        String handle1 = "user1";
        String handle2 = "user2";
        String handle3 = "user3";
        UserEntity user1 = createUser(handle1);
        UserEntity user2 = createUser(handle2);
        List<UserEntity> allUsers = Arrays.asList(user1, user2);

        // When
        when(userRepository.getUsers()).thenReturn(allUsers);
        userService.findUser(handle3);

        // Then
        // UserNotFoundException
    }


    @Test
    public void testFollowUser() {
        // Given
        String followed = "user1";
        String follower = "user2";
        UserEntity followedUser = createUser(followed);
        UserEntity followerUser = createUser(follower);
        List<UserEntity> allUsers = Arrays.asList(followedUser, followerUser);

        // When
        when(userRepository.getUsers()).thenReturn(allUsers);
        userService.followUser(followed, follower);

        // Then
        verify(userRepository, times(1)).addFollower(followedUser, followerUser);
    }

    // UserAlreadyFollowedException
    @Test(expected = UserAlreadyFollowedException.class)
    public void testFollowUser_throws_UserAlreadyFollowedException() {
        // Given
        String followed = "user1";
        String follower = "user2";
        UserEntity followedUser = createUser(followed);
        UserEntity followerUser = createUser(follower);
        followedUser.getFollowers().add(followerUser);
        List<UserEntity> allUsers = Arrays.asList(followedUser, followerUser);

        // When
        when(userRepository.getUsers()).thenReturn(allUsers);
        userService.followUser(followed, follower);

        // Then
        // UserAlreadyFollowedException
    }

    @Test(expected = UserNotFollowedException.class)
    public void testUnfollowUser_throws_UserNotFollowedException() {
        // Given
        String followed = "user1";
        String follower = "user2";
        UserEntity followedUser = createUser(followed);
        UserEntity followerUser = createUser(follower);
        List<UserEntity> allUsers = Arrays.asList(followedUser, followerUser);

        // When
        when(userRepository.getUsers()).thenReturn(allUsers);
        userService.unFollowUser(followed, follower);

        // Then
        // UserNotFollowedException
    }

    @Test
    public void testUnfollowUser() {
        // Given
        String followed = "user1";
        String follower = "user2";
        UserEntity followedUser = createUser(followed);
        UserEntity followerUser = createUser(follower);
        followedUser.getFollowers().add(followerUser);
        List<UserEntity> allUsers = Arrays.asList(followedUser, followerUser);

        // When
        when(userRepository.getUsers()).thenReturn(allUsers);
        userService.unFollowUser(followed, follower);

        // Then
        verify(userRepository, times(1)).removeFollower(followedUser, followerUser);
    }

    // Utils
    private UserEntity createUser(String handle) {
        return UserEntity.of(UUID.randomUUID(), handle);
    }
}