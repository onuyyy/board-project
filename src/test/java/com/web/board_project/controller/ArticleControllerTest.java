package com.web.board_project.controller;

import com.web.board_project.config.SecurityConfig;
import com.web.board_project.domain.type.SearchType;
import com.web.board_project.dto.ArticleWithCommentsDto;
import com.web.board_project.dto.UserAccountDto;
import com.web.board_project.service.ArticleService;
import com.web.board_project.service.PaginationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨르롤러 - 게시글")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class) // WebMvcTest를 열어두면 모든 컨트롤러 열기 때문에 컬트롤러 지정하는 게 좋음
class ArticleControllerTest {

    private final MockMvc mvc;

    @MockitoBean
    private ArticleService articleService;
    @MockitoBean
    private PaginationService paginationService;

    // 테스트 패키지는 생성자가 하나일 때 Autowired 직접 명시해 줘야 함
    ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticlesView_thenReturnArticlesView() throws Exception {
        // given
        BDDMockito.given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class)))
                        .willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(1,2,3,4));

        // when
        mvc.perform(get("/articles")) // 요청 url
                .andExpect(status().isOk()) // response
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // response contentType
                .andExpect(view().name("articles/index")) // view 위치
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationBarNumbers"));

        // then
        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 검색어와 함께 호출")
    @Test
    public void givenSearchKeyword_whenSearchingArticlesView_thenReturnArticlesView() throws Exception {
        // given
        SearchType searchType = SearchType.TITLE;
        String searchValue = "title";
        BDDMockito.given(articleService.searchArticles(eq(searchType), eq(searchValue), any(Pageable.class)))
                .willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(1,2,3,4));

        // when
        mvc.perform(get("/articles")
                        .queryParam("searchType", searchType.name())
                        .queryParam("searchValue", searchValue)
                ) // 요청 url
                .andExpect(status().isOk()) // response
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // response contentType
                .andExpect(view().name("articles/index")) // view 위치
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("searchTypes"));
        // then
        then(articleService).should().searchArticles(eq(searchType), eq(searchValue), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 페이징, 정렬 기능")
    @Test
    void givenPagingAndSortingParams_whenSearchingArticlesView_thenReturnsArticlesView() throws Exception {
        // Given
        String sortName = "title";
        String direction = "desc";
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        List<Integer> barNumbers = List.of(1, 2, 3, 4, 5);
        given(articleService.searchArticles(null, null, pageable)).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages())).willReturn(barNumbers);

        // When & Then
        mvc.perform(
                        get("/articles")
                                .queryParam("page", String.valueOf(pageNumber))
                                .queryParam("size", String.valueOf(pageSize))
                                .queryParam("sort", sortName + "," + direction)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attribute("paginationBarNumbers", barNumbers));
        then(articleService).should().searchArticles(null, null, pageable);
        then(paginationService).should().getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages());
    }


    @DisplayName("[view][GET] 게시글 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnArticleView() throws Exception {
        // given
        Long articleId = 1L;
        long totalCount = 1L;

        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());
        given(articleService.getArticleCount()).willReturn(totalCount);

        // when
        mvc.perform(get("/articles/" +  articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"))
                .andExpect(model().attributeExists("articleComments"))
                .andExpect(model().attribute("totalCount", totalCount));

        // then
        then(articleService).should().getArticle(articleId);
        then(articleService).should().getArticleCount();

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

    private ArticleWithCommentsDto createArticleWithCommentsDto() {
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "df",
                LocalDateTime.now(),
                "onuy"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "sdf",
                "sdf",
                "sdf",
                "3rewf",
                "sdf",
                LocalDateTime.now(),
                "sdf",
                LocalDateTime.now(),
                "df"
        );
    }
}