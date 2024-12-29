package com.travellog.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.travellog.domain.Post;
import com.travellog.repository.PostRepository;
import com.travellog.request.PostCreate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.TravelLog.com", uriPort = 443)
public class PostControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("글 단건 조회")
    void test1() throws Exception {

        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();


        postRepository.save(post);

        //expected
        this.mockMvc.perform(get("/posts/{postId}", 1L).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("post-inquiry",
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("게시글 ID"),
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용")

                        )
                        ));
    }

    @Test
    @DisplayName("글 등록")
    void test2() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .title("나는 바보입니다.")
                .content("반포자이.")
                .build();

        String json = objectMapper.writeValueAsString(request);



        //expected
        this.mockMvc.perform(post("/posts")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("post-create",
                        requestFields(
                                fieldWithPath("title").description("제목").attributes(Attributes.key("constraint").value("좋은 제목 입력해주세요.")),
                                fieldWithPath("content").description("내용").optional()
                        )));
    }
}
