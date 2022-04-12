package com.bd.assignment1.comment.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ParentCommentResDto {
    private String user;
    private String content;
    private List<ChildCommentResDto> children;
}
