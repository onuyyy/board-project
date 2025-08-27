package com.web.board_project.dto.response;


import com.web.board_project.dto.ArticleDto;

import java.io.Serializable;
import java.time.LocalDateTime;


public record ArticleResponse(
        Long id,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String email,
        String nickname
) implements Serializable {

    public static ArticleResponse of(Long id, String title, String content, String hashtag, LocalDateTime createdAt, String email, String nickname) {
        return new ArticleResponse(id, title, content, hashtag, createdAt, email, nickname);
    }

    public static ArticleResponse from(ArticleDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return new ArticleResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.hashtagDtos().stream()
                        .map(hashtagDto -> "#" + hashtagDto.hashtagName())
                        .reduce((h1, h2) -> h1 + " " + h2)
                        .orElse(""),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname
        );
    }

}