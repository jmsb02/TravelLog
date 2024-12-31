package com.travellog.service;

import com.travellog.domain.Post;
import com.travellog.exception.PostNotFound;
import com.travellog.exception.UserNotFound;
import com.travellog.repository.PostRepository;
import com.travellog.repository.UserRepository;
import com.travellog.request.PostCreate;
import com.travellog.request.PostEdit;
import com.travellog.request.PostSearch;
import com.travellog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public void write(Long userId, PostCreate postCreate) {

        var user = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);
        //PostCreate -> Entity

        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(post);

    }

    public PostResponse get(Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        return PostResponse.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    public List<PostResponse> getList(PostSearch postSearch) {

        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        post.edit(postEdit.getTitle(), postEdit.getContent()); //setter 대체

        return new PostResponse(post);

    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        postRepository.delete(post);
    }
}
