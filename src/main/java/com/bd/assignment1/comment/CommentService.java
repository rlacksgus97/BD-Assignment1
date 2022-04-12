package com.bd.assignment1.comment;

import com.bd.assignment1.comment.dto.ChildCommentResDto;
import com.bd.assignment1.comment.dto.ParentCommentResDto;
import com.bd.assignment1.comment.dto.CreateCommentReqDto;
import com.bd.assignment1.comment.dto.UpdateCommentReqDto;
import com.bd.assignment1.config.jwt.JwtService;
import com.bd.assignment1.post.Post;
import com.bd.assignment1.post.PostRepository;
import com.bd.assignment1.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final JwtService jwtService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Long create(Long postId, CreateCommentReqDto createCommentReqDto) {
        User user = jwtService.getUserFromJwt();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));
        Comment comment = Comment.builder()
                .content(createCommentReqDto.getContent())
                .build();

        post.addComment(comment);
        user.addComment(comment);

        if (createCommentReqDto.getParentId() != 0) {
            Comment parent = commentRepository.findById(createCommentReqDto.getParentId())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 부모 댓글입니다."));
            if (parent.getParent() != null && parent.getParent().getId() > 0) {
                throw new RuntimeException("대댓글은 1개까지만 허용됩니다.");
            }
            parent.addChild(comment);
        }

        return commentRepository.save(comment).getId();
    }

    //댓글 5개만, 각 댓글 당 대댓글도 5개만
    @Transactional
    public List<ParentCommentResDto> getParentList(Long postId, Pageable pageable) {
        List<ParentCommentResDto> parentCommentResDtos = new ArrayList<>();
        List<Comment> comments = commentRepository.findByPostIdAndParentId(postId, null, pageable);
        for (Comment p : comments) {
            List<ChildCommentResDto> childCommentResDtos = this.getChildrenList(p.getId(), PageRequest.of(0, 5));
            parentCommentResDtos.add(ParentCommentResDto.builder()
                    .user(p.getUser().getEmail())
                    .content(p.getContent())
                    .children(childCommentResDtos)
                    .build());
        }

        return parentCommentResDtos;
    }

    @Transactional
    public List<ChildCommentResDto> getChildrenList(Long parentId, Pageable pageable) {
        List<ChildCommentResDto> childCommentResDtos = new ArrayList<>();
        List<Comment> children = commentRepository.findByParentId(parentId, pageable);
        for (Comment c : children) {
            childCommentResDtos.add(ChildCommentResDto.builder()
                    .user(c.getUser().getEmail())
                    .content(c.getContent())
                    .build());
        }
        return childCommentResDtos;
    }

    @Transactional
    public Long update(Long commentId, UpdateCommentReqDto updateCommentReqDto) {
        User user = jwtService.getUserFromJwt();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 댓글입니다."));
        if (comment.getUser().equals(user)) {
            comment.update(updateCommentReqDto);
        } else {
            throw new RuntimeException("댓글을 수정할 권한이 없습니다.");
        }
        return commentId;
    }

    @Transactional
    public Long delete(Long commentId) {
        User user = jwtService.getUserFromJwt();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 댓글입니다."));
        if (comment.getUser().equals(user)) {
            commentRepository.delete(comment);
        } else {
            throw new RuntimeException("댓글을 삭제할 권한이 없습니다.");
        }
        return commentId;
    }
}
