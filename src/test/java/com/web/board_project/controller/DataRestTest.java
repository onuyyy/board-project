package com.web.board_project.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

//@WebMvcTest // MockMvc 사용 할 수 있도록 Bean 주입 해줌, Controller 외에는 로드하지 않음
@Disabled("Spring Data REST 통합테스트는 불필요하므로 제외시킴")
@DisplayName("Data Rest Test - API 테스트")
@Transactional // hibernate 가 로그에 찍히는 것 보니 DB에 영향이 가는 부분이라 트랜잭션 붙임
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class DataRestTest {
    
    private final MockMvc mvc;

    public DataRestTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[api] 게시글 리스트 조회")
    @Test
    void givenNothing_whenRequestingArticles_thenReturnsArticlesJsonResponse() throws Exception {
        // given

        // when
        mvc.perform(MockMvcRequestBuilders.get("/api/articles"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")));

        // then
    }

    @DisplayName("[api] 게시글 단건 조회")
    @Test
    void givenNothing_whenRequestingArticle_thenReturnsArticlesJsonResponse() throws Exception {
        // given

        // when
        mvc.perform(MockMvcRequestBuilders.get("/api/articles/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")));

        // then
    }

    @DisplayName("[api] 게시글 -> 댓글 리스트 조회")
    @Test
    void givenNothing_whenRequestingArticleCommentsFromArticle_thenReturnsArticlesJsonResponse() throws Exception {
        // given

        // when
        mvc.perform(MockMvcRequestBuilders.get("/api/articles/1/articleComments"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")));

        // then
    }

    @DisplayName("[api] 댓글 리스트 조회")
    @Test
    void givenNothing_whenRequestingArticleComments_thenReturnsArticlesJsonResponse() throws Exception {
        // given

        // when
        mvc.perform(MockMvcRequestBuilders.get("/api/articleComments"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")));

        // then
    }

    @DisplayName("[api] 댓글 단건 조회")
    @Test
    void givenNothing_whenRequestingArticleComment_thenReturnsArticlesJsonResponse() throws Exception {
        // given

        // when
        mvc.perform(MockMvcRequestBuilders.get("/api/articleComments/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")));

        // then
    }
}
