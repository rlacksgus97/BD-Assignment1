package com.bd.assignment1.post;

import com.bd.assignment1.config.jwt.JwtService;
import com.bd.assignment1.post.dto.CreatePostReqDto;
import com.bd.assignment1.post.dto.SimplePostResDto;
import com.bd.assignment1.post.dto.ReadPostResDto;
import com.bd.assignment1.post.dto.UpdatePostReqDto;
import com.bd.assignment1.user.User;
import com.bd.assignment1.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long create(CreatePostReqDto createPostReqDto) {
        Long userId = jwtService.getTokenInfo();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        Post post = Post.builder()
                .title(createPostReqDto.getTitle())
                .content(createPostReqDto.getContent())
                .category(createPostReqDto.getCategory())
                .user(user)
                .build();
        return postRepository.save(post).getId();
    }

    @Transactional
    public ReadPostResDto read(Long id) {
        Long userId = jwtService.getTokenInfo();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));
        if (!post.getReaders().contains(user)) {
            post.getReaders().add(user);
        }
        return ReadPostResDto.builder()
                .title(post.getTitle())
                .writer(post.getUser().getEmail())
                .content(post.getContent())
                .category(post.getCategory())
                .count(post.getReaders().size())
                .build();
    }

    @Transactional
    public Long update(Long id, UpdatePostReqDto updatePostReqDto) {
        Long userId = jwtService.getTokenInfo();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));

        if (post.getUser().equals(user)) {
            post.update(updatePostReqDto);
        } else {
            throw new RuntimeException("게시물을 수정할 권한이 없습니다.");
        }
        return id;
    }

    @Transactional
    public Long delete(Long id) {
        Long userId = jwtService.getTokenInfo();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));

        if (post.getUser().equals(user)) {
            postRepository.delete(post);
        } else {
            throw new RuntimeException("게시물을 삭제할 권한이 없습니다.");
        }
        return id;
    }

    @Transactional
    public List<SimplePostResDto> list(Pageable pageable) {
        Page<SimplePostResDto> postPage = postRepository.findAll(pageable)
                .map(SimplePostResDto::toDto);
        return postPage.getContent();
    }

    @Transactional
    public List<SimplePostResDto> search(String keyword) {
        List<Post> posts = postRepository.findByTitleContaining(keyword);
        List<SimplePostResDto> simplePostResDtos = new ArrayList<>();
        if (posts.size() != 0) {
            for (Post p : posts) {
                simplePostResDtos.add(SimplePostResDto.toDto(p));
            }
        }
        return simplePostResDtos;
    }
}
