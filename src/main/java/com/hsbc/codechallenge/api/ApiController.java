package com.hsbc.codechallenge.api;


import com.hsbc.codechallenge.api.dto.FollowDto;
import com.hsbc.codechallenge.api.dto.PostDto;
import com.hsbc.codechallenge.exceptions.api.GeneralApiException;
import com.hsbc.codechallenge.exceptions.api.UserAlreadyFollowedApiException;
import com.hsbc.codechallenge.exceptions.api.UserNotFollowedApiException;
import com.hsbc.codechallenge.exceptions.api.UserNotFoundApiException;
import com.hsbc.codechallenge.exceptions.runtime.UserAlreadyFollowedException;
import com.hsbc.codechallenge.exceptions.runtime.UserNotFollowedException;
import com.hsbc.codechallenge.exceptions.runtime.UserNotFoundException;
import com.hsbc.codechallenge.persistence.entity.PostEntity;
import com.hsbc.codechallenge.service.PostService;
import com.hsbc.codechallenge.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ApiController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(ApiController.class);

    @GetMapping("/wall/{handle}")
    public List<PostEntity> getWall(@PathVariable String handle) {
        try {
            return postService.getWall(handle);
        } catch (UserNotFoundException e) {
            logger.info("User not found for handle: " + e.getMessage());
            throw new UserNotFoundApiException();
        } catch (Exception e) {
            logger.warn("Unknown exception while calling method wall", e);
            throw new GeneralApiException();
        }
    }

    @GetMapping("/timeline/{handle}")
    public List<PostEntity> getTimeline(@PathVariable String handle) {
        try {
            return postService.getTimeline(handle);
        } catch (UserNotFoundException e) {
            logger.info("User not found for handle: " + e.getMessage());
            throw new UserNotFoundApiException();
        } catch (Exception e) {
            logger.warn("Unknown exception while calling method timeline", e);
            throw new GeneralApiException();
        }
    }

    @PostMapping("/follow")
    public ResponseEntity follow(@RequestBody @Valid FollowDto followDto) {
        try {
            userService.followUser(followDto.getFollowedHandle(), followDto.getFollowerHandle());
            return new ResponseEntity(HttpStatus.OK);
        } catch (UserNotFoundException e) {
            logger.info("User not found for handle:" + e.getMessage());
            throw new UserNotFoundApiException();
        } catch (UserAlreadyFollowedException e) {
            throw new UserAlreadyFollowedApiException();
        } catch (Exception e) {
            logger.warn("Unknown exception while calling method follow", e);
            throw new GeneralApiException();
        }
    }

    @PostMapping("/unfollow")
    public ResponseEntity unfollow(@RequestBody @Valid FollowDto followDto) {
        try {
            userService.unFollowUser(followDto.getFollowedHandle(), followDto.getFollowerHandle());
            return new ResponseEntity(HttpStatus.OK);
        } catch (UserNotFoundException e) {
            logger.info("User not found for handle:" + e.getMessage());
            throw new UserNotFoundApiException();
        } catch (UserNotFollowedException e) {
            throw new UserNotFollowedApiException();
        } catch (Exception e) {
            logger.warn("Unknown exception while calling method unfollow", e);
            throw new GeneralApiException();
        }
    }

    @PostMapping("/post")
    public PostEntity newPost(@RequestBody @Valid PostDto postDto) {
        try {
            return postService.newPost(postDto.getMessage(), postDto.getHandle());
        } catch (Exception e) {
            logger.warn("Unknown exception while calling method post", e);
            throw new GeneralApiException();
        }
    }
}
