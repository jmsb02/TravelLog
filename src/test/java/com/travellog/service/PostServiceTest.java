package com.travellog.service;

import com.travellog.domain.Post;
import com.travellog.domain.User;
import com.travellog.exception.PostNotFound;
import com.travellog.repository.PostRepository;
import com.travellog.repository.UserRepository;
import com.travellog.request.PostCreate;
import com.travellog.request.PostEdit;
import com.travellog.request.PostSearch;
import com.travellog.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        //given
        var user = User.builder()
                .name("TravelLog")
                .email("jmmmm@naver.com")
                .password("1234")
                .build();
        userRepository.save(user);

        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        //when
        postService.write(user.getId(), postCreate);

        //then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());

    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {

        //given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();

        postRepository.save(requestPost);

        //when
        PostResponse post = postService.get(requestPost.getId());

        //then
        assertNotNull(post);
        assertEquals("foo", post.getTitle());
        assertEquals("bar", post.getContent());

    }

    @Test
    @DisplayName("글 여러 개 조회")
    void test3() {
        //given

        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("트래블로그 제목 " + i)
                            .content("트래블로그 내용 " + i)
                            .build();
                })
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();

        //when
        List<PostResponse> posts = postService.getList(postSearch);

        //then
        assertEquals(10L, posts.size()); //전체 사이즈
        assertEquals("트래블로그 제목 19", posts.get(0).getTitle());

    }

    @Test
    @DisplayName("글 제목 수정")
    void test4() {

        //given
        Post post = Post.builder()
                .title("트래블로그 제목")
                .content("트래블로그 내용")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("트래블로그 제목 edit")
                .content("트래블로그 내용 edit")
                .build();


        //when
        postService.edit(post.getId(), postEdit);

        //then
        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id = " + post.getId()));

        assertEquals("트래블로그 제목 edit", changePost.getTitle());
        assertEquals("트래블로그 내용 edit", changePost.getContent()
        );


    }

    @Test
    @DisplayName("글 내용 수정")
    void test5() {

        //given
        Post post = Post.builder()
                .title("트래블로그 제목")
                .content("트래블로그 내용")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("트래블로그 제목 edit")
                .content("트래블로그 내용 edit")
                .build();


        //when
        postService.edit(post.getId(), postEdit);

        //then
        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id = " + post.getId()));

        assertEquals("트래블로그 제목 edit", changePost.getTitle());
        assertEquals("트래블로그 내용 edit", changePost.getContent()
        );


    }

    @Test
    @DisplayName("게시글 삭제")
    void test6() {

        //given
        Post post = Post.builder()
                .title("트래블로그 제목")
                .content("트래블로그 내용")
                .build();

        postRepository.save(post);

        //when
        postService.delete(post.getId());

        //then
        assertEquals(0, postRepository.count());

    }

    @Test
    @DisplayName("글 1개 조회 -> 존재하지 않는 글")
    void test7() {

        //given
        Post post = Post.builder()
                .title("트래블로그 제목")
                .content("트래블로그 내용")
                .build();
        postRepository.save(post);

        //Expected
        assertThrows(PostNotFound.class, () -> {
            postService.get(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는 글")
    void test8() {

        //given
        Post post = Post.builder()
                .title("트래블로그 제목")
                .content("트래블로그 내용")
                .build();

        postRepository.save(post);

        //Expect
        assertThrows(PostNotFound.class, () -> {
            postService.delete(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("글 내용 수정 -> 존재하지 않는 글")
    void test9() {

        //given
        Post post = Post.builder()
                .title("트래블로그 제목")
                .content("트래블로그 내용")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("트래블로그 제목 edit")
                .content("트래블로그 내용 edit")
                .build();


        //Expect
        assertThrows(PostNotFound.class, () -> {
            postService.edit(post.getId() + 1L, postEdit);
        });

    }




}