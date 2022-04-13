package com.bd.assignment1.comment;

import com.bd.assignment1.comment.dto.UpdateCommentReqDto;
import com.bd.assignment1.post.Post;
import com.bd.assignment1.post.dto.UpdatePostReqDto;
import com.bd.assignment1.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;

    @ManyToOne
    private Post post;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children;

    @ManyToOne
    private Comment parent;

    public void update(UpdateCommentReqDto updateCommentReqDto) {
        this.content = updateCommentReqDto.getContent();
    }

    public void addChild(Comment comment) {
        comment.setParent(this);
        this.getChildren().add(comment);
    }
}
