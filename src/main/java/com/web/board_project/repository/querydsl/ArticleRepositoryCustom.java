package com.web.board_project.repository.querydsl;

import java.util.List;

public interface ArticleRepositoryCustom {

    /*
         도메인이 아니라 String으로 내려받기 때문에 querydsl을 사용한다
     */
    List<String> findAllDistinctHashtags();
}
