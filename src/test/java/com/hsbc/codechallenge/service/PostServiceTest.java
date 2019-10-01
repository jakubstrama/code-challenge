package com.hsbc.codechallenge.service;

import com.hsbc.codechallenge.exceptions.runtime.UserNotFoundException;
import com.hsbc.codechallenge.persistence.PostRepository;
import com.hsbc.codechallenge.persistence.entity.PostEntity;
import com.hsbc.codechallenge.persistence.entity.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private UserService userService;

    @Test
    public void testGetWall() {
        // Given
        String handle = "user1";
        UserEntity user = createUser(handle);
        UserEntity user2 = createUser("user2");
        LocalDateTime now = LocalDateTime.now();

        PostEntity post1 = new PostEntity(UUID.randomUUID(), user, "Sample text1");
        post1.setTimestamp(now);
        PostEntity post2 = new PostEntity(UUID.randomUUID(), user, "Sample text2");
        post2.setTimestamp(now.minusMinutes(2));
        PostEntity post3 = new PostEntity(UUID.randomUUID(), user2, "Sample text3");
        post3.setTimestamp(now.minusMinutes(5));

        List<PostEntity> allPosts = Arrays.asList(post3, post2, post1);

        // When
        when(userService.findUser(handle)).thenReturn(user);
        when(postRepository.getPosts()).thenReturn(allPosts);
        List<PostEntity> expected = postService.getWall(handle);

        // Then
        assertThat(expected).containsSequence(post1, post2);
    }

    @Test
    public void testGetTimeline() {
        // Given
        String handle1 = "user1";
        String handle2 = "user2";
        UserEntity user1 = createUser(handle1);
        UserEntity user2 = createUser(handle2);
        user2.getFollowers().add(user1);

        LocalDateTime now = LocalDateTime.now();

        PostEntity post1 = new PostEntity(UUID.randomUUID(), user1, "Should not be visible");
        post1.setTimestamp(now);
        PostEntity post2 = new PostEntity(UUID.randomUUID(), user2, "Should be visible 1");
        post2.setTimestamp(now.minusMinutes(2));
        PostEntity post3 = new PostEntity(UUID.randomUUID(), user2, "Should be visible 2");
        post3.setTimestamp(now.minusMinutes(5));

        List<PostEntity> allPosts = Arrays.asList(post2, post3, post1);

        // When
        when(userService.findUser(handle1)).thenReturn(user1);
        when(postRepository.getPosts()).thenReturn(allPosts);
        List<PostEntity> expected = postService.getTimeline(handle1);

        // Then
        assertThat(expected).containsSequence(post2, post3);
    }

    @Test
    public void testNewPost_existing_user() {
        // Given
        String handle1 = "user1";
        String message = "Message";
        UserEntity user1 = createUser(handle1);

        // When
        when(userService.findUser(handle1)).thenReturn(user1);
        doAnswer(returnsFirstArg()).when(postRepository).addPost(any(PostEntity.class));
        PostEntity given = postService.newPost(message, handle1);

        // Then
        assertThat(given.getAuthor()).isSameAs(user1);
        assertThat(given.getMessage()).isSameAs(message);
    }

    @Test
    public void testNewPost_new_user() {
        // Given
        String handle1 = "user1";
        String message = "Message";
        UserEntity user1 = createUser(handle1);

        // When
        when(userService.findUser(handle1)).thenThrow(new UserNotFoundException());
        when(userService.createUser(handle1)).thenReturn(user1);
        doAnswer(returnsFirstArg()).when(postRepository).addPost(any(PostEntity.class));
        PostEntity given = postService.newPost(message, handle1);

        // Then
        verify(userService, times(1)).findUser(handle1);
        verify(userService, times(1)).createUser(handle1);
        assertThat(given.getAuthor()).isSameAs(user1);
        assertThat(given.getMessage()).isSameAs(message);
    }

    // Utils
    private UserEntity createUser(String handle) {
        return UserEntity.of(UUID.randomUUID(), handle);
    }

}