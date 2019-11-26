package com.caiofavoretto.APIFTT.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    private String id;
    private String userId;
    private String title;
    private String description;
    private int likes;
    private int comments;

    private UserResponse user;
    private Boolean liked;
}