package com.bd.assignment1.comment.dto;

import lombok.Getter;

@Getter
public class CreateCommentReqDto {
    private Long parentId;
    private String content;
}
