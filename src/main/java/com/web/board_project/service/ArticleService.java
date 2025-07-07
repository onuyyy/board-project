package com.web.board_project.service;

import com.web.board_project.domain.Article;
import com.web.board_project.domain.type.SearchType;
import com.web.board_project.dto.ArticleDto;
import com.web.board_project.dto.ArticleWithCommentsDto;
import com.web.board_project.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {

        // 검색어가 없는 경우
        if (searchKeyword == null || searchKeyword.isBlank()) {
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }

        // 검색어가 있는 경우
        return switch (searchType) {
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);

            // 해시태그에서 #은 자동으로 넣어주자 todo # 어떻게 할지 리팩토링
            case HASHTAG-> articleRepository.findByHashtag(searchKeyword, pageable).map(ArticleDto::from);
        };
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(Long articleId) {

        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId : " + articleId));
    }

    public void saveArticle(ArticleDto dto) {
        articleRepository.save(dto.toEntity());
    }

    public void updateArticle(ArticleDto dto) {
        /*
            Optional<Article> article = articleRepository.findById(dto.id());
            if (article.isPresent()) {
                Article articleEntity = article.get();
                articleEntity.setTitle(dto.title());
                articleRepository.save(articleEntity);
            }
            id가 있다는 걸 이미 알고 있는 상태에서 db까지 굳이 select를 굳이 날려야 하는가?
        */

        try {
            Article article = articleRepository.getReferenceById(dto.id());

            // title, content는 not null 필드라 방어 로직 추가
            if (dto.title() != null) { article.setTitle(dto.title()); }
            if (dto.content() != null) { article.setContent(dto.content()); }

            // hashtag는 null 필드라서 dto에 있는 걸 그대로 넣으면 된다
            article.setHashtag(dto.hashtag());

        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패, 게시글을 찾을 수 없습니다 - dto: {}", dto);
        }

        // save는 필요 없다... 왜?
        // 트랜잭션이 걸려 있어서 영속성컨텍스트가 변경을 감지하고 이 함수가 끝나면 쿼리를 날린다
    }

    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }

    public long getArticleCount() {
        return articleRepository.count();
    }

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticlesViaHashtag(String hashtag, Pageable pageable) {

        if (hashtag == null || hashtag.isBlank()) {
            return Page.empty(pageable);
        }

        return articleRepository.findByHashtag(hashtag, pageable).map(ArticleDto::from);
    }

    public List<String> getHashtags() {
        return articleRepository.findAllDistinctHashtags();
    }
}