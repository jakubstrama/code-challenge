package com.hsbc.codechallenge.persistence.entity;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor(staticName = "of")
public class UserEntity {

    @NonNull
    private UUID id;

    @NonNull
    private String handle;

    private List<UserEntity> followers = new ArrayList<>();

}
