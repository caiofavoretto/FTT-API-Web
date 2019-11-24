package com.caiofavoretto.APIFTT.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class CommentRequest {
    private  String id;
    private String userId;
    private String postId;
    @NotBlank
    private String description;
}