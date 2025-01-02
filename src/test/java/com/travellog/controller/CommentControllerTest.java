package com.travellog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travellog.domain.Comment;
import com.travellog.domain.Post;
import com.travellog.domain.User;
import com.travellog.repository.UserRepository;
import com.travellog.repository.comment.CommentRepository;
import com.travellog.repository.post.PostRepository;
import com.travellog.request.comment.CommentCreate;
import com.travellog.request.comment.CommentDelete;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void clean() {
        commentRepository.deleteAll();
        userRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 작성")
    void test() throws Exception {

        //given
        User user = User.builder()
                .name("TravelLog")
                .email("jmmmm@naver.com")
                .password("1234")
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .title("12345568790")
                .content("bar")
                .build();

        postRepository.save(post);

        String encryptedPassword = passwordEncoder.encode("123456");


        CommentCreate request = CommentCreate.builder()
                .author("작성자")
                .password(encryptedPassword)
                .content("댓글 좀 10글자 이상 작성하라고")
                .build();
        String json = objectMapper.writeValueAsString(request);

        //Expected
        mockMvc.perform(post("/posts/{postId}/comments", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    @DisplayName("댓글 삭제")
    void test2() throws Exception {

        //given
        User user = User.builder()
                .name("TravelLog")
                .email("jmmmm@naver.com")
                .password("1234")
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .title("12345568790")
                .content("bar")
                .build();

        postRepository.save(post);

        String encryptedPassword = passwordEncoder.encode("123456");


        Comment comment = Comment.builder()
                .author("TravelLog")
                .password(encryptedPassword)
                .content("댓글 좀 10글자 이상 작성하라고")
                .build();
        comment.setPost(post);
        commentRepository.save(comment);

            CommentDelete request = new CommentDelete("123456");
        String json = objectMapper.writeValueAsString(request);

        //Expected
        mockMvc.perform(post("/comments/{commentId}/delete", comment.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());

    }

}