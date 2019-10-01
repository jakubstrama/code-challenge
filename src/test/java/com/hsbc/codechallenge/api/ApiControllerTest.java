package com.hsbc.codechallenge.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsbc.codechallenge.api.dto.FollowDto;
import com.hsbc.codechallenge.api.dto.PostDto;
import com.hsbc.codechallenge.exceptions.runtime.UserAlreadyFollowedException;
import com.hsbc.codechallenge.exceptions.runtime.UserNotFollowedException;
import com.hsbc.codechallenge.exceptions.runtime.UserNotFoundException;
import com.hsbc.codechallenge.persistence.entity.PostEntity;
import com.hsbc.codechallenge.persistence.entity.UserEntity;
import com.hsbc.codechallenge.service.PostService;
import com.hsbc.codechallenge.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApiController.class)
@RunWith(SpringRunner.class)
public class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @MockBean
    private UserService userService;

    @Test
    public void testGetWall() throws Exception {
        // Given
        String handle = "user1";
        UserEntity user = createUser(handle);
        PostEntity post1 = new PostEntity(UUID.randomUUID(), user, "Sample text1");
        PostEntity post2 = new PostEntity(UUID.randomUUID(), user, "Sample text2");
        List<PostEntity> expected = Arrays.asList(post1, post2);

        // When
        when(postService.getWall(handle)).thenReturn(expected);

        // Then
        mockMvc.perform(
                get("/wall/" + handle).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message", is("Sample text1")))
                .andExpect(jsonPath("$[1].message", is("Sample text2")));
    }

    @Test
    public void testGetWall_UserNotFoundException() throws Exception {
        // Given
        String handle = "user1";

        // When
        when(postService.getWall(handle)).thenThrow(UserNotFoundException.class);

        // Then
        mockMvc.perform(
                get("/wall/" + handle).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetWall_RandomException() throws Exception {
        // Given
        String handle = "user1";

        // When
        when(postService.getWall(handle)).thenThrow(RuntimeException.class);

        // Then
        mockMvc.perform(
                get("/wall/" + handle).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testGetTimeline() throws Exception {
        // Given
        String handle = "user1";
        UserEntity user = createUser(handle);
        PostEntity post1 = new PostEntity(UUID.randomUUID(), user, "Sample text1");
        PostEntity post2 = new PostEntity(UUID.randomUUID(), user, "Sample text2");
        List<PostEntity> expected = Arrays.asList(post1, post2);

        // When
        when(postService.getTimeline(handle)).thenReturn(expected);

        // Then
        mockMvc.perform(
                get("/timeline/" + handle).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTimeline_UserNotFoundException() throws Exception {
        // Given
        String handle = "user1";

        // When
        when(postService.getTimeline(handle)).thenThrow(UserNotFoundException.class);

        // Then
        mockMvc.perform(
                get("/timeline/" + handle).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testNewPost() throws Exception {
        // Given
        String handle = "user1";
        String message = "Sample text1";
        UserEntity user = createUser(handle);
        PostEntity expected = new PostEntity(UUID.randomUUID(), user, message);


        // When
        when(postService.newPost(message, handle)).thenReturn(expected);

        // Then
        mockMvc.perform(
                post("/post").contentType(MediaType.APPLICATION_JSON)
                        .content(createPostBody(handle, message)))
                .andExpect(status().isOk());
    }

    @Test
    public void testNewPost_UserNotFoundException() throws Exception {
        // Given
        String handle = "user1";
        String message = "Sample text1";

        // When
        when(postService.newPost(message, handle)).thenThrow(UserNotFoundException.class);

        // Then
        mockMvc.perform(
                post("/post").contentType(MediaType.APPLICATION_JSON)
                        .content(createPostBody(handle, message)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void  testFollow() throws Exception {
        // Given
        String followed = "user1";
        String follower = "user2";

        // Then
        mockMvc.perform(
                post("/follow").contentType(MediaType.APPLICATION_JSON)
                        .content(createFollowBody(followed, follower)))
                .andExpect(status().isOk());
    }

    @Test
    public void  testFollow_UserNotFoundException() throws Exception {
        // Given
        String followed = "user1";
        String follower = "user2";

        // When
        doThrow(new UserNotFoundException()).when(userService).followUser(followed, follower);

        // Then
        mockMvc.perform(
                post("/follow").contentType(MediaType.APPLICATION_JSON)
                        .content(createFollowBody(followed, follower)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void  testFollow_UserAlreadyFollowedException() throws Exception {
        // Given
        String followed = "user1";
        String follower = "user2";

        // When
        doThrow(new UserAlreadyFollowedException()).when(userService).followUser(followed, follower);

        // Then
        mockMvc.perform(
                post("/follow").contentType(MediaType.APPLICATION_JSON)
                        .content(createFollowBody(followed, follower)))
                .andExpect(status().isConflict());
    }


    @Test
    public void  testUnfollow() throws Exception {
        // Given
        String followed = "user1";
        String follower = "user2";

        // Then
        mockMvc.perform(
                post("/unfollow").contentType(MediaType.APPLICATION_JSON)
                        .content(createFollowBody(followed, follower)))
                .andExpect(status().isOk());
    }

    @Test
    public void  testUnfollow_UserNotFoundException() throws Exception {
        // Given
        String followed = "user1";
        String follower = "user2";

        // When
        doThrow(new UserNotFoundException()).when(userService).unFollowUser(followed, follower);

        // Then
        mockMvc.perform(
                post("/unfollow").contentType(MediaType.APPLICATION_JSON)
                        .content(createFollowBody(followed, follower)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void  testUnfollow_UserNotFollowedException() throws Exception {
        // Given
        String followed = "user1";
        String follower = "user2";

        // When
        doThrow(new UserNotFollowedException()).when(userService).unFollowUser(followed, follower);

        // Then
        mockMvc.perform(
                post("/unfollow").contentType(MediaType.APPLICATION_JSON)
                        .content(createFollowBody(followed, follower)))
                .andExpect(status().isConflict());
    }

    // Utils
    private UserEntity createUser(String handle) {
        return UserEntity.of(UUID.randomUUID(), handle);
    }

    private String createPostBody(String handle, String message) throws Exception {
        return objectMapper.writeValueAsString(new PostDto(handle, message));
    }

    private String createFollowBody(String followed, String follower) throws Exception {
        return objectMapper.writeValueAsString(new FollowDto(follower, followed));
    }
}