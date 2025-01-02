package com.travellog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travellog.config.TravelLogMockUser;
import com.travellog.domain.Post;
import com.travellog.domain.User;
import com.travellog.repository.post.PostRepository;
import com.travellog.repository.UserRepository;
import com.travellog.request.post.PostCreate;
import com.travellog.request.post.PostEdit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성 요청시 Hello World를 출력한다.")
//    @WithMockUser(username = "jmmmm@naver.com", roles = {"ADMIN"})
    @TravelLogMockUser(name = "홍길동", email = "jmmmm@naver.com", password = "1234")
    void test() throws Exception {

        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

//    @Test
//    @DisplayName("글 작성시 title값은 필수다.")
//    void test2() throws Exception {
//        //given
//        PostCreate request = PostCreate.builder()
//                .content("내용입니다.")
//                .build();
//
//        String json = objectMapper.writeValueAsString(request);
//
//
//        //expected
//        mockMvc.perform(post("/posts")
//                        .contentType(APPLICATION_JSON)
//                        .content(json)
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.code").value("400"))
//                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
//                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
//                .andDo(print());
//    }

    @Test
//    @WithMockUser(username = "jmmmm@naver.com", roles = {"ADMIN"})
    @TravelLogMockUser(name = "홍길동", email = "jmmmm@naver.com", password = "1234")
    @DisplayName("글 작성")
    void test3() throws Exception {

        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);


        //when
        mockMvc.perform(post("/posts")
                        .header("authorization", "TravelLog")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

//    @Test
//    @DisplayName("단일 글 조회")
//    void test4() throws Exception {
//
//        //given
//        Post post = Post.builder()
//                .title("12345568790")
//                .content("bar")
//                .build();
//
//
//        postRepository.save(post);
//
//
//        //expected(when + then)
//        mockMvc.perform(get("/posts/{postId}", post.getId())
//                        .contentType(APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(post.getId()))
//                .andExpect(jsonPath("$.title").value(post.getTitle()))
//                .andExpect(jsonPath("$.content").value(post.getContent()))
//                .andDo(print());
//
//    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {

        User user = User.builder()
                .name("TravelLog")
                .email("jmmmm@naver.com")
                .password("1234")
                .build();
        
        userRepository.save(user);

        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("트래블로그 제목 " + i)
                            .content("트래블로그 내용 " + i)
                            .user(user)
                            .build();
                })
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);


        //expected(when + then)
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("트래블로그 제목 19"))
                .andExpect(jsonPath("$[0].content").value("트래블로그 내용 19"))
                .andDo(print());
    }

    @Test
    @DisplayName("페이지 0을 요청하면 첫 페이지를 가져온다.")
    void test6() throws Exception {

        User user = userRepository.findAll().get(0);

        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("트래블로그 제목 " + i)
                            .content("트래블로그 내용 " + i)
                            .user(user)
                            .build();
                })
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);


        //expected(when + then)
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("트래블로그 제목 19"))
                .andExpect(jsonPath("$[0].content").value("트래블로그 내용 19"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 제목 수정")
    @TravelLogMockUser
    void test7() throws Exception {
        //given

        User user = userRepository.findAll().get(0);

        userRepository.save(user);

        Post post = Post.builder()
                .title("트래블로그 제목")
                .content("트래블로그 내용")
                .user(user)
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("트래블로그 제목 edit")
                .content("트래블로그 내용")
                .build();

        //expected(when + then)
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 제목 삭제")
    @TravelLogMockUser
    void test8() throws Exception {

        //given
        User user = userRepository.findAll().get(0);

        userRepository.save(user);


        Post post = Post.builder()
                .title("트래블로그 제목")
                .content("트래블로그 내용")
                .user(user)
                .build();

        postRepository.save(post);


        //expected(when + then)
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test9() throws Exception {

        //expected(when + then)
        mockMvc.perform(get("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    @WithMockUser(username = "jmmmm@naver.com", roles = {"ADMIN"})
    void test10() throws Exception {

        PostEdit postEdit = PostEdit.builder()
                .title("트래블로그 제목 edit")
                .content("트래블로그 내용")
                .build();

        //expected(when + then)
        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}