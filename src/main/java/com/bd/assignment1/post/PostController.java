package com.bd.assignment1.post;

import com.bd.assignment1.post.dto.CreatePostReqDto;
import com.bd.assignment1.post.dto.SimplePostResDto;
import com.bd.assignment1.post.dto.ReadPostResDto;
import com.bd.assignment1.post.dto.UpdatePostReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody CreatePostReqDto createPostReqDto) {
        return ResponseEntity.ok(postService.create(createPostReqDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadPostResDto> read(@PathVariable Long id) {
        return ResponseEntity.ok(postService.read(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Long> update(@PathVariable Long id, @RequestBody UpdatePostReqDto updatePostReqDto) {
        return ResponseEntity.ok(postService.update(id, updatePostReqDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return ResponseEntity.ok(postService.delete(id));
    }

    @GetMapping
    public ResponseEntity<List<SimplePostResDto>> list(Pageable pageable) {
        return ResponseEntity.ok(postService.list(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SimplePostResDto>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(postService.search(keyword));
    }
}
