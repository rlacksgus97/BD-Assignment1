package com.bd.assignment1.comment;

import com.bd.assignment1.comment.dto.ChildCommentResDto;
import com.bd.assignment1.comment.dto.ParentCommentResDto;
import com.bd.assignment1.comment.dto.CreateCommentReqDto;
import com.bd.assignment1.comment.dto.UpdateCommentReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<Long> create(@PathVariable Long postId,
                                       @RequestBody CreateCommentReqDto createCommentReqDto) {
        return ResponseEntity.ok(commentService.create(postId, createCommentReqDto));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<ParentCommentResDto>> getParentList(@PathVariable Long postId, Pageable pageable) {
        return ResponseEntity.ok(commentService.getParentList(postId, pageable));
    }

    @GetMapping("/{postId}/{commentId}")
    public ResponseEntity<List<ChildCommentResDto>> getChildrenList(@PathVariable Long commentId, Pageable pageable) {
        return ResponseEntity.ok(commentService.getChildrenList(commentId, pageable));
    }

    @PostMapping("/{postId}/{commentId}")
    public ResponseEntity<Long> update(@PathVariable Long commentId,
                                       @RequestBody UpdateCommentReqDto updateCommentReqDto) {
        return ResponseEntity.ok(commentService.update(commentId, updateCommentReqDto));
    }

    @DeleteMapping("/{postId}/{commentId}")
    public ResponseEntity<Long> delete(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.delete(commentId));
    }
}
