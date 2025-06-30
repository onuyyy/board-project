package com.web.board_project.repository;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.web.board_project.domain.Article;
import com.web.board_project.domain.QArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>, // 기본 검색
        QuerydslBinderCustomizer<QArticle>
{

    // Article은 UserAccount 를 들고 있다
    // @ManyToOne(optional = false) @JoinColumn(name = "user_id") private UserAccount userAccount; // 유저 정보 (ID)
    // _ 사용하여 타고 들어가면 된다

    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String content, Pageable pageable);
    Page<Article> findByUserAccount_UserIdContaining(String userId, Pageable pageable);
    Page<Article> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);

    // hashtag는 정확한 분류이므로 containing 사용 안 함
    Page<Article> findByHashtag(String title, Pageable pageable);

    /*
        querydsl
        QuerydslPredicateExecutor : Entity 안에 있는 모든 필드에 대한 기본 검색 기능을 추가해줌
     */
    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        bindings.excludeUnlistedProperties(true); // 리스팅 하지 않은 프로퍼티를 검색에서 제외함 > 기본 값음 false로 되어 있음
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy); // 원하는 필드 추가
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // 검색 파라미터는 하나만 받는다 like '%%'
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}