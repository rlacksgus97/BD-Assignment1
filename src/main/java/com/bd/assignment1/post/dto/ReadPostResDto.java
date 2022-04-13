package com.bd.assignment1.post.dto;

import com.bd.assignment1.post.Category;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReadPostResDto {
    private String title;
    private String writer;
    private String content;
    private Category category;
    private Long watched;
}
