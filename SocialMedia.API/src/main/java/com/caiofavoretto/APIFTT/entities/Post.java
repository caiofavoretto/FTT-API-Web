package com.caiofavoretto.APIFTT.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "posts")
public class Post {
    @Id
    private String id;
    @Indexed
    private String userId;
    private String title;
    private String description;
    private int likes;
    private int comments;
}
