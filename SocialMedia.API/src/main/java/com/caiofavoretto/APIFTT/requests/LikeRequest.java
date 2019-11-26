package com.caiofavoretto.APIFTT.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LikeRequest {
    private  String id;
    private String userId;
    private String postId;
}