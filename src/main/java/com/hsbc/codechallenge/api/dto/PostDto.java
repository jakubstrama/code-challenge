package com.hsbc.codechallenge.api.dto;

import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Size;

@Data
public class PostDto {

    @NonNull
    @Size(min = 1, max = 20)
    private String handle;

    @NonNull
    @Size(min = 1, max = 140)
    private String message;
}
