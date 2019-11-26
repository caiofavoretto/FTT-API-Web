package com.caiofavoretto.APIFTT.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeResponse {
    private String id;
    private String userId;
    private String postId;

    private UserResponse user;
}