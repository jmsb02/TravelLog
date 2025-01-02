package com.travellog.service;

import com.travellog.crypto.PasswordEncoder;
import com.travellog.domain.Comment;
import com.travellog.domain.Post;
import com.travellog.exception.CommentNotFound;
import com.travellog.exception.InvalidPassword;
import com.travellog.exception.PostNotFound;
import com.travellog.repository.comment.CommentRepository;
import com.travellog.repository.post.PostRepository;
import com.travellog.request.comment.CommentCreate;
import com.travellog.request.comment.CommentDelete;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommentRepository commentRepository;

    @Transactional
    public void write(Long postId, CommentCreate request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        String encryptedPassword = passwordEncoder.encrypt(request.getPassword());

        Comment comment = Comment.builder()
                .author(request.getAuthor())
                .password(encryptedPassword)
                .content(request.getContent())
                .build();

        post.addComment(comment);
    }


    public void delete(Long commentId, CommentDelete request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFound::new);

        String encryptedPassword = comment.getPassword();
        if(!passwordEncoder.matches(request.getPassword(), encryptedPassword)) {
            throw new InvalidPassword();
        }

        commentRepository.delete(comment);
    }
}
