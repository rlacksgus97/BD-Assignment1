package com.bd.assignment1.post.dto;

import com.bd.assignment1.post.Category;
import lombok.Getter;

@Getter
public class UpdatePostReqDto {
    private String title;
    private String content;
    private Category category;
}
