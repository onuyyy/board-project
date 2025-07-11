package com.web.board_project.dto.request;

import com.web.board_project.dto.ArticleCommentDto;
import com.web.board_project.dto.UserAccountDto;

/**
 * DTO for {@link com.web.board_project.domain.ArticleComment}
 */
public record ArticleCommentRequest(Long articleId, String content)  {

  public static ArticleCommentRequest of(Long articleId, String content) {
    return new ArticleCommentRequest(articleId, content);
  }

  public ArticleCommentDto toDto(UserAccountDto userAccountDto) {
    return ArticleCommentDto.of(
            articleId,
            userAccountDto,
            content
    );
  }
}