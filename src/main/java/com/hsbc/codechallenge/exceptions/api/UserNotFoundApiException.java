package com.hsbc.codechallenge.exceptions.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason="The user was not found")
public class UserNotFoundApiException extends RuntimeException {}
