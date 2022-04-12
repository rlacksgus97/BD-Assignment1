package com.bd.assignment1.comment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChildCommentResDto {
    private String user;
    private String content;
}
