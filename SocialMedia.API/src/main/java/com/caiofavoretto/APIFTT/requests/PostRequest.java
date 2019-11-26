package com.caiofavoretto.APIFTT.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class PostRequest {
    private  String id;
    private String userId;
    private String title;
    @NotBlank
    private String description;
}
