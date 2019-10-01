package com.hsbc.codechallenge.exceptions.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason="Ooops, something went wrong.")
public class GeneralApiException extends RuntimeException { }
