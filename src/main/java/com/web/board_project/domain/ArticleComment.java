package com.web.board_project.domain;

import java.time.LocalDateTime;

public class ArticleComment {
    private long id;
    private Article article;
    private String content;

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
}
