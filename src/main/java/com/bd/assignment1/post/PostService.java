package com.bd.assignment1.post;

import com.bd.assignment1.config.jwt.JwtService;
import com.bd.assignment1.post.dto.CreatePostReqDto;
import com.bd.assignment1.post.dto.ReadPostResDto;
import com.bd.assignment1.post.dto.UpdatePostReqDto;
import com.bd.assignment1.user.User;
import com.bd.assignment1.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long create(CreatePostReqDto createPostReqDto) throws Exception {
        Long userId = jwtService.getTokenInfo();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("존재하지 않는 유저입니다."));
        Post post = Post.builder()
                .title(createPostReqDto.getTitle())
                .content(createPostReqDto.getContent())
                .category(createPostReqDto.getCategory())
                .user(user)
                .build();
        return postRepository.save(post).getId();
    }

    @Transactional
    public ReadPostResDto read(Long id) throws Exception {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new Exception("존재하지 않는 게시글입니다."));
        return ReadPostResDto.builder()
                .title(post.getTitle())
                .writer(post.getUser().getEmail())
                .content(post.getContent())
                .category(post.getCategory())
                .build();
    }

    @Transactional
    public Long update(Long id, UpdatePostReqDto updatePostReqDto) throws Exception {
        Long userId = jwtService.getTokenInfo();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("존재하지 않는 유저입니다."));

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new Exception("존재하지 않는 게시글입니다."));

        if (post.getUser().equals(user)) {
            post.update(updatePostReqDto);
        } else {
            throw new Exception("게시물을 수정할 권한이 없습니다.");
        }
        return id;
    }

    @Transactional
    public Long delete(Long id) throws Exception {
        Long userId = jwtService.getTokenInfo();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("존재하지 않는 유저입니다."));

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new Exception("존재하지 않는 게시글입니다."));

        if (post.getUser().equals(user)) {
            postRepository.delete(post);
        } else {
            throw new Exception("게시물을 삭제할 권한이 없습니다.");
        }
        return id;
    }
}
