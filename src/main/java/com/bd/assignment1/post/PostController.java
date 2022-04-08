package com.bd.assignment1.post;

import com.bd.assignment1.post.dto.CreatePostReqDto;
import com.bd.assignment1.post.dto.ReadPostResDto;
import com.bd.assignment1.post.dto.UpdatePostReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody CreatePostReqDto createPostReqDto) throws Exception {
        return ResponseEntity.ok(postService.create(createPostReqDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadPostResDto> read(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(postService.read(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Long> update(@PathVariable Long id, @RequestBody UpdatePostReqDto updatePostReqDto) throws Exception {
        return ResponseEntity.ok(postService.update(id, updatePostReqDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(postService.delete(id));
    }
}