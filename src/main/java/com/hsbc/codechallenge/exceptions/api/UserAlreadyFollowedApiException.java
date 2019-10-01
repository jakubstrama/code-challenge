package com.hsbc.codechallenge.exceptions.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason="The user is already being followed")
public class UserAlreadyFollowedApiException extends RuntimeException {}
