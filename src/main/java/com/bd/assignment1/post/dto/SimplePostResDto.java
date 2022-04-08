package com.bd.assignment1.post.dto;

import com.bd.assignment1.post.Post;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimplePostResDto {
    private String title;
    private String writer;

    public static SimplePostResDto toDto(Post post) {
        return SimplePostResDto.builder()
                .title(post.getTitle())
                .writer(post.getUser().getEmail())
                .build();
    }
}
