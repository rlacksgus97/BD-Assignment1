package com.bd.assignment1.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment as c where c.parent is null")
    List<Comment> findParentsByPostId(Long postId);
}
