package com.travellog.controller;

import com.travellog.request.PostCreate;
import com.travellog.request.PostEdit;
import com.travellog.request.PostSearch;
import com.travellog.response.PostResponse;
import com.travellog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        request.validate();
        postService.write(request);
    }

    /**
     * 단 건 조회 API
     * /posts/{postId}
     */
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    /**
     * 여러 개의 글을 조회 API
     * /posts -> 글 전체 조회(검색 + 페이징)
     */
    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public PostResponse edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        return postService.edit(postId, request);
    }

    @DeleteMapping("/posts/{postId}")
    public void getList(@PathVariable Long postId) {
        postService.delete(postId);
    }
}
