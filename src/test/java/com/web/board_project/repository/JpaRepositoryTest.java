package com.web.board_project.repository;

import com.web.board_project.config.JpaConfig;
import com.web.board_project.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class) // DataJpaTest 라 다른 클래스 로딩 안 되기 때문에 명시해줌
@DataJpaTest // 슬라이스 테스트 @Entity와 Respository 인터페이스만 로딩한다
class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(
                @Autowired ArticleRepository articleRepository,
                @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select test")
    @Test
    void selectTest() {
        // given
        List<Article> articles = articleRepository.findAll();
        // when

        // then
        assertThat(articles).isNotNull().hasSize(100);
    }

    @DisplayName("insertTest")
    @Test
    void insertTest() {
        // given
        long prevCnt = articleRepository.count();
        // when
        Article savedArticle = articleRepository.save(Article.of("spring", "spring", "#spring"));
        // then
        assertThat(articleRepository.count()).isEqualTo(prevCnt + 1);
    }

    @DisplayName("update Test")
    @Test
    void updateTest() {
        // given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updatedHashtag = "update!!";
        article.setHashtag(updatedHashtag);

        // when
        Article savedArticle = articleRepository.saveAndFlush(article);

        // then
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updatedHashtag);
    }

    @DisplayName("delete Test")
    @Test
    void deleteTest() {
        // given
        Article article = articleRepository.findById(1L).orElseThrow();
        long prevCnt = articleRepository.count();
        long prevCommentCnt = articleCommentRepository.count();
        int deletedCommentSize = article.getArticleComments().size();

        // when
        articleRepository.delete(article);

        // then
        assertThat(articleRepository.count()).isEqualTo(prevCnt - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(prevCommentCnt - deletedCommentSize);
    }
}