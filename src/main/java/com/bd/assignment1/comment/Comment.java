package com.bd.assignment1.comment;

import com.bd.assignment1.post.Post;
import com.bd.assignment1.user.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Comment {
    @Id
    private Long id;

    private String content;

    @ManyToOne
    private Post post;

    @ManyToOne
    private User user;
}
