package com.web.board_project.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨르롤러 - 게시글")
@WebMvcTest(ArticleController.class) // WebMvcTest를 열어두면 모든 컨트롤러 열기 때문에 컬트롤러 지정하는 게 좋음
class ArticleControllerTest {

    private final MockMvc mvc;

    // 테스트 패키지는 생성자가 하나일 때 Autowired 직접 명시해 줘야 함
    ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticlesView_thenReturnArticlesView() throws Exception {
        // given

        // when
        mvc.perform(get("/articles")) // 요청 url
                .andExpect(status().isOk()) // response
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // response contentType
                .andExpect(view().name("articles/index")) // view 위치
                .andExpect(model().attributeExists("articles"));
        // then

    }

    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnArticleView() throws Exception {
        // given

        // when
        mvc.perform(get("/articles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("articleComments"))
                .andExpect(model().attributeExists("article"));
        // then

    }

    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingSearchingView_thenReturnSearchingView() throws Exception {
        // given

        // when
        mvc.perform(get("/articles/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("article/search"))
                .andExpect(model().attributeExists("article"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
        // then

    }

    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleHashtagSearchingView_thenReturnArticleHashtagSearchingView() throws Exception {
        // given

        // when
        mvc.perform(get("/article/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(view().name("article/search-hashtag"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
        // then

    }
}