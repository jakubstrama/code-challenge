package com.hsbc.codechallenge.api.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Size;

@Data
@ToString
public class FollowDto {

    @NonNull
    @Size(min = 1, max = 20)
    private String followerHandle;

    @NonNull
    @Size(min = 1, max = 20)
    private String followedHandle;
}
