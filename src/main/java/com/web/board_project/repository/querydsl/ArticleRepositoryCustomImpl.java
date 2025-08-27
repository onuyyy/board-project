package com.web.board_project.repository.querydsl;

import com.querydsl.jpa.JPQLQuery;
import com.web.board_project.domain.Article;
import com.web.board_project.domain.QArticle;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

/**
 * Impl 이라고 해두면 querydsl이 이 자식을 인식하기 시작한다
 * Impl 이라고 붙이는 건 약속이다
 */
public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom {

    public ArticleRepositoryCustomImpl() {
        super(Article.class); // Article 도메인이라는 걸 알려준다
    }

    @Override
    public List<String> findAllDistinctHashtags() {
        // Qclass를 사용한다
        QArticle article = QArticle.article;

        // return tpye을 일치시켜준다
        // from부터 시작한다
        return from(article)
                .distinct() // 중복 제거
                .select(article.hashtags.any().hashtagName)
                .where(article.hashtags.isNotEmpty())
                .fetch();
    }
}
