package com.hsbc.codechallenge.persistence.entity;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class PostEntity {

    @NonNull
    private UUID id;

    @NonNull
    private UserEntity author;

    @NonNull
    private String message;

    private LocalDateTime timestamp;

}
